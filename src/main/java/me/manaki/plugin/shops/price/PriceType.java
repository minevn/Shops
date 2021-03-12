package me.manaki.plugin.shops.price;

import org.black_ixx.playerpoints.PlayerPoints;
import org.black_ixx.playerpoints.PlayerPointsAPI;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import me.realized.tokenmanager.api.TokenManager;
import net.milkbowl.vault.economy.Economy;

public enum PriceType {
	
	POINT {
		@Override
		public double getAsset(Player player) {
			if (Bukkit.getPluginManager().isPluginEnabled("PlayerPoints")) {
				PlayerPointsAPI api = PlayerPoints.getPlugin(PlayerPoints.class).getAPI();
				return api.look(player.getUniqueId());
			}
			return 0;
		}

		@Override
		public boolean pay(Player player, double value) {
			if (!canPay(player, value)) return false;
			PlayerPointsAPI api = PlayerPoints.getPlugin(PlayerPoints.class).getAPI();
			api.set(player.getUniqueId(), new Double(getAsset(player) - value).intValue());
			return true;
		}
	},
	TOKEN {
		@Override
		public double getAsset(Player player) {
			if (Bukkit.getPluginManager().isPluginEnabled("TokenManager")) {
				TokenManager tokenManager = (TokenManager) Bukkit.getPluginManager().getPlugin("TokenManager");
				return tokenManager.getTokens(player).getAsLong();
			}
			return 0;
		}

		@Override
		public boolean pay(Player player, double value) {
			if (!canPay(player, value)) return false;
			TokenManager tokenManager = (TokenManager) Bukkit.getPluginManager().getPlugin("TokenManager");
			tokenManager.setTokens(player, new Double(getAsset(player) - value).longValue());
			return true;
		}
	},
	MONEY {
		@Override
		public double getAsset(Player player) {
			if (Bukkit.getPluginManager().isPluginEnabled("Vault")) {
				Economy eco = (Economy) Bukkit.getServer().getServicesManager().getRegistration(Economy.class).getProvider();
				return eco.getBalance(player);
			}
			return 0;
		}

		@Override
		public boolean pay(Player player, double value) {
			if (!canPay(player, value)) return false;
			Economy eco = (Economy) Bukkit.getServer().getServicesManager().getRegistration(Economy.class).getProvider();
			eco.withdrawPlayer(player, value);
			return true;
		}
	};
	
	
	private String unit;
	private String color;
	
	public abstract double getAsset(Player player);
	public abstract boolean pay(Player player, double value);
	
	public String getUnit() {
		return this.unit;
	}
	
	public String getColor() {
		return this.color;
	}
	
	public boolean canPay(Player player, double value) {
		return getAsset(player) >= value;
	}
	
	public void load(FileConfiguration config) {
		this.unit = config.getString("price." + this.name().toLowerCase() + ".unit");
		this.color = config.getString("price." + this.name().toLowerCase() + ".color").replace("&", "§");
	}
	
	public static void loadAll(FileConfiguration config) {
		for (PriceType pt : values()) {
			pt.load(config);
		}
	}
	
}
