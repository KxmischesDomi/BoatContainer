package de.kxmischesdomi.boatcontainer.common.registry;

import de.kxmischesdomi.boatcontainer.BoatContainer;
import de.kxmischesdomi.boatcontainer.common.entity.ChestBoatEntity;
import de.kxmischesdomi.boatcontainer.common.entity.EnderChestBoatEntity;
import de.kxmischesdomi.boatcontainer.common.entity.FurnaceBoatEntity;
import de.kxmischesdomi.boatcontainer.common.entity.OverriddenBoatEntity;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0
 */
public class ModEntities {

	public static EntityType<ChestBoatEntity> CHEST_BOAT = registerBoat("chest_boat", FabricEntityTypeBuilder.create(MobCategory.MISC, ChestBoatEntity::new));
	public static EntityType<EnderChestBoatEntity> ENDER_CHEST_BOAT = registerBoat("ender_chest_boat", FabricEntityTypeBuilder.create(MobCategory.MISC, EnderChestBoatEntity::new));
	public static EntityType<FurnaceBoatEntity> FURNACE_BOAT = registerBoat("furnace_boat", FabricEntityTypeBuilder.create(MobCategory.MISC, FurnaceBoatEntity::new));

	private static <T extends OverriddenBoatEntity> EntityType<T> registerBoat(String id, FabricEntityTypeBuilder<T> builder) {
		builder.dimensions(EntityDimensions.fixed(1.375F, 0.5625F)).trackRangeBlocks(10);
		return Registry.register(Registry.ENTITY_TYPE, new ResourceLocation(BoatContainer.MOD_ID, id), builder.build());
	}

	public static void init() { }

}
