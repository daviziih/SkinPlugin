package me.davi.api;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;

import me.davi.Skin;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import net.minecraft.server.v1_8_R3.PacketPlayOutPlayerInfo;
import net.minecraft.server.v1_8_R3.PacketPlayOutRespawn;
import net.minecraft.server.v1_8_R3.PlayerConnection;
import net.minecraft.server.v1_8_R3.PacketPlayOutPlayerInfo.EnumPlayerInfoAction;

public class SkinAPI {

	public static void reloadSkinForSelf(Player player) {
		EntityPlayer ep = ((CraftPlayer) player).getHandle();
		PacketPlayOutPlayerInfo removeInfo = new PacketPlayOutPlayerInfo(EnumPlayerInfoAction.REMOVE_PLAYER, ep);
		PacketPlayOutPlayerInfo addInfo = new PacketPlayOutPlayerInfo(EnumPlayerInfoAction.ADD_PLAYER, ep);
		ep.playerConnection.sendPacket(removeInfo);
		ep.playerConnection.sendPacket(addInfo);
		new BukkitRunnable() {
			@Override
			public void run() {
				ep.playerConnection.sendPacket(new PacketPlayOutRespawn(ep.dimension, ep.getWorld().getDifficulty(),
						ep.getWorld().getWorldData().getType(), ep.playerInteractManager.getGameMode()));
				for (Player pl : Bukkit.getOnlinePlayers()) {
					pl.hidePlayer(player);
					pl.showPlayer(player);
				}
			}
		}.runTaskLater(Skin.plugin, 1L);
	}

	public static void setSkinPlayer(Player p, String skin) {
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new InputStreamReader(
					new URL("https://api.mojang.com/users/profiles/minecraft/" + skin).openStream()));
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
			return;
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

		connection.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER,
				((CraftPlayer) p).getHandle()));

		profile.getProperties().removeAll("textures");
		profile.getProperties().put("textures", new Property("textures", prop.split(" : ")[0], prop.split(" : ")[1]));

		connection.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER,
				((CraftPlayer) p).getHandle()));

		SkinAPI.reloadSkinForSelf(p);
		p.sendMessage("§b§lSKIN§f Agora você está utilizando a skin §e§l" + skin);
	}

}
