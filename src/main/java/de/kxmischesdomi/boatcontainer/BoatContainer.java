package de.kxmischesdomi.boatcontainer;

import de.kxmischesdomi.boatcontainer.common.registry.ModEntities;
import de.kxmischesdomi.boatcontainer.common.registry.ModItems;
import net.fabricmc.api.ModInitializer;

public class BoatContainer implements ModInitializer {

	public static final String MOD_ID = "boatcontainer";

	@Override
	public void onInitialize() {
		ModItems.init();
		ModEntities.init();
	}

}
