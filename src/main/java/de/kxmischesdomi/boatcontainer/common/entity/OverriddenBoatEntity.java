package de.kxmischesdomi.boatcontainer.common.entity;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0
 */
public abstract class OverriddenBoatEntity extends BoatEntity {

	public OverriddenBoatEntity(EntityType<? extends BoatEntity> entityType, World world) {
		super(entityType, world);
	}

	public OverriddenBoatEntity(EntityType<? extends BoatEntity> entityType, World world, double x, double y, double z) {
		super(entityType, world);
		this.setPosition(x, y, z);
		this.prevX = x;
		this.prevY = y;
		this.prevZ = z;
	}

	@Override
	public void setPosition(double x, double y, double z) {
		super.setPosition(x, y, z);
		this.prevX = x;
		this.prevY = y;
		this.prevZ = z;
	}


	@Override
	public boolean damage(DamageSource source, float amount) {
		if (this.isInvulnerableTo(source)) {
			return false;
		} else if (!this.world.isClient && !this.isRemoved()) {
			this.setDamageWobbleSide(-this.getDamageWobbleSide());
			this.setDamageWobbleTicks(10);
			this.setDamageWobbleStrength(this.getDamageWobbleStrength() + amount * 10.0F);
			this.scheduleVelocityUpdate();
			this.emitGameEvent(GameEvent.ENTITY_DAMAGED, source.getAttacker());
			boolean bl = source.getAttacker() instanceof PlayerEntity && ((PlayerEntity)source.getAttacker()).getAbilities().creativeMode;
			if (bl || this.getDamageWobbleStrength() > 40.0F) {
				if (!bl && this.world.getGameRules().getBoolean(GameRules.DO_ENTITY_DROPS)) {
					this.dropItem(this.asItem());
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
	public ActionResult interact(PlayerEntity player, Hand hand) {

		if (player.isSneaking() && hand == Hand.MAIN_HAND) {
			ActionResult result = sneakInteract(player, hand);
			if (result == ActionResult.CONSUME || result == ActionResult.SUCCESS) return result;
		}

		return super.interact(player, hand);
	}

	public ActionResult sneakInteract(PlayerEntity playerEntity, Hand hand) {
		return ActionResult.PASS;
	}

}
