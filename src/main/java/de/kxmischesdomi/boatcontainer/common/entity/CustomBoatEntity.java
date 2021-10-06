package de.kxmischesdomi.boatcontainer.common.entity;

import de.kxmischesdomi.boatcontainer.mixin.BoatEntityAccessor;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.tag.FluidTags;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0
 */
public abstract class CustomBoatEntity extends OverwrittenBoatEntity {

	public CustomBoatEntity(EntityType<? extends BoatEntity> entityType, World world) {
		super(entityType, world);
	}

	public CustomBoatEntity(EntityType<? extends BoatEntity> entityType, World world, double x, double y, double z) {
		super(entityType, world, x, y, z);
	}

	@Override
	protected boolean canAddPassenger(Entity passenger) {
		return this.getPassengerList().size() < 1 && !this.isSubmergedIn(FluidTags.WATER);
	}

	@Override
	public void updatePassengerPosition(Entity passenger) {
		if (this.hasPassenger(passenger)) {
			float f = 0.0F;
			float g = (float)((this.isRemoved() ? 0.009999999776482582D : this.getMountedHeightOffset()) + passenger.getHeightOffset());

			int i = this.getPassengerList().indexOf(passenger);
			if (i == 0) {
				f = 0.2F;
			} else {
				f = -0.6F;
			}

			if (passenger instanceof AnimalEntity) {
				f = (float)((double)f + 0.2D);
			}

			Vec3d vec3d = (new Vec3d(f, 0.0D, 0.0D)).rotateY(-this.getYaw() * 0.017453292F - 1.5707964F);
			passenger.setPosition(this.getX() + vec3d.x, this.getY() + (double)g, this.getZ() + vec3d.z);
			float yawVelocity = ((BoatEntityAccessor) this).getYawVelocity();
			passenger.setYaw(passenger.getYaw() + yawVelocity);
			passenger.setHeadYaw(passenger.getHeadYaw() + yawVelocity);
			this.copyEntityData(passenger);
			if (passenger instanceof AnimalEntity) {
				int j = passenger.getId() % 2 == 0 ? 90 : 270;
				passenger.setBodyYaw(((AnimalEntity)passenger).bodyYaw + (float)j);
				passenger.setHeadYaw(passenger.getHeadYaw() + (float)j);
			}

		}
	}

}
