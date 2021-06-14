package dev.array21.dutchyhome.listeners;

import java.util.Map;

import org.bukkit.Location;

import dev.array21.dutchycore.annotations.EventHandler;
import dev.array21.dutchycore.module.events.ModuleEventListener;
import dev.array21.dutchyhome.DutchyHome;
import dev.array21.dutchyhome.PlayerHomes;
import dev.array21.offlineplayers.events.PlayerTransferEvent;

public class PlayerTransferEventListener implements ModuleEventListener {

	private DutchyHome module;
	
	public PlayerTransferEventListener(DutchyHome module) {
		this.module = module;
	}

	@EventHandler
	public void onPlayerTransferEvent(PlayerTransferEvent event) {
				
		PlayerHomes playerHomesOld = this.module.getPlayerHomes(event.getOldUUID());
		
		//Create a new PlayerHoems object
		PlayerHomes playerHomesNew = new PlayerHomes(event.getNewUUID());
		
		//Add all h omes from the playerHomesOld to the playerHomesNew
		for(Map.Entry<String, Location> entry : playerHomesOld.getAllHomes().entrySet()) {
			playerHomesNew.setHome(entry.getKey(), entry.getValue());
		}
		
		//Set the new PlayerHomes, and remove the old
		this.module.setPlayerHome(event.getNewUUID(), playerHomesNew);
		this.module.delPlayerHome(event.getOldUUID());
		
		//Write memory to disk
		this.module.writeMemory();
	}
}
