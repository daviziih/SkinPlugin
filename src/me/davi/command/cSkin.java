package me.davi.command;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;

import me.davi.Skin;
import me.davi.api.SkinAPI;
import me.davi.inventory.SkinInventory;
import net.minecraft.server.v1_8_R3.PacketPlayOutPlayerInfo;
import net.minecraft.server.v1_8_R3.PlayerConnection;

public class cSkin extends Command {

	public cSkin(String name) {
		super(name);
	}

	public static ArrayList<Player> cooldown = new ArrayList<>();

	@Override
	public boolean execute(CommandSender sender, String label, String[] args) {
		if (!(sender instanceof Player)) {
			return true;
		}
		Player p = (Player) sender;

		if (args.length == 0) {
			SkinInventory.openInventoryServer(p);
			return true;
		}

		if (args[0].equalsIgnoreCase("reset")) {
			BufferedReader reader = null;
			try {
				reader = new BufferedReader(new InputStreamReader(
						new URL("https://api.mojang.com/users/profiles/minecraft/" + p.getName()).openStream()));
			} catch (IOException e4) {
				e4.printStackTrace();
			}
			String str = "";
			String line;
			try {
				while ((line = reader.readLine()) != null) {
					str += line;
				}
			} catch (IOException e3) {
				e3.printStackTrace();
			}

			String id = new JsonParser().parse(str).getAsJsonObject().get("id").getAsString();

			try {
				reader.close();
			} catch (IOException e2) {
				e2.printStackTrace();
			}
			try {
				reader = new BufferedReader(new InputStreamReader(
						new URL("https://sessionserver.mojang.com/session/minecraft/profile/" + id + "?unsigned=false")
								.openStream()));
			} catch (MalformedURLException e1) {
				e1.printStackTrace();
			} catch (IOException e1) {
				e1.printStackTrace();
			}

			str = "";
			line = null;
			try {
				while ((line = reader.readLine()) != null) {
					str += line;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}

			String prop = "";
			JsonObject profile1 = new JsonParser().parse(str).getAsJsonObject();
			JsonObject object = profile1.get("properties").getAsJsonArray().get(0).getAsJsonObject();
			prop = object.get("value").getAsString() + " : " + object.get("signature").getAsString();

			GameProfile profile = ((CraftPlayer) p).getHandle().getProfile();
			PlayerConnection connection = ((CraftPlayer) p).getHandle().playerConnection;

			connection.sendPacket(new PacketPlayOutPlayerInfo(
					PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER, ((CraftPlayer) p).getHandle()));

			profile.getProperties().removeAll("textures");
			profile.getProperties().put("textures",
					new Property("textures", prop.split(" : ")[0], prop.split(" : ")[1]));

			connection.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER,
					((CraftPlayer) p).getHandle()));
			SkinAPI.reloadSkinForSelf(p);
			p.sendMessage("§b§lSKIN§f Agora você está utilizando a skin §e§l" + p.getName());
			cooldown.add(p);

			new BukkitRunnable() {
				@Override
				public void run() {
					cooldown.remove(p);
				}
			}.runTaskLater(Skin.plugin, 6000L);
		} else {
			if (!p.hasPermission("skin.custom")) {
				p.sendMessage("§b§lSKIN§f Esse função está §a§lDISPONIVEL§f somente pra §b§lVIPS§f!");
				return true;
			}
			BufferedReader reader = null;
			try {
				reader = new BufferedReader(new InputStreamReader(
						new URL("https://api.mojang.com/users/profiles/minecraft/" + args[0]).openStream()));
			} catch (IOException e4) {
				e4.printStackTrace();
			}
			String str = "";
			String line;
			try {
				while ((line = reader.readLine()) != null) {
					str += line;
				}
			} catch (IOException e3) {
				e3.printStackTrace();
			}

			if (str == "") {
				p.sendMessage("§b§lSKIN§f Essa skin §c§lNÃO§f é §6§lPREMIUM§f!");
				return true;
			}

			String id = new JsonParser().parse(str).getAsJsonObject().get("id").getAsString();

			try {
				reader.close();
			} catch (IOException e2) {
				e2.printStackTrace();
			}
			try {
				reader = new BufferedReader(new InputStreamReader(
						new URL("https://sessionserver.mojang.com/session/minecraft/profile/" + id + "?unsigned=false")
								.openStream()));
			} catch (MalformedURLException e1) {
				e1.printStackTrace();
			} catch (IOException e1) {
				e1.printStackTrace();
			}

			str = "";
			line = null;
			try {
				while ((line = reader.readLine()) != null) {
					str += line;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}

			String prop = "";
			JsonObject profile1 = new JsonParser().parse(str).getAsJsonObject();
			JsonObject object = profile1.get("properties").getAsJsonArray().get(0).getAsJsonObject();
			prop = object.get("value").getAsString() + " : " + object.get("signature").getAsString();

			GameProfile profile = ((CraftPlayer) p).getHandle().getProfile();
			PlayerConnection connection = ((CraftPlayer) p).getHandle().playerConnection;

			connection.sendPacket(new PacketPlayOutPlayerInfo(
					PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER, ((CraftPlayer) p).getHandle()));

			profile.getProperties().removeAll("textures");
			profile.getProperties().put("textures",
					new Property("textures", prop.split(" : ")[0], prop.split(" : ")[1]));

			connection.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER,
					((CraftPlayer) p).getHandle()));

			SkinAPI.reloadSkinForSelf(p);
			p.sendMessage("§b§lSKIN§f Agora você está utilizando a skin §e§l" + args[0]);
			cooldown.add(p);
			new BukkitRunnable() {
				@Override
				public void run() {
					cooldown.remove(p);
				}
			}.runTaskLater(Skin.plugin, 6000L);
		}
		return false;
	}
}
