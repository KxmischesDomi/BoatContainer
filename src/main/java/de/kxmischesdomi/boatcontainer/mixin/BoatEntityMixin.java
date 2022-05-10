package de.kxmischesdomi.boatcontainer.mixin;

import de.kxmischesdomi.boatcontainer.common.entity.OverriddenBoatEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0.5
 */
@Mixin(Boat.class)
public abstract class BoatEntityMixin extends Entity {

	public BoatEntityMixin(EntityType<?> entityType, Level level) {
		super(entityType, level);
	}

	@Inject(method = "controlBoat", at = @At(value = "TAIL"))
	public void controlBoatMixin(CallbackInfo ci) {
		if (((Object) this) instanceof OverriddenBoatEntity overriddenBoatEntity && isInWater()) {
			setDeltaMovement(overriddenBoatEntity.modifyDeltaMovement(getDeltaMovement()));
		}
	}

}
