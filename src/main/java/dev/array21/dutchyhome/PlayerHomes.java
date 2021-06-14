package dev.array21.dutchyhome;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Location;

import dev.array21.dutchycore.annotations.Nullable;

public class PlayerHomes {

	private UUID player;
	private HashMap<String, Location> homes = new HashMap<>();
	
	/**
	 * Create a new PlayerHomes object
	 * @param player The UUID of the owner of this PlayerHomes object
	 */
	public PlayerHomes(UUID player) {
		this.player = player;
	}
	
	/**
	 * Get the home owner
	 * @return Returns the UUID of the owner of this PlayerHomes object
	 */
	public UUID getOwner() {
		return this.player;
	}
	
	/**
	 * Get a home
	 * @param name The name of the home
	 * @return Returns the Location associated with the provided name
	 */
	@Nullable
	public Location getHome(String name) {
		return this.homes.get(name);
	}
	
	/**
	 * Set a home
	 * @param name The name of the home
	 * @param location The location of the home
	 */
	public void setHome(String name, Location location) {
		this.homes.put(name, location);
	}
	
	/**
	 * Remove a home
	 * @param name The name of the home
	 */
	public void delHome(String name) {
		this.homes.remove(name);
	}
	
	/**
	 * Get all homes
	 * @return Returns a HashMap containing all homes, k = name, v = location
	 */
	public HashMap<String, Location> getAllHomes() {
		return this.homes;
	}
}
