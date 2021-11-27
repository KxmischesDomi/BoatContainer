package de.kxmischesdomi.boatcontainer.common.entity;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.*;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import org.jetbrains.annotations.Nullable;

import java.util.Iterator;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0
 */
public abstract class StorageBoatEntity extends CustomBoatEntity implements Container, MenuProvider {

	private NonNullList<ItemStack> inventory;
	private ResourceLocation lootTableId;
	private long lootSeed;

	public StorageBoatEntity(EntityType<? extends Boat> entityType, Level world) {
		super(entityType, world);
		this.inventory = NonNullList.withSize(36, ItemStack.EMPTY);
	}

	public StorageBoatEntity(EntityType<? extends Boat> entityType, Level world, double x, double y, double z) {
		super(entityType, world, x, y, z);
		this.inventory = NonNullList.withSize(36, ItemStack.EMPTY);
	}

	@Override
	public void dropItems(DamageSource damageSource) {
		Containers.dropContents(this.level, this, this);
	}

	public boolean isEmpty() {
		Iterator var1 = this.inventory.iterator();

		ItemStack itemStack;
		do {
			if (!var1.hasNext()) {
				return true;
			}

			itemStack = (ItemStack)var1.next();
		} while(itemStack.isEmpty());

		return false;
	}

	public ItemStack getItem(int slot) {
		this.generateLoot((Player)null);
		return (ItemStack)this.inventory.get(slot);
	}

	public ItemStack removeItem(int slot, int amount) {
		this.generateLoot((Player)null);
		return ContainerHelper.removeItem(this.inventory, slot, amount);
	}

	public ItemStack removeItemNoUpdate(int slot) {
		this.generateLoot((Player)null);
		ItemStack itemStack = (ItemStack)this.inventory.get(slot);
		if (itemStack.isEmpty()) {
			return ItemStack.EMPTY;
		} else {
			this.inventory.set(slot, ItemStack.EMPTY);
			return itemStack;
		}
	}

	public void setItem(int slot, ItemStack stack) {
		this.generateLoot((Player)null);
		this.inventory.set(slot, stack);
		if (!stack.isEmpty() && stack.getCount() > this.getMaxStackSize()) {
			stack.setCount(this.getMaxStackSize());
		}

	}

	public SlotAccess getSlot(final int mappedIndex) {
		return mappedIndex >= 0 && mappedIndex < this.getContainerSize() ? new SlotAccess() {
			public ItemStack get() {
				return StorageBoatEntity.this.getItem(mappedIndex);
			}

			public boolean set(ItemStack stack) {
				StorageBoatEntity.this.setItem(mappedIndex, stack);
				return true;
			}
		} : super.getSlot(mappedIndex);
	}

	public void setChanged() {
	}

	public boolean stillValid(Player player) {
		if (this.isRemoved()) {
			return false;
		} else {
			return !(player.distanceToSqr(this) > 64.0D);
		}
	}

	public void remove(Entity.RemovalReason reason) {
		if (!this.level.isClientSide && reason.shouldDestroy()) {
			Containers.dropContents(this.level, (Entity)this, (Container)this);
		}

		super.remove(reason);
	}

	protected void addAdditionalSaveData(CompoundTag nbt) {
		super.addAdditionalSaveData(nbt);
		if (this.lootTableId != null) {
			nbt.putString("LootTable", this.lootTableId.toString());
			if (this.lootSeed != 0L) {
				nbt.putLong("LootTableSeed", this.lootSeed);
			}
		} else {
			ContainerHelper.saveAllItems(nbt, this.inventory);
		}

	}

	protected void readAdditionalSaveData(CompoundTag nbt) {
		super.readAdditionalSaveData(nbt);
		this.inventory = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
		if (nbt.contains("LootTable", 8)) {
			this.lootTableId = new ResourceLocation(nbt.getString("LootTable"));
			this.lootSeed = nbt.getLong("LootTableSeed");
		} else {
			ContainerHelper.loadAllItems(nbt, this.inventory);
		}

	}

	@Override
	public InteractionResult sneakInteract(Player player, InteractionHand hand) {
		player.openMenu(this);
		if (!player.level.isClientSide) {
			this.gameEvent(GameEvent.CONTAINER_OPEN, player);
			return InteractionResult.CONSUME;
		} else {
			return InteractionResult.SUCCESS;
		}
	}

	public void generateLoot(@Nullable Player player) {
		if (this.lootTableId != null && this.level.getServer() != null) {
			LootTable lootTable = this.level.getServer().getLootTables().get(this.lootTableId);
			if (player instanceof ServerPlayer) {
				CriteriaTriggers.GENERATE_LOOT.trigger((ServerPlayer)player, this.lootTableId);
			}

			this.lootTableId = null;
			LootContext.Builder builder = (new LootContext.Builder((ServerLevel)this.level)).withParameter(LootContextParams.ORIGIN, this.position()).withOptionalRandomSeed(this.lootSeed);
			if (player != null) {
				builder.withLuck(player.getLuck()).withParameter(LootContextParams.THIS_ENTITY, player);
			}

			lootTable.fill(this, builder.create(LootContextParamSets.CHEST));
		}

	}

	public void clearContent() {
		this.generateLoot(null);
		this.inventory.clear();
	}

	public void setLootTable(ResourceLocation id, long lootSeed) {
		this.lootTableId = id;
		this.lootSeed = lootSeed;
	}

	@Nullable
	public AbstractContainerMenu createMenu(int i, Inventory playerInventory, Player playerEntity) {
		if (this.lootTableId != null && playerEntity.isSpectator()) {
			return null;
		} else {
			this.generateLoot(playerInventory.player);
			return this.getScreenHandler(i, playerEntity, playerInventory);
		}
	}

	protected abstract AbstractContainerMenu getScreenHandler(int syncId, Player playerEntity, Inventory playerInventory);

}
