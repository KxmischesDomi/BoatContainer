package de.kxmischesdomi.boatcontainer.common.item;

import com.mojang.datafixers.util.Function5;
import de.kxmischesdomi.boatcontainer.common.entity.OverriddenBoatEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import java.util.Iterator;
import java.util.List;
import java.util.function.Predicate;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0
 */
public class CustomBoatItem extends Item {

	private static final Predicate<Entity> RIDERS;
	private final Boat.Type type;
	private final EntityType<? extends OverriddenBoatEntity> entityType;
	private final Function5<EntityType<? extends OverriddenBoatEntity>, Level, Double, Double, Double, ? extends OverriddenBoatEntity> instanceCreator;

	public CustomBoatItem(EntityType<? extends OverriddenBoatEntity> entityType, Function5<EntityType<? extends OverriddenBoatEntity>, Level, Double, Double, Double, ? extends OverriddenBoatEntity> instanceCreator, Boat.Type type, Properties settings) {
		super(settings);
		this.entityType = entityType;
		this.type = type;
		this.instanceCreator = instanceCreator;
	}

	public InteractionResultHolder<ItemStack> use(Level world, Player user, InteractionHand hand) {
		ItemStack itemStack = user.getItemInHand(hand);
		HitResult hitResult = getPlayerPOVHitResult(world, user, ClipContext.Fluid.ANY);
		if (hitResult.getType() == HitResult.Type.MISS) {
			return InteractionResultHolder.pass(itemStack);
		} else {
			Vec3 vec3d = user.getViewVector(1.0F);
			double d = 5.0D;
			List<Entity> list = world.getEntities(user, user.getBoundingBox().expandTowards(vec3d.scale(5.0D)).inflate(1.0D), RIDERS);
			if (!list.isEmpty()) {
				Vec3 vec3d2 = user.getEyePosition();
				Iterator var11 = list.iterator();

				while(var11.hasNext()) {
					Entity entity = (Entity)var11.next();
					AABB box = entity.getBoundingBox().inflate((double)entity.getPickRadius());
					if (box.contains(vec3d2)) {
						return InteractionResultHolder.pass(itemStack);
					}
				}
			}

			if (hitResult.getType() == HitResult.Type.BLOCK) {

				OverriddenBoatEntity boatEntity = instanceCreator.apply(entityType, world, hitResult.getLocation().x, hitResult.getLocation().y, hitResult.getLocation().z);
				boatEntity.setType(this.type);
				boatEntity.setYRot(user.getYRot());

				if (!world.noCollision(boatEntity, boatEntity.getBoundingBox().inflate(-0.1D))) {
					return InteractionResultHolder.fail(itemStack);
				} else {
					if (!world.isClientSide) {
						modifyBoat(boatEntity, itemStack);
						world.addFreshEntity(boatEntity);
						world.gameEvent(user, GameEvent.ENTITY_PLACE, new BlockPos(hitResult.getLocation()));
						if (!user.getAbilities().instabuild) {
							itemStack.shrink(1);
						}
					}

					user.awardStat(Stats.ITEM_USED.get(this));
					return InteractionResultHolder.sidedSuccess(itemStack, world.isClientSide());
				}


			} else {
				return InteractionResultHolder.pass(itemStack);
			}
		}
	}

	protected void modifyBoat(Boat boatEntity, ItemStack itemStack) {

	}

	static {
		RIDERS = EntitySelector.NO_SPECTATORS.and(Entity::isPickable);
	}

}
