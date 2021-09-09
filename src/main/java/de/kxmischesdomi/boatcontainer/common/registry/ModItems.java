package de.kxmischesdomi.boatcontainer.common.registry;

import com.mojang.datafixers.util.Function5;
import de.kxmischesdomi.boatcontainer.BoatContainer;
import de.kxmischesdomi.boatcontainer.common.entity.ChestBoatEntity;
import de.kxmischesdomi.boatcontainer.common.entity.EnderChestBoatEntity;
import de.kxmischesdomi.boatcontainer.common.entity.OverwrittenBoatEntity;
import de.kxmischesdomi.boatcontainer.common.item.CustomBoatItem;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.vehicle.BoatEntity.Type;
import net.minecraft.item.Item;
import net.minecraft.item.Item.Settings;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;

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

	public static CustomBoatItem[] registerBoat(String name, EntityType<? extends OverwrittenBoatEntity> type, Function5<EntityType<? extends OverwrittenBoatEntity>, World, Double, Double, Double, ? extends OverwrittenBoatEntity> instanceCreator) {

		List<CustomBoatItem> list = new ArrayList<>();

		for (Type value : Type.values()) {
			CustomBoatItem item = register(value.getName() + "_" + name, new CustomBoatItem(type, instanceCreator, value, new Settings().maxCount(1).group(ItemGroup.TRANSPORTATION)));
			list.add(item);
		}

		return list.toArray(new CustomBoatItem[0]);
	}

	public static <T extends Item> T register(String name, T item) {
		Registry.register(Registry.ITEM, new Identifier(BoatContainer.MOD_ID, name), item);
		return item;
	}

}
