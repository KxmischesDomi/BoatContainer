package de.kxmischesdomi.boatcontainer.client.renderer;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.entity.EntityRendererFactory.Context;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.vehicle.BoatEntity;

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
	public BlockState getBlockState(BoatEntity boatEntity) {
		return blockState;
	}

	@Override
	public void modifyMatrix(MatrixStack matrixStack) {

	}

}
