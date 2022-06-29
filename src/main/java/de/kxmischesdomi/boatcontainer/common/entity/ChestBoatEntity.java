package de.kxmischesdomi.boatcontainer.common.entity;

import de.kxmischesdomi.boatcontainer.common.registry.ModEntities;
import de.kxmischesdomi.boatcontainer.common.registry.ModItems;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0
 */
public class ChestBoatEntity extends StorageBoatEntity {

	public ChestBoatEntity(EntityType<? extends Boat> entityType, Level world) {
		super(entityType, world);
	}

	public ChestBoatEntity(EntityType<? extends Boat> type, Level world, double x, double y, double z) {
		super(type, world, x, y, z);
	}

	public ChestBoatEntity(Level world) {
		super(ModEntities.CHEST_BOAT, world);
	}

	public int getContainerSize() {
		return 27;
	}

	public AbstractContainerMenu getScreenHandler(int syncId, Player playerEntity, Inventory playerInventory) {
		return ChestMenu.threeRows(syncId, playerInventory, this);
	}

	@Override
	public void dropItems(DamageSource damageSource) {
		super.dropItems(damageSource);
		if (!damageSource.isExplosion()) {
			this.spawnAtLocation(Items.CHEST.asItem());
		}
	}

	@Override
	public ItemStack getPickResult() {
		int ordinal = getBoatType().ordinal();
		if (ModItems.CHEST_BOAT.length > ordinal) {
			return new ItemStack(ModItems.CHEST_BOAT[ordinal]);
		}
		return super.getPickResult();
	}

	@Override
	public BlockState getDisplayBlockState() {
		return Blocks.CHEST.defaultBlockState();
	}

}
