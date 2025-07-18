package com.darksoldier1404.dpcash.commands;

import com.darksoldier1404.dpcash.functions.CashFunction;
import com.darksoldier1404.dpcash.functions.CommonFunction;
import com.darksoldier1404.dppc.builder.command.CommandBuilder;
import org.bukkit.OfflinePlayer;

import static com.darksoldier1404.dpcash.CashPlugin.*;

public class CashCommand {
    private final CommandBuilder builder = new CommandBuilder(prefix);

    public CashCommand() {
        builder.addSubCommand("give", "dpcash.admin", lang.get("cash_cmd_give"), (p, args) -> {
            if (args.length == 3) {
                if (args[2].matches("\\d+")) {
                    OfflinePlayer target = CommonFunction.getOfflinePlayer(args[1]);
                    int amount = Integer.parseInt(args[2]);
                    CashFunction.giveCash(p, target, amount);
                } else {
                    p.sendMessage(prefix + lang.get("cash_cmd_amount_number"));
                }
            }
        });
        builder.addSubCommand("take", "dpcash.admin", lang.get("cash_cmd_take"), (p, args) -> {
            if (args.length == 3) {
                if (args[2].matches("\\d+")) {
                    OfflinePlayer target = CommonFunction.getOfflinePlayer(args[1]);
                    int amount = Integer.parseInt(args[2]);
                    CashFunction.takeCash(p, target, amount);
                } else {
                    p.sendMessage(prefix + lang.get("cash_cmd_amount_number"));
                }
            }
        });
        builder.addSubCommand("set", "dpcash.admin", lang.get("cash_cmd_set"), (p, args) -> {
            if (args.length == 3) {
                if (args[2].matches("\\d+")) {
                    OfflinePlayer target = CommonFunction.getOfflinePlayer(args[1]);
                    int amount = Integer.parseInt(args[2]);
                    CashFunction.setCash(p, target, amount);
                } else {
                    p.sendMessage(prefix + lang.get("cash_cmd_amount_number"));
                }
            }
        });
        builder.addSubCommand("info", "dpcash.admin", lang.get("cash_cmd_info"), (p, args) -> {
            if (args.length == 2) {
                OfflinePlayer target = CommonFunction.getOfflinePlayer(args[1]);
                CashFunction.infoCash(p, target);
            } else {
                p.sendMessage(prefix + lang.get("cash_cmd_info_usage"));
            }
        });
        builder.addSubCommand("my", "dpcash.user", lang.get("cash_cmd_my"), (p, args) -> {
            if (args.length == 1) {
                OfflinePlayer target = (OfflinePlayer) p;
                CashFunction.infoCash(p, target);
            } else {
                p.sendMessage(prefix + lang.get("cash_cmd_my_usage"));
            }
        });
        builder.addSubCommand("reset", "dpcash.admin", lang.get("cash_cmd_reset"), (p, args) -> {
            if (args.length == 2) {
                OfflinePlayer target = CommonFunction.getOfflinePlayer(args[1]);
                CashFunction.resetCash(p, target);
            } else {
                p.sendMessage(prefix + lang.get("cash_cmd_reset_usage"));
            }
        });
    }

    public CommandBuilder getExecutor() {
        return builder;
    }
}
