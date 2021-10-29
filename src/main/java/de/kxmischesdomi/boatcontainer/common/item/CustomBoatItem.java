package de.kxmischesdomi.boatcontainer.common.item;

import com.mojang.datafixers.util.Function5;
import de.kxmischesdomi.boatcontainer.common.entity.OverriddenBoatEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;

import java.util.Iterator;
import java.util.List;
import java.util.function.Predicate;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0
 */
public class CustomBoatItem extends Item {

	private static final Predicate<Entity> RIDERS;
	private final BoatEntity.Type type;
	private final EntityType<? extends OverriddenBoatEntity> entityType;
	private final Function5<EntityType<? extends OverriddenBoatEntity>, World, Double, Double, Double, ? extends OverriddenBoatEntity> instanceCreator;

	public CustomBoatItem(EntityType<? extends OverriddenBoatEntity> entityType, Function5<EntityType<? extends OverriddenBoatEntity>, World, Double, Double, Double, ? extends OverriddenBoatEntity> instanceCreator, BoatEntity.Type type, Settings settings) {
		super(settings);
		this.entityType = entityType;
		this.type = type;
		this.instanceCreator = instanceCreator;
	}

	public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
		ItemStack itemStack = user.getStackInHand(hand);
		HitResult hitResult = raycast(world, user, RaycastContext.FluidHandling.ANY);
		if (hitResult.getType() == HitResult.Type.MISS) {
			return TypedActionResult.pass(itemStack);
		} else {
			Vec3d vec3d = user.getRotationVec(1.0F);
			double d = 5.0D;
			List<Entity> list = world.getOtherEntities(user, user.getBoundingBox().stretch(vec3d.multiply(5.0D)).expand(1.0D), RIDERS);
			if (!list.isEmpty()) {
				Vec3d vec3d2 = user.getEyePos();
				Iterator var11 = list.iterator();

				while(var11.hasNext()) {
					Entity entity = (Entity)var11.next();
					Box box = entity.getBoundingBox().expand((double)entity.getTargetingMargin());
					if (box.contains(vec3d2)) {
						return TypedActionResult.pass(itemStack);
					}
				}
			}

			if (hitResult.getType() == HitResult.Type.BLOCK) {

				OverriddenBoatEntity boatEntity = instanceCreator.apply(entityType, world, hitResult.getPos().x, hitResult.getPos().y, hitResult.getPos().z);
				boatEntity.setBoatType(this.type);
				boatEntity.setYaw(user.getYaw());

				if (!world.isSpaceEmpty(boatEntity, boatEntity.getBoundingBox().expand(-0.1D))) {
					return TypedActionResult.fail(itemStack);
				} else {
					if (!world.isClient) {
						modifyBoat(boatEntity, itemStack);
						world.spawnEntity(boatEntity);
						world.emitGameEvent(user, GameEvent.ENTITY_PLACE, new BlockPos(hitResult.getPos()));
						if (!user.getAbilities().creativeMode) {
							itemStack.decrement(1);
						}
					}

					user.incrementStat(Stats.USED.getOrCreateStat(this));
					return TypedActionResult.success(itemStack, world.isClient());
				}


			} else {
				return TypedActionResult.pass(itemStack);
			}
		}
	}

	protected void modifyBoat(BoatEntity boatEntity, ItemStack itemStack) {

	}

	static {
		RIDERS = EntityPredicates.EXCEPT_SPECTATOR.and(Entity::collides);
	}

}
