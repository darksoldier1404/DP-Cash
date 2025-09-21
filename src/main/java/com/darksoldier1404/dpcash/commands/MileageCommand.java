package com.darksoldier1404.dpcash.commands;

import com.darksoldier1404.dpcash.functions.CashFunction;
import com.darksoldier1404.dpcash.functions.CommonFunction;
import com.darksoldier1404.dppc.builder.command.CommandBuilder;
import org.bukkit.OfflinePlayer;

import static com.darksoldier1404.dpcash.CashPlugin.*;

public class MileageCommand {
    private final CommandBuilder builder = new CommandBuilder(plugin);

    public MileageCommand() {
        builder.addSubCommand("give", "dpcash.admin", plugin.getLang().get("mileage_cmd_give"), (p, args) -> {
            if (args.length == 3) {
                if (args[2].matches("\\d+")) {
                    OfflinePlayer target = CommonFunction.getOfflinePlayer(args[1]);
                    int amount = Integer.parseInt(args[2]);
                    CashFunction.giveMileage(p, target, amount);
                } else {
                    p.sendMessage(plugin.getPrefix() + plugin.getLang().get("mileage_cmd_amount_number"));
                }
                return true;
            }
            return false;
        });
        builder.addSubCommand("take", "dpcash.admin", plugin.getLang().get("mileage_cmd_take"), (p, args) -> {
            if (args.length == 3) {
                if (args[2].matches("\\d+")) {
                    OfflinePlayer target = CommonFunction.getOfflinePlayer(args[1]);
                    int amount = Integer.parseInt(args[2]);
                    CashFunction.takeMileage(p, target, amount);
                } else {
                    p.sendMessage(plugin.getPrefix() + plugin.getLang().get("mileage_cmd_amount_number"));
                }
                return true;

            }
            return false;
        });
        builder.addSubCommand("set", "dpcash.admin", plugin.getLang().get("mileage_cmd_set"), (p, args) -> {
            if (args.length == 3) {
                if (args[2].matches("\\d+")) {
                    OfflinePlayer target = CommonFunction.getOfflinePlayer(args[1]);
                    int amount = Integer.parseInt(args[2]);
                    CashFunction.setMileage(p, target, amount);
                } else {
                    p.sendMessage(plugin.getPrefix() + plugin.getLang().get("mileage_cmd_amount_number"));
                }
                return true;
            }
            return false;
        });
        builder.addSubCommand("info", "dpcash.admin", plugin.getLang().get("mileage_cmd_info"), (p, args) -> {
            if (args.length == 2) {
                OfflinePlayer target = CommonFunction.getOfflinePlayer(args[1]);
                CashFunction.infoMileage(p, target);
            } else {
                p.sendMessage(plugin.getPrefix() + plugin.getLang().get("mileage_cmd_info_usage"));
            }
            return true;
        });
        builder.addSubCommand("my", "dpcash.user", plugin.getLang().get("mileage_cmd_my"), (p, args) -> {
            if (args.length == 1) {
                OfflinePlayer target = (OfflinePlayer) p;
                CashFunction.infoMileage(p, target);
            } else {
                p.sendMessage(plugin.getPrefix() + plugin.getLang().get("mileage_cmd_my_usage"));
            }
            return true;
        });
        builder.addSubCommand("reset", "dpcash.admin", plugin.getLang().get("mileage_cmd_reset"), (p, args) -> {
            if (args.length == 2) {
                OfflinePlayer target = CommonFunction.getOfflinePlayer(args[1]);
                CashFunction.resetMileage(p, target);
            } else {
                p.sendMessage(plugin.getPrefix() + plugin.getLang().get("mileage_cmd_reset_usage"));
            }
            return true;
        });
    }

    public CommandBuilder getExecutor() {
        return builder;
    }
}
