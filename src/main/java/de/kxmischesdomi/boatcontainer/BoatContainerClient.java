package de.kxmischesdomi.boatcontainer;

import de.kxmischesdomi.boatcontainer.client.renderer.BoatBlockRenderer;
import de.kxmischesdomi.boatcontainer.common.registry.ModEntities;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityRendererRegistry;
import net.minecraft.world.level.block.Blocks;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0
 */
public class BoatContainerClient implements ClientModInitializer {

	@Override
	public void onInitializeClient() {
		EntityRendererRegistry.INSTANCE.register(ModEntities.CHEST_BOAT, ctx -> new BoatBlockRenderer(ctx, Blocks.CHEST.defaultBlockState()));
		EntityRendererRegistry.INSTANCE.register(ModEntities.ENDER_CHEST_BOAT, ctx -> new BoatBlockRenderer(ctx, Blocks.ENDER_CHEST.defaultBlockState()));
	}

}
