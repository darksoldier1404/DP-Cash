package com.darksoldier1404.dpcash.functions;

import com.darksoldier1404.dpcash.obj.CashUser;
import com.darksoldier1404.dppc.api.placeholder.PlaceholderBuilder;
import com.darksoldier1404.dppc.utils.ConfigUtils;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.UUID;

import static com.darksoldier1404.dpcash.CashPlugin.*;

public class CommonFunction {

    public static void initPlaceholders() {
        PlaceholderBuilder.Builder pb = new PlaceholderBuilder.Builder(plugin);
        pb.identifier("dpcash");
        pb.version(plugin.getDescription().getVersion());
        pb.onRequest(
                (player, string) -> {
                    switch (string) {
                        case "cash":
                            if (udata.containsKey(player.getUniqueId())) {
                                CashUser cu = udata.get(player.getUniqueId());
                                return String.valueOf(cu.getCurrentCash());
                            } else {
                                return "0";
                            }
                        case "total_cash_earned":
                            if (udata.containsKey(player.getUniqueId())) {
                                CashUser cu = udata.get(player.getUniqueId());
                                return String.valueOf(cu.getTotalCashEarned());
                            } else {
                                return "0";
                            }
                        case "total_cash_spent":
                            if (udata.containsKey(player.getUniqueId())) {
                                CashUser cu = udata.get(player.getUniqueId());
                                return String.valueOf(cu.getTotalCashSpent());
                            } else {
                                return "0";
                            }
                        case "mileage":
                            if (udata.containsKey(player.getUniqueId())) {
                                CashUser cu = udata.get(player.getUniqueId());
                                return String.valueOf(cu.getCurrentMileage());
                            } else {
                                return "0";
                            }
                        case "total_mileage_earned":
                            if (udata.containsKey(player.getUniqueId())) {
                                CashUser cu = udata.get(player.getUniqueId());
                                return String.valueOf(cu.getTotalMileageEarned());
                            } else {
                                return "0";
                            }
                        case "total_mileage_spent":
                            if (udata.containsKey(player.getUniqueId())) {
                                CashUser cu = udata.get(player.getUniqueId());
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

    public static void initUser(Player p) {
        UUID uuid = p.getUniqueId();
        CashUser cu = new CashUser(uuid).deserialize(ConfigUtils.initUserData(plugin, uuid.toString(), "users"));
        if (cu == null) {
            cu = new CashUser(uuid);
            saveUser(uuid);
        }
        udata.put(uuid, cu);
    }

    public static void saveUserAndQuit(Player p) {
        saveUser(p.getUniqueId());
        udata.remove(p.getUniqueId());
    }

    public static void saveUser(UUID uuid) {
        udata.save(uuid);
    }

    public static CashUser getCashUser(Player p) {
        return udata.get(p.getUniqueId());
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
