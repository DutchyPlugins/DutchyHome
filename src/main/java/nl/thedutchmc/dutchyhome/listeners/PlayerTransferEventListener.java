package nl.thedutchmc.dutchyhome.listeners;

import java.util.Map;

import org.bukkit.Location;

import nl.thedutchmc.dutchycore.module.events.ModuleEvent;
import nl.thedutchmc.dutchycore.module.events.ModuleEventListener;
import nl.thedutchmc.dutchyhome.DutchyHome;
import nl.thedutchmc.dutchyhome.PlayerHomes;
import nl.thedutchmc.offlineplayers.events.PlayerTransferEvent;

public class PlayerTransferEventListener implements ModuleEventListener {

	private DutchyHome module;
	
	public PlayerTransferEventListener(DutchyHome module) {
		this.module = module;
	}

	@Override
	public void onEvent(ModuleEvent event) {
		PlayerTransferEvent pte = (PlayerTransferEvent) event;
				
		PlayerHomes playerHomesOld = this.module.getPlayerHomes(pte.getOldUUID());
		
		//Create a new PlayerHoems object
		PlayerHomes playerHomesNew = new PlayerHomes(pte.getNewUUID());
		
		//Add all h omes from the playerHomesOld to the playerHomesNew
		for(Map.Entry<String, Location> entry : playerHomesOld.getAllHomes().entrySet()) {
			playerHomesNew.setHome(entry.getKey(), entry.getValue());
		}
		
		//Set the new PlayerHomes, and remove the old
		this.module.setPlayerHome(pte.getNewUUID(), playerHomesNew);
		this.module.delPlayerHome(pte.getOldUUID());
		
		//Write memory to disk
		this.module.writeMemory();
	}
}
