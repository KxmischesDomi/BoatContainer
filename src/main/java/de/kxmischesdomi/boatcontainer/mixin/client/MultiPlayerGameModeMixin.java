package de.kxmischesdomi.boatcontainer.mixin.client;

import de.kxmischesdomi.boatcontainer.common.entity.HasCustomInventoryScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.2.0
 */
@Mixin(MultiPlayerGameMode.class)
public class MultiPlayerGameModeMixin {

	@Shadow @Final private Minecraft minecraft;

	@Inject(method = "isServerControlledInventory", at = @At("HEAD"), cancellable = true)
	public void isServerControlledInventoryMixin(CallbackInfoReturnable<Boolean> cir) {
		if (this.minecraft.player.isPassenger() && this.minecraft.player.getVehicle() instanceof HasCustomInventoryScreen) {
			cir.setReturnValue(true);
		}
	}

}
