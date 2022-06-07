package de.kxmischesdomi.boatcontainer.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0.5
 */
@Environment(EnvType.CLIENT)
public class FurnaceBoatBlockRenderer extends BoatBlockRenderer {

	public FurnaceBoatBlockRenderer(Context context) {
		super(context);
	}

	@Override
	public void preModify(PoseStack matrixStack, float f) {
		matrixStack.mulPose(Vector3f.YP.rotationDegrees(-f));
	}

	@Override
	public void afterModify(PoseStack matrixStack, float f) {
		matrixStack.translate(0, 0.375D, -0.75D);
		matrixStack.scale(0.9f, 0.9f, 0.9f);
	}

	@Override
	public Vector3f getDamagingRotation() {
		return Vector3f.XN;
	}

}
