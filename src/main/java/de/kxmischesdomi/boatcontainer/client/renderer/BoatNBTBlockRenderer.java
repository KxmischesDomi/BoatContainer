package de.kxmischesdomi.boatcontainer.client.renderer;

import de.kxmischesdomi.boatcontainer.common.entity.BoatNBTBlockEntity;
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
public class BoatNBTBlockRenderer extends AbstractBoatBlockRenderer {

	public BoatNBTBlockRenderer(Context context) {
		super(context);
	}

	@Override
	public BlockState getBlockState(BoatEntity boatEntity) {

		if (boatEntity instanceof BoatNBTBlockEntity) {
			BoatNBTBlockEntity nbtEntity = (BoatNBTBlockEntity) boatEntity;
			return nbtEntity.getCarriedBlock();
		} else {
			return null;
		}

	}

	@Override
	public void modifyMatrix(MatrixStack matrixStack) {

	}

}
