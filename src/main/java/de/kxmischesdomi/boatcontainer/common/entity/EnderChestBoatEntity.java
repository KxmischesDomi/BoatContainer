package de.kxmischesdomi.boatcontainer.common.entity;

import de.kxmischesdomi.boatcontainer.common.registry.ModEntities;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.inventory.EnderChestInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.context.LootContext.Builder;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.loot.context.LootContextTypes;
import net.minecraft.screen.GenericContainerScreenHandler;
import net.minecraft.screen.SimpleNamedScreenHandlerFactory;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

import java.util.List;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0
 */
public class EnderChestBoatEntity extends CustomBoatEntity {

	public EnderChestBoatEntity(EntityType<? extends BoatEntity> entityType, World world) {
		super(entityType, world);
	}

	public EnderChestBoatEntity(EntityType<? extends BoatEntity> type, World world, double x, double y, double z) {
		super(type, world, x, y, z);
	}

	public EnderChestBoatEntity(World world) {
		super(ModEntities.ENDER_CHEST_BOAT, world);
	}

	@Override
	public ActionResult sneakInteract(PlayerEntity playerEntity, Hand hand) {
		EnderChestInventory enderChestInventory = playerEntity.getEnderChestInventory();
		playerEntity.openHandledScreen(new SimpleNamedScreenHandlerFactory((syncId, inventory, playerx) -> {
			return GenericContainerScreenHandler.createGeneric9x3(syncId, inventory, enderChestInventory);
		}, new TranslatableText(this.getType().getTranslationKey())));
		return ActionResult.SUCCESS;
	}

	@Override
	public void dropItems(DamageSource source) {
		super.dropItems(source);
		if (world.getServer() == null) return;
		LootTable lootTable = world.getServer().getLootManager().getTable(new Identifier("blocks/ender_chest"));

		Builder builder = new Builder((ServerWorld) world)
				.parameter(LootContextParameters.ORIGIN, getPos())
				.optionalParameter(LootContextParameters.THIS_ENTITY, this)
				.random(this.random);
		List<ItemStack> itemStacks = lootTable.generateLoot(builder.build(LootContextTypes.GIFT));

		for (ItemStack itemStack : itemStacks) {
			dropStack(itemStack);
		}

	}

}
