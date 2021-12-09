package me.davi.inventory;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import me.davi.Skin;
import me.davi.enums.Skins;

public class SkinInventory implements Listener {

	public SkinInventory(Skin main) {
		Bukkit.getPluginManager().registerEvents(this, main);

		List<ItemStack> items = new ArrayList<>();

		int a = 0;

		for (Skins skins : Skins.values()) {
			if (a <= 18) {
				if (Skins.NONE.getName() != skins.getName()) {
					ItemStack Skull = new ItemStack(Material.SKULL_ITEM);
					Skull.setDurability((short) 3);
					Skull.setAmount(1);
					SkullMeta Skull1 = (SkullMeta) Skull.getItemMeta();
					Skull1.setOwner(skins.getSkuul());
					Skull1.setDisplayName("§a" + skins.getName());
					Skull1.setLore(skins.setDescription(" ", "§eClique para selecionar"));
					Skull.setItemMeta(Skull1);
					items.add(Skull);
				}
			} else {
			}
			a++;
		}

		int slot = 11;
		for (ItemStack itens : items) {
			while (isColum(slot, 9) || isColum(slot, 2)) {
				slot++;
			}
			inventory.setItem(slot++, itens);
		}

	}

	private static String nameinventory = "§7Sua skin";

	private static Inventory inventory = Bukkit.createInventory(null, 9 * 5, nameinventory);

	public static void openInventoryServer(Player p) {

//		Api.setSkullInventory(inventory, 9, MySQLManager.getCustomSkinPlayer(p),
//				"§aSua skin: §f" + MySQLManager.getCustomSkinPlayer(p),
//				Arrays.asList("", "§7Fonte: §f" + MySQLManager.getCustomSkinType(p), ""));
//
//		Api.setItemInventory(inventory, 18, Material.REDSTONE_BLOCK, "§cEm Breve", null);
//
//		Api.setItemInventory(inventory, 27, Material.NAME_TAG, "§aCustomizar Skin",
//				Arrays.asList("", "§7Escolha uma skin customizada", "§7baseado em um nickname", ""));

		p.openInventory(inventory);
	}

	public static boolean isColum(int index, int colum) {
		if (index < 9) {
			index += 1;
		}
		index = (index % 9) + 1;

		return index == colum;

	}

	@EventHandler
	private static void onMenuOpen(InventoryClickEvent e) {

		if (e.getSlotType().equals(InventoryType.SlotType.OUTSIDE)) {
			return;
		}
		Player p = (Player) e.getWhoClicked();
		if (e.getInventory().getTitle().equals(nameinventory)) {
			e.setCancelled(true);
			if (e.getCurrentItem() != null) {
				for (Skins skin : Skins.values()) {
					if (e.getCurrentItem().getItemMeta().getDisplayName().contains(skin.getName())) {
						Skins.setSkin(p, skin);
						p.sendMessage("§b§lSKIN§f Agora você está utilizando a skin §e§l" + skin.getName());
						p.closeInventory();
						return;
					}
				}
				if (e.getSlot() == 27) {
					if (!p.hasPermission("skin.custom")) {
						p.sendMessage("§b§lSKIN§f Esse função esta §a§lDISPONIVEL§f somente pra §b§lVIPS§f!");
						p.closeInventory();
						return;
					}
					p.sendMessage("§b§lSKIN§f Use correto: §b/skin (skinName)");
					p.closeInventory();
					return;
				}
			}
		}
	}
}
