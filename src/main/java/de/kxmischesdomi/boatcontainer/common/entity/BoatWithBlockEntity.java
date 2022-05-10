package de.kxmischesdomi.boatcontainer.common.entity;

import de.kxmischesdomi.boatcontainer.mixin.BoatEntityAccessor;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0
 */
public abstract class BoatWithBlockEntity extends OverriddenBoatEntity {

	public BoatWithBlockEntity(EntityType<? extends Boat> entityType, Level world) {
		super(entityType, world);
	}

	public BoatWithBlockEntity(EntityType<? extends Boat> entityType, Level world, double x, double y, double z) {
		super(entityType, world, x, y, z);
	}

	@Override
	protected boolean canAddPassenger(Entity passenger) {
		return this.getPassengers().size() < 1 && !this.isEyeInFluid(FluidTags.WATER);
	}

	@Override
	public void positionRider(Entity passenger) {
		if (this.hasPassenger(passenger)) {
			float f = 0.0F;
			float g = (float)((this.isRemoved() ? 0.009999999776482582D : this.getPassengersRidingOffset()) + passenger.getMyRidingOffset());

			int i = this.getPassengers().indexOf(passenger);
			if (i == 0) {
				f = 0.2F;
			} else {
				f = -0.6F;
			}

			if (passenger instanceof Animal) {
				f = (float)((double)f + 0.2D);
			}

			Vec3 vec3d = (new Vec3(f, 0.0D, 0.0D)).yRot(-this.getYRot() * 0.017453292F - 1.5707964F);
			passenger.setPos(this.getX() + vec3d.x, this.getY() + (double)g, this.getZ() + vec3d.z);
			float yawVelocity = ((BoatEntityAccessor) this).getDeltaRotation();
			passenger.setYRot(passenger.getYRot() + yawVelocity);
			passenger.setYHeadRot(passenger.getYHeadRot() + yawVelocity);
			this.clampRotation(passenger);
			if (passenger instanceof Animal) {
				int j = passenger.getId() % 2 == 0 ? 90 : 270;
				passenger.setYBodyRot(((Animal)passenger).yBodyRot + (float)j);
				passenger.setYHeadRot(passenger.getYHeadRot() + (float)j);
			}

		}
	}

	public abstract BlockState getDisplayBlockState();

}
