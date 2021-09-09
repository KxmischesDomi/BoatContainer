package de.kxmischesdomi.boatcontainer.common.entity;

import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.StackReference;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.loot.context.LootContextTypes;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import org.jetbrains.annotations.Nullable;

import java.util.Iterator;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0
 */
public abstract class StorageBoatEntity extends CustomBoatEntity implements Inventory, NamedScreenHandlerFactory {

	private DefaultedList<ItemStack> inventory;
	private Identifier lootTableId;
	private long lootSeed;

	public StorageBoatEntity(EntityType<? extends BoatEntity> entityType, World world) {
		super(entityType, world);
		this.inventory = DefaultedList.ofSize(36, ItemStack.EMPTY);
	}

	public StorageBoatEntity(EntityType<? extends BoatEntity> entityType, World world, double x, double y, double z) {
		super(entityType, world, x, y, z);
		this.inventory = DefaultedList.ofSize(36, ItemStack.EMPTY);
	}

	@Override
	public void dropItems(DamageSource damageSource) {
		ItemScatterer.spawn(this.world, this, this);
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

	public ItemStack getStack(int slot) {
		this.generateLoot((PlayerEntity)null);
		return (ItemStack)this.inventory.get(slot);
	}

	public ItemStack removeStack(int slot, int amount) {
		this.generateLoot((PlayerEntity)null);
		return Inventories.splitStack(this.inventory, slot, amount);
	}

	public ItemStack removeStack(int slot) {
		this.generateLoot((PlayerEntity)null);
		ItemStack itemStack = (ItemStack)this.inventory.get(slot);
		if (itemStack.isEmpty()) {
			return ItemStack.EMPTY;
		} else {
			this.inventory.set(slot, ItemStack.EMPTY);
			return itemStack;
		}
	}

	public void setStack(int slot, ItemStack stack) {
		this.generateLoot((PlayerEntity)null);
		this.inventory.set(slot, stack);
		if (!stack.isEmpty() && stack.getCount() > this.getMaxCountPerStack()) {
			stack.setCount(this.getMaxCountPerStack());
		}

	}

	public StackReference getStackReference(final int mappedIndex) {
		return mappedIndex >= 0 && mappedIndex < this.size() ? new StackReference() {
			public ItemStack get() {
				return StorageBoatEntity.this.getStack(mappedIndex);
			}

			public boolean set(ItemStack stack) {
				StorageBoatEntity.this.setStack(mappedIndex, stack);
				return true;
			}
		} : super.getStackReference(mappedIndex);
	}

	public void markDirty() {
	}

	public boolean canPlayerUse(PlayerEntity player) {
		if (this.isRemoved()) {
			return false;
		} else {
			return !(player.squaredDistanceTo(this) > 64.0D);
		}
	}

	public void remove(Entity.RemovalReason reason) {
		if (!this.world.isClient && reason.shouldDestroy()) {
			ItemScatterer.spawn(this.world, (Entity)this, (Inventory)this);
		}

		super.remove(reason);
	}

	protected void writeCustomDataToNbt(NbtCompound nbt) {
		super.writeCustomDataToNbt(nbt);
		if (this.lootTableId != null) {
			nbt.putString("LootTable", this.lootTableId.toString());
			if (this.lootSeed != 0L) {
				nbt.putLong("LootTableSeed", this.lootSeed);
			}
		} else {
			Inventories.writeNbt(nbt, this.inventory);
		}

	}

	protected void readCustomDataFromNbt(NbtCompound nbt) {
		super.readCustomDataFromNbt(nbt);
		this.inventory = DefaultedList.ofSize(this.size(), ItemStack.EMPTY);
		if (nbt.contains("LootTable", 8)) {
			this.lootTableId = new Identifier(nbt.getString("LootTable"));
			this.lootSeed = nbt.getLong("LootTableSeed");
		} else {
			Inventories.readNbt(nbt, this.inventory);
		}

	}

	@Override
	public ActionResult sneakInteract(PlayerEntity player, Hand hand) {
		player.openHandledScreen(this);
		if (!player.world.isClient) {
			this.emitGameEvent(GameEvent.CONTAINER_OPEN, player);
			return ActionResult.CONSUME;
		} else {
			return ActionResult.SUCCESS;
		}
	}

	public void generateLoot(@Nullable PlayerEntity player) {
		if (this.lootTableId != null && this.world.getServer() != null) {
			LootTable lootTable = this.world.getServer().getLootManager().getTable(this.lootTableId);
			if (player instanceof ServerPlayerEntity) {
				Criteria.PLAYER_GENERATES_CONTAINER_LOOT.trigger((ServerPlayerEntity)player, this.lootTableId);
			}

			this.lootTableId = null;
			LootContext.Builder builder = (new LootContext.Builder((ServerWorld)this.world)).parameter(LootContextParameters.ORIGIN, this.getPos()).random(this.lootSeed);
			if (player != null) {
				builder.luck(player.getLuck()).parameter(LootContextParameters.THIS_ENTITY, player);
			}

			lootTable.supplyInventory(this, builder.build(LootContextTypes.CHEST));
		}

	}

	public void clear() {
		this.generateLoot(null);
		this.inventory.clear();
	}

	public void setLootTable(Identifier id, long lootSeed) {
		this.lootTableId = id;
		this.lootSeed = lootSeed;
	}

	@Nullable
	public ScreenHandler createMenu(int i, PlayerInventory playerInventory, PlayerEntity playerEntity) {
		if (this.lootTableId != null && playerEntity.isSpectator()) {
			return null;
		} else {
			this.generateLoot(playerInventory.player);
			return this.getScreenHandler(i, playerEntity, playerInventory);
		}
	}

	protected abstract ScreenHandler getScreenHandler(int syncId, PlayerEntity playerEntity, PlayerInventory playerInventory);

}
