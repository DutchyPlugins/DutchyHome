package dev.array21.dutchyhome.events;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import dev.array21.dutchycore.module.events.ModuleEvent;

public class HomeTeleportEvent extends ModuleEvent {

	private Player teleportedPlayer;
	private Location preTeleportLocation, postTeleportLocation;
	
	public HomeTeleportEvent(Player teleportedPlayer, Location preTeleportLocation, Location postTeleportLocation) {
		this.teleportedPlayer = teleportedPlayer;
		this.preTeleportLocation = preTeleportLocation;
		this.postTeleportLocation = postTeleportLocation;
	}
	
	/**
	 * Get the player that was teleported
	 * @return Returns a player
	 */
	public Player getTeleportedPlayer() {
		return this.teleportedPlayer;
	}
	
	/**
	 * Get the location the player was at before they got teleported
	 * @return Returns a Location
	 */
	public Location getPreTeleportLocation() {
		return this.preTeleportLocation;
	}
	
	/**
	 * Get the location the player was teleported to
	 * @return Returns a Location
	 */
	public Location getPostTeleportLocation() {
		return this.postTeleportLocation;
	}
}
