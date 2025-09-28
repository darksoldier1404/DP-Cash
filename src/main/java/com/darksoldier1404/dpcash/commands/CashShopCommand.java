package com.darksoldier1404.dpcash.commands;

import com.darksoldier1404.dpcash.enums.ShopType;
import com.darksoldier1404.dpcash.functions.CommonFunction;
import com.darksoldier1404.dpcash.functions.ShopFunction;
import com.darksoldier1404.dppc.builder.command.CommandBuilder;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

import static com.darksoldier1404.dpcash.CashPlugin.*;

public class CashShopCommand {
    private final CommandBuilder builder = new CommandBuilder(plugin);

    public CashShopCommand() {
        builder.addSubCommand("create", "dpcash.admin", plugin.getLang().get("cashshop_cmd_create"), true, (p, args) -> {
            if (args.length == 4) {
                ShopFunction.createShop((Player) p, args[1], args[2], args[3]);
            } else {
                p.sendMessage(plugin.getPrefix() + plugin.getLang().get("cashshop_cmd_create_usage"));
            }
            return true;
        });
        builder.addSubCommand("items", "dpcash.admin", plugin.getLang().get("cashshop_cmd_items"), true, (p, args) -> {
            if (args.length == 2) {
                ShopFunction.openShopItemSetting((Player) p, args[1]);
            } else {
                p.sendMessage(plugin.getPrefix() + plugin.getLang().get("cashshop_cmd_items_usage"));
            }
            return true;
        });
        builder.addSubCommand("price", "dpcash.admin", plugin.getLang().get("cashshop_cmd_price"), true, (p, args) -> {
            if (args.length == 2) {
                ShopFunction.openShopPriceSetting((Player) p, args[1]);
            } else {
                p.sendMessage(plugin.getPrefix() + plugin.getLang().get("cashshop_cmd_price_usage"));
            }
            return true;
        });
        builder.addSubCommand("maxpage", "dpcash.admin", plugin.getLang().get("cashshop_cmd_maxpage"), true, (p, args) -> {
            if (args.length == 3) {
                ShopFunction.setShopMaxPage((Player) p, args[1], args[2]);
            } else {
                p.sendMessage(plugin.getPrefix() + plugin.getLang().get("cashshop_cmd_maxpage_usage"));
            }
            return true;
        });
        // delete
        builder.addSubCommand("delete", "dpcash.admin", plugin.getLang().get("cashshop_cmd_delete"), true, (p, args) -> {
            if (args.length == 2) {
                ShopFunction.deleteShop((Player) p, args[1]);
            } else {
                p.sendMessage(plugin.getPrefix() + plugin.getLang().get("cashshop_cmd_delete"));
            }
            return true;
        });
        builder.addSubCommand("reload", "dpcash.admin", plugin.getLang().get("cashshop_cmd_reload"), (p, args) -> {
            if (args.length == 1) {
                plugin.init();
            } else {
                p.sendMessage(plugin.getPrefix() + plugin.getLang().get("cashshop_cmd_reload_usage"));
            }
            return true;
        });
        builder.addSubCommand("open", "dpcash.user", plugin.getLang().get("cashshop_cmd_open"), true, (p, args) -> {
            if (args.length == 2) {
                ShopFunction.openShop((Player) p, args[1]);
            } else {
                p.sendMessage(plugin.getPrefix() + plugin.getLang().get("cashshop_cmd_open_usage"));
            }
            return true;
        });
        for (String c : builder.getSubCommandNames()) {
            builder.addTabCompletion(c, (sender, args) -> {
                if (args.length == 1) {
                    return builder.getSubCommandNames();
                }
                if (args.length == 2 && builder.getSubCommandNames().contains(args[0].toLowerCase())) {
                    return new ArrayList<>(shops.keySet());
                }
                if (args.length == 3 && args[0].equalsIgnoreCase("create")) {
                    return Arrays.asList("2", "3", "4", "5", "6");
                }
                if (args.length == 4 && builder.getSubCommandNames().contains(args[0].toLowerCase())) {
                    return Arrays.stream(ShopType.values())
                            .map(ShopType::name)
                            .collect(Collectors.toList());
                }
                return null;
            });
        }
    }

    public CommandBuilder getExecutor() {
        return builder;
    }
}