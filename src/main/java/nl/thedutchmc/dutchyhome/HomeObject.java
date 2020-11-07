package nl.thedutchmc.dutchyhome;

import java.util.UUID;

import org.bukkit.Location;

public class HomeObject {

	private UUID owner;
	private String name;
	private Location loc;
	
	public HomeObject(UUID owner, String name, Location loc) {
		this.owner = owner;
		this.name = name;
		this.loc = loc;
	}
	
	public UUID getOwner() {
		return this.owner;
	}
	
	public String getName() {
		return this.name;
	}
	
	public Location getLocation() {
		return this.loc;
	}
	
	public void setLocation(Location loc) {
		this.loc = loc;
	}
}
