package de.kxmischesdomi.boatcontainer.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.BoatRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.level.block.state.BlockState;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0
 */
@Environment(EnvType.CLIENT)
public abstract class AbstractBoatBlockRenderer extends BoatRenderer {

	public AbstractBoatBlockRenderer(Context context) {
		super(context, false);
	}

	public abstract BlockState getBlockState(Boat boatEntity);
	public abstract void preModify(PoseStack matrixStack, float f);
	public abstract void afterModify(PoseStack matrixStack, float f);

	public Vector3f getDamagingRotation() {
		return Vector3f.XP;
	}

	@Override
	public void render(Boat boatEntity, float f, float g, PoseStack matrixStack, MultiBufferSource vertexConsumerProvider, int i) {
		super.render(boatEntity, f, g, matrixStack, vertexConsumerProvider, i);

		BlockState blockState = getBlockState(boatEntity);
		if (blockState == null) return;
		preModify(matrixStack, f);

		matrixStack.pushPose();

		float h = (float) boatEntity.getHurtTime() - g;
		float j = boatEntity.getDamage() - g;
		if (j < 0.0F) {
			j = 0.0F;
		}
		if (h > 0.0F) {
			matrixStack.mulPose(getDamagingRotation().rotationDegrees(Mth.sin(h) * h * j / 10.0F * (float) boatEntity.getHurtDir()));
		}

		float k = boatEntity.getBubbleAngle(g);
		if (!Mth.equal(k, 0.0F)) {
			matrixStack.mulPose(new Quaternion(new Vector3f(1.0F, 0.0F, 1.0F), boatEntity.getBubbleAngle(g), true));
		}

		matrixStack.translate(-0.45, -0.2, -0.035);
		matrixStack.scale(0.9f, 0.9f, 0.9f);
		afterModify(matrixStack, f);
		Minecraft.getInstance().getBlockRenderer().renderSingleBlock(blockState, matrixStack, vertexConsumerProvider, i, OverlayTexture.NO_OVERLAY);
		matrixStack.popPose();

	}

}