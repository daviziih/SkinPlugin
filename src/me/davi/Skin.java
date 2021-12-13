package me.davi;

import java.lang.reflect.Field;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;
import org.bukkit.plugin.java.JavaPlugin;

import me.davi.command.cSkin;
import me.davi.inventory.SkinInventory;

public class Skin extends JavaPlugin {

	public static Skin plugin;

	@Override
	public void onEnable() {
		plugin = this;
		saveDefaultConfig();

	//	Connections.openConnectionMySQL();

		new SkinInventory(plugin);

		registerCommands();

	}

	@Override
	public void onDisable() {
		plugin = null;
	}

	private static void registerCommands() {
		try {
			final Field commandField = Bukkit.getServer().getClass().getDeclaredField("commandMap");
			commandField.setAccessible(true);

			CommandMap newCommand = (CommandMap) commandField.get(Bukkit.getServer());

			newCommand.register("lobby", new cSkin("skin"));

		} catch (NoSuchFieldException | IllegalArgumentException | IllegalAccessException e) {
		}
	}
}
