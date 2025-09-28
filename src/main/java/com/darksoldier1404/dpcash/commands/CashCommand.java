package com.darksoldier1404.dpcash.commands;

import com.darksoldier1404.dpcash.functions.CashFunction;
import com.darksoldier1404.dpcash.functions.CommonFunction;
import com.darksoldier1404.dppc.builder.command.CommandBuilder;
import org.bukkit.OfflinePlayer;

import static com.darksoldier1404.dpcash.CashPlugin.*;

public class CashCommand {
    private final CommandBuilder builder = new CommandBuilder(plugin);

    public CashCommand() {
        builder.addSubCommand("give", "dpcash.admin", plugin.getLang().get("cash_cmd_set"), (p, args) -> {
            if (args.length == 3) {
                if (args[2].matches("\\d+")) {
                    OfflinePlayer target = CommonFunction.getOfflinePlayer(args[1]);
                    int amount = Integer.parseInt(args[2]);
                    CashFunction.giveCash(p, target, amount);
                } else {
                    p.sendMessage(plugin.getPrefix() + plugin.getLang().get("cash_cmd_amount_number"));
                }
                return true;
            }
            return false;
        });
        builder.addSubCommand("take", "dpcash.admin", plugin.getLang().get("cash_cmd_take"), (p, args) -> {
            if (args.length == 3) {
                if (args[2].matches("\\d+")) {
                    OfflinePlayer target = CommonFunction.getOfflinePlayer(args[1]);
                    int amount = Integer.parseInt(args[2]);
                    CashFunction.takeCash(p, target, amount);
                } else {
                    p.sendMessage(plugin.getPrefix() + plugin.getLang().get("cash_cmd_amount_number"));
                }
                return true;
            }
            return false;
        });
        builder.addSubCommand("set", "dpcash.admin", plugin.getLang().get("cash_cmd_set"), (p, args) -> {
            if (args.length == 3) {
                if (args[2].matches("\\d+")) {
                    OfflinePlayer target = CommonFunction.getOfflinePlayer(args[1]);
                    int amount = Integer.parseInt(args[2]);
                    CashFunction.setCash(p, target, amount);
                } else {
                    p.sendMessage(plugin.getPrefix() + plugin.getLang().get("cash_cmd_amount_number"));
                }
                return true;
            }
            return false;
        });
        builder.addSubCommand("info", "dpcash.admin", plugin.getLang().get("cash_cmd_info"), (p, args) -> {
            if (args.length == 2) {
                OfflinePlayer target = CommonFunction.getOfflinePlayer(args[1]);
                CashFunction.infoCash(p, target);
            } else {
                p.sendMessage(plugin.getPrefix() + plugin.getLang().get("cash_cmd_info_usage"));
            }
            return true;
        });
        builder.addSubCommand("my", "dpcash.user", plugin.getLang().get("cash_cmd_my"), (p, args) -> {
            if (args.length == 1) {
                OfflinePlayer target = (OfflinePlayer) p;
                CashFunction.infoCash(p, target);
            } else {
                p.sendMessage(plugin.getPrefix() + plugin.getLang().get("cash_cmd_my_usage"));
            }
            return true;
        });
        builder.addSubCommand("reset", "dpcash.admin", plugin.getLang().get("cash_cmd_reset"), (p, args) -> {
            if (args.length == 2) {
                OfflinePlayer target = CommonFunction.getOfflinePlayer(args[1]);
                CashFunction.resetCash(p, target);
            } else {
                p.sendMessage(plugin.getPrefix() + plugin.getLang().get("cash_cmd_reset_usage"));
            }
            return true;
        });
    }

    public CommandBuilder getExecutor() {
        return builder;
    }
}
