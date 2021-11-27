package de.kxmischesdomi.boatcontainer.common.registry;

import com.mojang.datafixers.util.Function5;
import de.kxmischesdomi.boatcontainer.BoatContainer;
import de.kxmischesdomi.boatcontainer.common.entity.ChestBoatEntity;
import de.kxmischesdomi.boatcontainer.common.entity.EnderChestBoatEntity;
import de.kxmischesdomi.boatcontainer.common.entity.OverriddenBoatEntity;
import de.kxmischesdomi.boatcontainer.common.item.CustomBoatItem;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.vehicle.Boat.Type;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Item.Properties;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.List;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0
 */
public class ModItems {

	public static CustomBoatItem[] CHEST_BOAT = registerBoat("chest_boat", ModEntities.CHEST_BOAT, ChestBoatEntity::new);
	public static CustomBoatItem[] ENDER_CHEST_BOAT = registerBoat("ender_chest_boat", ModEntities.ENDER_CHEST_BOAT, EnderChestBoatEntity::new);

	public static void init() {}

	public static CustomBoatItem[] registerBoat(String name, EntityType<? extends OverriddenBoatEntity> type, Function5<EntityType<? extends OverriddenBoatEntity>, Level, Double, Double, Double, ? extends OverriddenBoatEntity> instanceCreator) {

		List<CustomBoatItem> list = new ArrayList<>();

		for (Type value : Type.values()) {
			CustomBoatItem item = register(value.getName() + "_" + name, new CustomBoatItem(type, instanceCreator, value, new Properties().stacksTo(1).tab(CreativeModeTab.TAB_TRANSPORTATION)));
			list.add(item);
		}

		return list.toArray(new CustomBoatItem[0]);
	}

	public static <T extends Item> T register(String name, T item) {
		Registry.register(Registry.ITEM, new ResourceLocation(BoatContainer.MOD_ID, name), item);
		return item;
	}

}
