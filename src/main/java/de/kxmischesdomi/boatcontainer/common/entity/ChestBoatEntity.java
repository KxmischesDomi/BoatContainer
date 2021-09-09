package de.kxmischesdomi.boatcontainer.common.entity;

import de.kxmischesdomi.boatcontainer.common.registry.ModEntities;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.item.Items;
import net.minecraft.screen.GenericContainerScreenHandler;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.world.World;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0
 */
public class ChestBoatEntity extends StorageBoatEntity {

	public ChestBoatEntity(EntityType<? extends BoatEntity> entityType, World world) {
		super(entityType, world);
	}

	public ChestBoatEntity(EntityType<? extends BoatEntity> type, World world, double x, double y, double z) {
		super(type, world, x, y, z);
	}

	public ChestBoatEntity(World world) {
		super(ModEntities.CHEST_BOAT, world);
	}

	public int size() {
		return 27;
	}

	public ScreenHandler getScreenHandler(int syncId, PlayerEntity playerEntity, PlayerInventory playerInventory) {
		return GenericContainerScreenHandler.createGeneric9x3(syncId, playerInventory, this);
	}

	@Override
	public void dropItems(DamageSource damageSource) {
		super.dropItems(damageSource);
		this.dropItem(Items.CHEST.asItem());
	}

}
