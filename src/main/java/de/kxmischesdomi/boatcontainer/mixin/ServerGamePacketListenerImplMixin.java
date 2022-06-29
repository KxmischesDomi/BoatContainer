package de.kxmischesdomi.boatcontainer.mixin;

import de.kxmischesdomi.boatcontainer.common.entity.HasCustomInventoryScreen;
import net.minecraft.network.protocol.game.ServerboundPlayerCommandPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.2.0
 */
@Mixin(ServerGamePacketListenerImpl.class)
public class ServerGamePacketListenerImplMixin {

	@Shadow public ServerPlayer player;

	@Inject(method = "handlePlayerCommand", at = @At(value = "INVOKE", shift = At.Shift.BEFORE, target = "Lnet/minecraft/network/protocol/game/ServerboundPlayerCommandPacket;getAction()Lnet/minecraft/network/protocol/game/ServerboundPlayerCommandPacket$Action;"), cancellable = true)
	public void handlePlayerCommandMixin(ServerboundPlayerCommandPacket serverboundPlayerCommandPacket, CallbackInfo ci) {
		System.out.println("a");
		if (serverboundPlayerCommandPacket.getAction() == ServerboundPlayerCommandPacket.Action.OPEN_INVENTORY) {
		System.out.println("b");
			if (player.getVehicle() instanceof HasCustomInventoryScreen screen) {
		System.out.println("c");
				screen.openCustomInventoryScreen(player);
				ci.cancel();
			}
		}
	}

}
