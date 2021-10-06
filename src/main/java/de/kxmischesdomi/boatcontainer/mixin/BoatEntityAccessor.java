package de.kxmischesdomi.boatcontainer.mixin;

import net.minecraft.entity.vehicle.BoatEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.1
 */
@Mixin(BoatEntity.class)
public interface BoatEntityAccessor {

	@Accessor
	float getYawVelocity();

}
