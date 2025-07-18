package com.darksoldier1404.dpcash.commands;

import com.darksoldier1404.dpcash.functions.CashFunction;
import com.darksoldier1404.dpcash.functions.CommonFunction;
import com.darksoldier1404.dppc.builder.command.CommandBuilder;
import org.bukkit.OfflinePlayer;

import static com.darksoldier1404.dpcash.CashPlugin.prefix;
import static com.darksoldier1404.dpcash.CashPlugin.lang;

public class MileageCommand {
    private final CommandBuilder builder = new CommandBuilder(prefix);

    public MileageCommand() {
        builder.addSubCommand("give", "dpcash.admin", lang.get("mileage_cmd_give"), (p, args) -> {
            if (args.length == 3) {
                if (args[2].matches("\\d+")) {
                    OfflinePlayer target = CommonFunction.getOfflinePlayer(args[1]);
                    int amount = Integer.parseInt(args[2]);
                    CashFunction.giveMileage(p, target, amount);
                } else {
                    p.sendMessage(prefix + lang.get("mileage_cmd_amount_number"));
                }
            }
        });
        builder.addSubCommand("take", "dpcash.admin", lang.get("mileage_cmd_take"), (p, args) -> {
            if (args.length == 3) {
                if (args[2].matches("\\d+")) {
                    OfflinePlayer target = CommonFunction.getOfflinePlayer(args[1]);
                    int amount = Integer.parseInt(args[2]);
                    CashFunction.takeMileage(p, target, amount);
                } else {
                    p.sendMessage(prefix + lang.get("mileage_cmd_amount_number"));
                }
            }
        });
        builder.addSubCommand("set", "dpcash.admin", lang.get("mileage_cmd_set"), (p, args) -> {
            if (args.length == 3) {
                if (args[2].matches("\\d+")) {
                    OfflinePlayer target = CommonFunction.getOfflinePlayer(args[1]);
                    int amount = Integer.parseInt(args[2]);
                    CashFunction.setMileage(p, target, amount);
                } else {
                    p.sendMessage(prefix + lang.get("mileage_cmd_amount_number"));
                }
            }
        });
        builder.addSubCommand("info", "dpcash.admin", lang.get("mileage_cmd_info"), (p, args) -> {
            if (args.length == 2) {
                OfflinePlayer target = CommonFunction.getOfflinePlayer(args[1]);
                CashFunction.infoMileage(p, target);
            } else {
                p.sendMessage(prefix + lang.get("mileage_cmd_info_usage"));
            }
        });
        builder.addSubCommand("my", "dpcash.user", lang.get("mileage_cmd_my"), (p, args) -> {
            if (args.length == 1) {
                OfflinePlayer target = (OfflinePlayer) p;
                CashFunction.infoMileage(p, target);
            } else {
                p.sendMessage(prefix + lang.get("mileage_cmd_my_usage"));
            }
        });
        builder.addSubCommand("reset", "dpcash.admin", lang.get("mileage_cmd_reset"), (p, args) -> {
            if (args.length == 2) {
                OfflinePlayer target = CommonFunction.getOfflinePlayer(args[1]);
                CashFunction.resetMileage(p, target);
            } else {
                p.sendMessage(prefix + lang.get("mileage_cmd_reset_usage"));
            }
        });
    }

    public CommandBuilder getExecutor() {
        return builder;
    }
}
