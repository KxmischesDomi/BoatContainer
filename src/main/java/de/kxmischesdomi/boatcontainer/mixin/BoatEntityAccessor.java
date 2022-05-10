package de.kxmischesdomi.boatcontainer.mixin;

import net.minecraft.world.entity.vehicle.Boat;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0
 */
@Mixin(Boat.class)
public interface BoatEntityAccessor {

	@Accessor("deltaRotation")
	float getDeltaRotation();

}
