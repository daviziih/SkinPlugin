package me.davi.database;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.bukkit.Bukkit;

import me.davi.Skin;

public class Connections {

	public static Connection con;

	private static String ip = Skin.plugin.getConfig().getString("mysql_ip");
	private static String usuario = Skin.plugin.getConfig().getString("mysql_usuario");
	private static String senha = Skin.plugin.getConfig().getString("mysql_senha");
	private static String database = Skin.plugin.getConfig().getString("mysql_database");

	public static void openConnectionMySQL() {
		try {
			con = DriverManager.getConnection("jdbc:mysql://" + ip + ":3306/" + database, usuario, senha);
			Bukkit.getConsoleSender().sendMessage("§b§lMYSQL§f A coxexão com §b§lMYSQL§f foi aberta!");
			createTable();
		} catch (Exception e) {
			// e.printStackTrace();
			Bukkit.getConsoleSender()
					.sendMessage("§b§lMYSQL§f A coxexão com §b§lMYSQL§f §c§lFALHOU§f, o plugin será desabilitado!");
			Skin.plugin.getPluginLoader().disablePlugin(Skin.plugin);
			openConnectionSQLite();
		}
	}

	public static void openConnectionSQLite() {
		File file = new File(Skin.plugin.getDataFolder(), "database.db");
		try {
			Class.forName("org.sqlite.JDBC");
			con = DriverManager.getConnection("jdbc:sqlite:" + file);
			Bukkit.getConsoleSender().sendMessage("§e§lSQLITE§f A coxexão com §e§lSQLITE§f foi aberta!");
			createTable();
		} catch (Exception e) {
			// e.printStackTrace();
			Bukkit.getConsoleSender()
					.sendMessage("§e§lSQLITE§f A coxexão com §e§lSQLITE§f §c§lFALHOU§f, o plugin será desabilitado!");
			Skin.plugin.getPluginLoader().disablePlugin(Skin.plugin);
		}
	}

	public static void closeConnections() {
		if (con != null) {
			try {
				con = null;
				Bukkit.getConsoleSender()
						.sendMessage("§e§lCONNECITONS§f As conexões do plugin foi §c§lDESABILITADAS§f!");
			} catch (Exception e) {
				e.printStackTrace();
				Bukkit.getConsoleSender().sendMessage("§e§lCONNECITONS§f Erro ao fechar as §b§lCONEXÕES§f do plugin!");
			}
		}
	}

	private static void createTable() {
		PreparedStatement stm = null;
		try {
			stm = con.prepareStatement(
					"CREATE TABLE IF NOT EXISTS PlayerSkin (UUID TEXT, PLAYER TEXT, SKIN TEXT, TYPE TEXT)");
			stm.execute();
			stm.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
