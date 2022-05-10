package de.kxmischesdomi.boatcontainer.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import de.kxmischesdomi.boatcontainer.common.entity.BoatWithBlockEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0
 */
@Environment(EnvType.CLIENT)
public class BoatBlockRenderer extends AbstractBoatBlockRenderer {


	public BoatBlockRenderer(Context context) {
		super(context);
	}

	@Override
	public BlockState getBlockState(Boat boatEntity) {
		if (boatEntity instanceof BoatWithBlockEntity boatWithBlockEntity) {
			return boatWithBlockEntity.getDisplayBlockState();
		}
		return Blocks.AIR.defaultBlockState();
	}

	@Override
	public void setBlockPosition(PoseStack matrixStack, float f) {
		matrixStack.mulPose(Vector3f.YP.rotationDegrees(180 -f));
		matrixStack.translate(0.0D, 0.375D, 0.0D);
	}

}
