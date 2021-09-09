package de.kxmischesdomi.boatcontainer.client.renderer;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.BoatEntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory.Context;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Quaternion;
import net.minecraft.util.math.Vec3f;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0
 */
@Environment(EnvType.CLIENT)
public abstract class AbstractBoatBlockRenderer extends BoatEntityRenderer {

	public AbstractBoatBlockRenderer(Context context) {
		super(context);
	}

	public abstract BlockState getBlockState(BoatEntity boatEntity);
	public abstract void modifyMatrix(MatrixStack matrixStack);

	@Override
	public void render(BoatEntity boatEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {
		super.render(boatEntity, f, g, matrixStack, vertexConsumerProvider, i);

		BlockState blockState = getBlockState(boatEntity);
		if (blockState == null) return;

		matrixStack.push();
		matrixStack.translate(0.0D, 0.375D, 0.0D);
		matrixStack.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(180.0F - f));

		float h = (float) boatEntity.getDamageWobbleTicks() - g;
		float j = boatEntity.getDamageWobbleStrength() - g;
		if (j < 0.0F) {
			j = 0.0F;
		}
		if (h > 0.0F) {
			matrixStack.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(MathHelper.sin(h) * h * j / 10.0F * (float) boatEntity.getDamageWobbleSide()));
		}

		float k = boatEntity.interpolateBubbleWobble(g);
		if (!MathHelper.approximatelyEquals(k, 0.0F)) {
			matrixStack.multiply(new Quaternion(new Vec3f(1.0F, 0.0F, 1.0F), boatEntity.interpolateBubbleWobble(g), true));
		}

		matrixStack.translate(-0.45, -0.2, -0.035);
		matrixStack.scale(0.9f, 0.9f, 0.9f);
		modifyMatrix(matrixStack);
		MinecraftClient.getInstance().getBlockRenderManager().renderBlockAsEntity(blockState, matrixStack, vertexConsumerProvider, i, OverlayTexture.DEFAULT_UV);
		matrixStack.pop();

	}

}