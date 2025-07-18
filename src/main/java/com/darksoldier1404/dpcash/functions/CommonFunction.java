package com.darksoldier1404.dpcash.functions;

import com.darksoldier1404.dpcash.obj.CashUser;
import com.darksoldier1404.dpcash.obj.Shop;
import com.darksoldier1404.dppc.api.placeholder.PlaceholderBuilder;
import com.darksoldier1404.dppc.lang.DLang;
import com.darksoldier1404.dppc.utils.ColorUtils;
import com.darksoldier1404.dppc.utils.ConfigUtils;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import static com.darksoldier1404.dpcash.CashPlugin.*;

public class CommonFunction {
    public static void init() {
        config = ConfigUtils.loadDefaultPluginConfig(plugin);
        lang = new DLang(config.getString("Settings.Lang") == null ? "English" : config.getString("Settings.Lang"), plugin);
        prefix = ColorUtils.applyColor(config.getString("Settings.prefix"));
        loadShops();
    }

    public static void initPlaceholders() {
        PlaceholderBuilder.Builder pb = new PlaceholderBuilder.Builder(plugin);
        pb.identifier("dpcash");
        pb.version(plugin.getDescription().getVersion());
        pb.onRequest(
                (player, string) -> {
                    switch (string) {
                        case "cash":
                            if (cashUsers.containsKey(player.getUniqueId())) {
                                CashUser cu = cashUsers.get(player.getUniqueId());
                                return String.valueOf(cu.getCurrentCash());
                            } else {
                                return "0";
                            }
                        case "total_cash_earned":
                            if (cashUsers.containsKey(player.getUniqueId())) {
                                CashUser cu = cashUsers.get(player.getUniqueId());
                                return String.valueOf(cu.getTotalCashEarned());
                            } else {
                                return "0";
                            }
                        case "total_cash_spent":
                            if (cashUsers.containsKey(player.getUniqueId())) {
                                CashUser cu = cashUsers.get(player.getUniqueId());
                                return String.valueOf(cu.getTotalCashSpent());
                            } else {
                                return "0";
                            }
                        case "mileage":
                            if (cashUsers.containsKey(player.getUniqueId())) {
                                CashUser cu = cashUsers.get(player.getUniqueId());
                                return String.valueOf(cu.getCurrentMileage());
                            } else {
                                return "0";
                            }
                        case "total_mileage_earned":
                            if (cashUsers.containsKey(player.getUniqueId())) {
                                CashUser cu = cashUsers.get(player.getUniqueId());
                                return String.valueOf(cu.getTotalMileageEarned());
                            } else {
                                return "0";
                            }
                        case "total_mileage_spent":
                            if (cashUsers.containsKey(player.getUniqueId())) {
                                CashUser cu = cashUsers.get(player.getUniqueId());
                                return String.valueOf(cu.getTotalMileageSpent());
                            } else {
                                return "0";
                            }
                    }
                    return null;
                }
        );
        pb.build();
    }

    public static void loadShops() {
        ConfigUtils.loadCustomDataList(plugin, "shops").forEach(data -> {
            String shopName = data.getString("name");
            if (shopName != null) {
                Shop shop = Shop.load(data);
                shops.put(shopName, shop);
            }
        });
    }

    public static void saveConfig() {
        ConfigUtils.savePluginConfig(plugin, config);
        cashUsers.values().forEach(CommonFunction::saveUser);
        shops.values().forEach(Shop::save);
    }

    public static void initUser(Player p) {
        CashUser cu = CashUser.deserialize(ConfigUtils.initUserData(plugin, p.getUniqueId().toString(), "users"));
        if (cu == null) {
            cu = new CashUser(p.getUniqueId());
            saveUser(cu);
        }
        cashUsers.put(p.getUniqueId(), cu);
    }

    public static void saveUserAndQuit(Player p) {
        CashUser cu = cashUsers.get(p.getUniqueId());
        if (cu != null) {
            saveUser(cu);
            cashUsers.remove(p.getUniqueId());
        }
    }

    public static void saveUser(CashUser cu) {
        ConfigUtils.saveCustomData(plugin, cu.serialize(), cu.getUUID().toString(), "users");
    }

    public static CashUser getCashUser(Player p) {
        return cashUsers.get(p.getUniqueId());
    }

    public static OfflinePlayer getOfflinePlayer(String nameOrUUID) {
        for (OfflinePlayer player : plugin.getServer().getOfflinePlayers()) {
            if (player.getName() != null && player.getName().equalsIgnoreCase(nameOrUUID)) {
                return player;
            }
            if (player.getUniqueId().toString().equals(nameOrUUID)) {
                return player;
            }
        }
        return null;
    }
}
