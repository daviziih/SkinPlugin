package me.davi.enums;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;

import me.davi.Skin;
import me.davi.api.SkinAPI;

public enum Skins {

	NONE("Nenhum", "", ""),
	SKIN1(Skin.plugin.getConfig().getString("skin1_name"),
			Skin.plugin.getConfig().getString("skin1_skin").replace("§", "&"),
			Skin.plugin.getConfig().getString("skin1_lore"));

	private String name;
	private String nameSkull;
	private String[] description;

	private Skins(String name, String nameSkull, String... description) {
		this.name = name;
		this.nameSkull = nameSkull;
		this.description = description;
	}

	public String getName() {
		return name;
	}

	public String getSkuul() {
		return nameSkull;
	}

	public List<String> setDescription(String... desc) {
		List<String> lore = new ArrayList<String>();
		for (String descriptionL : this.description) {
			lore.add(descriptionL);
		}
		for (String desc2 : desc) {
			lore.add(desc2);
		}
		return lore;
	}

	public static void setSkin(Player p, Skins skin) {
		setSkins(p, skin);
	}

	private static void setSkins(Player p, Skins skins) {
		if (skins.getName().equalsIgnoreCase(SKIN1.getName())) {
			SkinAPI.setSkinPlayer(p, Skin.plugin.getConfig().getString("skin1_skin"));
		}
	}
}
