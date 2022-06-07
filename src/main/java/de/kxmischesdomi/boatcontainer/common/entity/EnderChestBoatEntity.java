package de.kxmischesdomi.boatcontainer.common.entity;

import de.kxmischesdomi.boatcontainer.common.registry.ModEntities;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.inventory.PlayerEnderChestContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootContext.Builder;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;

import java.util.List;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0
 */
public class EnderChestBoatEntity extends BoatWithBlockEntity {

	public EnderChestBoatEntity(EntityType<? extends Boat> entityType, Level world) {
		super(entityType, world);
	}

	public EnderChestBoatEntity(EntityType<? extends Boat> type, Level world, double x, double y, double z) {
		super(type, world, x, y, z);
	}

	public EnderChestBoatEntity(Level world) {
		super(ModEntities.ENDER_CHEST_BOAT, world);
	}

	@Override
	public InteractionResult sneakInteract(Player playerEntity, InteractionHand hand) {
		PlayerEnderChestContainer enderChestInventory = playerEntity.getEnderChestInventory();
		playerEntity.openMenu(new SimpleMenuProvider((syncId, inventory, playerx) -> {
			return ChestMenu.threeRows(syncId, inventory, enderChestInventory);
		}, MutableComponent.create(new TranslatableContents(this.getType().getDescriptionId()))));
		return InteractionResult.SUCCESS;
	}

	@Override
	public void dropItems(DamageSource source) {
		super.dropItems(source);
		if (level.getServer() == null) return;
		LootTable lootTable = level.getServer().getLootTables().get(new ResourceLocation("blocks/ender_chest"));

		Player killer = null;
		ItemStack tool = ItemStack.EMPTY;
		if (source.getEntity() != null && source.getEntity() instanceof Player) {
			killer = ((Player) source.getEntity());
			tool = killer.getMainHandItem() == ItemStack.EMPTY ? killer.getOffhandItem() : killer.getMainHandItem();
		}

		Builder builder = new Builder((ServerLevel) level)
				.withParameter(LootContextParams.ORIGIN, position())
				.withParameter(LootContextParams.BLOCK_STATE, Blocks.ENDER_CHEST.defaultBlockState())
				.withParameter(LootContextParams.TOOL, tool)
				.withOptionalParameter(LootContextParams.THIS_ENTITY, killer)
				.withRandom(this.random);
		List<ItemStack> itemStacks = lootTable.getRandomItems(builder.create(LootContextParamSets.BLOCK));

		for (ItemStack itemStack : itemStacks) {
			spawnAtLocation(itemStack);
		}

	}

	@Override
	public BlockState getDisplayBlockState() {
		return Blocks.ENDER_CHEST.defaultBlockState();
	}

}
