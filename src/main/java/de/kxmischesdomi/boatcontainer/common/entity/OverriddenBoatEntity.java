package de.kxmischesdomi.boatcontainer.common.entity;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.Vec3;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0
 */
public abstract class OverriddenBoatEntity extends Boat {

	public OverriddenBoatEntity(EntityType<? extends Boat> entityType, Level world) {
		super(entityType, world);
	}

	public OverriddenBoatEntity(EntityType<? extends Boat> entityType, Level world, double x, double y, double z) {
		super(entityType, world);
		this.setPos(x, y, z);
		this.xo = x;
		this.yo = y;
		this.zo = z;
	}

	@Override
	public void setPos(double x, double y, double z) {
		super.setPos(x, y, z);
		this.xo = x;
		this.yo = y;
		this.zo = z;
	}


	@Override
	public boolean hurt(DamageSource source, float amount) {
		if (this.isInvulnerableTo(source)) {
			return false;
		} else if (!this.level.isClientSide && !this.isRemoved()) {
			this.setHurtDir(-this.getHurtDir());
			this.setHurtTime(10);
			this.setDamage(this.getDamage() + amount * 10.0F);
			this.markHurt();
			this.gameEvent(GameEvent.ENTITY_DAMAGED, source.getEntity());
			boolean bl = source.getEntity() instanceof Player && ((Player)source.getEntity()).getAbilities().instabuild;
			if (bl || this.getDamage() > 40.0F) {
				if (!bl && this.level.getGameRules().getBoolean(GameRules.RULE_DOENTITYDROPS)) {
					this.spawnAtLocation(this.getDropItem());
					this.dropItems(source);
				}

				this.discard();
			}

			return true;
		} else {
			return true;
		}
	}

	public void dropItems(DamageSource source) { }

	@Override
	public InteractionResult interact(Player player, InteractionHand hand) {

		if (player.isShiftKeyDown() && hand == InteractionHand.MAIN_HAND) {
			InteractionResult result = sneakInteract(player, hand);
			if (result == InteractionResult.CONSUME || result == InteractionResult.SUCCESS) return result;
		}

		return super.interact(player, hand);
	}

	public InteractionResult sneakInteract(Player playerEntity, InteractionHand hand) {
		return InteractionResult.PASS;
	}

	public Vec3 modifyDeltaMovement(Vec3 vec3) {
		return vec3;
	}

}
