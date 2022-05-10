package de.kxmischesdomi.boatcontainer.common.entity;

import de.kxmischesdomi.boatcontainer.common.registry.ModEntities;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FurnaceBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0
 */
public class FurnaceBoatEntity extends BoatWithBlockEntity {

	private static final EntityDataAccessor<Boolean> DATA_ID_FUEL = SynchedEntityData.defineId(FurnaceBoatEntity.class, EntityDataSerializers.BOOLEAN);
	private short fuel;
	private static final Ingredient INGREDIENT = Ingredient.of(Items.COAL, Items.CHARCOAL);

	public FurnaceBoatEntity(EntityType<? extends Boat> entityType, Level world) {
		super(entityType, world);
	}

	public FurnaceBoatEntity(EntityType<? extends Boat> type, Level world, double x, double y, double z) {
		super(type, world, x, y, z);
	}

	public FurnaceBoatEntity(Level world) {
		super(ModEntities.CHEST_BOAT, world);
	}

	@Override
	public void dropItems(DamageSource damageSource) {
		if (!damageSource.isExplosion()) {
			this.spawnAtLocation(Items.FURNACE.asItem());
		}
	}

	@Override
	protected void defineSynchedData() {
		super.defineSynchedData();
		this.entityData.define(DATA_ID_FUEL, false);
	}

	@Override
	public void tick() {
		super.tick();
		if (!this.level.isClientSide()) {
			if (this.fuel > 0) {
				--this.fuel;
			}
			this.setHasFuel(this.fuel > 0);
		}
		if (this.hasFuel() && random.nextInt(6) == 0) {

			double rot = getYRot() % 360;
			rot = rot * Math.PI / 180;

			double s = Math.sin(rot);
			double c = Math.cos(rot);

			double add = -0.35;

			double x = - add * s;
			double z = add * c;

			this.level.addParticle(ParticleTypes.LARGE_SMOKE, this.getX() + x, this.getY() + 1, this.getZ() + z, 0.0, 0.0, 0.0);
		}
	}

	@Override
	public InteractionResult interact(Player player, InteractionHand interactionHand) {
		ItemStack itemStack = player.getItemInHand(interactionHand);
		if (INGREDIENT.test(itemStack) && this.fuel + 3600 <= 32000) {
			if (!player.getAbilities().instabuild) {
				itemStack.shrink(1);
			}
			this.fuel += 3600;
			return InteractionResult.sidedSuccess(this.level.isClientSide);
		}
		return super.interact(player, interactionHand);
	}

	@Override
	protected void addAdditionalSaveData(CompoundTag compoundTag) {
		super.addAdditionalSaveData(compoundTag);
		compoundTag.putShort("Fuel", this.fuel);
	}

	@Override
	protected void readAdditionalSaveData(CompoundTag compoundTag) {
		super.readAdditionalSaveData(compoundTag);
		this.fuel = compoundTag.getShort("Fuel");
	}

	protected boolean hasFuel() {
		return this.entityData.get(DATA_ID_FUEL);
	}

	protected void setHasFuel(boolean bl) {
		this.entityData.set(DATA_ID_FUEL, bl);
	}

	@Override
	public BlockState getDisplayBlockState() {
		return Blocks.FURNACE.defaultBlockState().setValue(FurnaceBlock.FACING, Direction.NORTH).setValue(FurnaceBlock.LIT, this.hasFuel());
	}

	@Override
	public Vec3 modifyDeltaMovement(Vec3 vec3) {
		if (hasFuel()) {
			// Dupes the speed of the boat x2
			return vec3.multiply(1.05, 0, 1.05);
		}
		return vec3;
	}

}
