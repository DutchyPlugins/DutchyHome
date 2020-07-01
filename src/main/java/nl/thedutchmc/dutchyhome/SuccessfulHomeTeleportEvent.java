package nl.thedutchmc.dutchyhome;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class SuccessfulHomeTeleportEvent extends Event implements Cancellable {

    private static final HandlerList HANDLERS_LIST = new HandlerList();
	private boolean isCancelled = false;
    
	private Player player;
	private Location locationBeforeHomeTp;
	
	public SuccessfulHomeTeleportEvent(Player player, Location locationBeforeHomeTp) {
		this.player = player;
		this.locationBeforeHomeTp = locationBeforeHomeTp;
	}
    
	@Override
	public boolean isCancelled() {
		return isCancelled;
	}

	@Override
	public void setCancelled(boolean isCancelled) {
		this.isCancelled = isCancelled;
	}

	@Override
	public HandlerList getHandlers() {
		return HANDLERS_LIST;
	}
	
    public static HandlerList getHandlerList() {
        return HANDLERS_LIST;
    }
    
    public Player getPlayer() {
    	return player;
    }
    
    public void setPlayer(Player player) {
    	this.player = player;
    }
    
    public Location getLocationBeforeHomeTp() {
    	return locationBeforeHomeTp;
    }
    
    public void setLocationBeforeHomeTp(Location locationBeforeHomeTp) {
    	this.locationBeforeHomeTp = locationBeforeHomeTp;
    }

}
