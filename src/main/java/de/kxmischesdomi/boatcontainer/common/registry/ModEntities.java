package de.kxmischesdomi.boatcontainer.common.registry;

import de.kxmischesdomi.boatcontainer.BoatContainer;
import de.kxmischesdomi.boatcontainer.common.entity.ChestBoatEntity;
import de.kxmischesdomi.boatcontainer.common.entity.EnderChestBoatEntity;
import de.kxmischesdomi.boatcontainer.common.entity.OverwrittenBoatEntity;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0
 */
public class ModEntities {

	public static EntityType<ChestBoatEntity> CHEST_BOAT = registerBoat("chest_boat", FabricEntityTypeBuilder.create(SpawnGroup.MISC, ChestBoatEntity::new));
	public static EntityType<EnderChestBoatEntity> ENDER_CHEST_BOAT = registerBoat("ender_chest_boat", FabricEntityTypeBuilder.create(SpawnGroup.MISC, EnderChestBoatEntity::new));

	private static <T extends OverwrittenBoatEntity> EntityType<T> registerBoat(String id, FabricEntityTypeBuilder<T> builder) {
		builder.dimensions(EntityDimensions.fixed(1.375F, 0.5625F)).trackRangeBlocks(10);
		return Registry.register(Registry.ENTITY_TYPE, new Identifier(BoatContainer.MOD_ID, id), builder.build());
	}

	public static void init() { }

}
