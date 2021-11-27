package de.kxmischesdomi.boatcontainer.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.level.block.state.BlockState;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0
 */
@Environment(EnvType.CLIENT)
public class BoatBlockRenderer extends AbstractBoatBlockRenderer {

	private final BlockState blockState;

	public BoatBlockRenderer(Context context, BlockState blockState) {
		super(context);
		this.blockState = blockState;
	}

	@Override
	public BlockState getBlockState(Boat boatEntity) {
		return blockState;
	}

	@Override
	public void modifyMatrix(PoseStack matrixStack) {

	}

}
