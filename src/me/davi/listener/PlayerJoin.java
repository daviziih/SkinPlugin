package me.davi.listener;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import me.davi.Skin;
import me.davi.api.SkinGrabber;

public class PlayerJoin implements Listener {

	public PlayerJoin(Skin main) {
		Bukkit.getPluginManager().registerEvents(this, main);
	}

	@EventHandler
	private void onJoin(PlayerJoinEvent e) {
		SkinGrabber.chageSkin(e.getPlayer());
	}
}
