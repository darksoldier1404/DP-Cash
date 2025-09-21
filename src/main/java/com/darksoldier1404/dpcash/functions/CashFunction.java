package com.darksoldier1404.dpcash.functions;

import com.darksoldier1404.dpcash.obj.CashUser;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;

import static com.darksoldier1404.dpcash.CashPlugin.*;

public class CashFunction {
    public static void giveCash(CommandSender sender, OfflinePlayer player, int amount) {
        if (player == null || !player.hasPlayedBefore()) {
            sender.sendMessage(plugin.getPrefix() + plugin.getLang().get("cash_err_player_not_found"));
            return;
        }
        if (amount <= 0) {
            sender.sendMessage(plugin.getPrefix() + plugin.getLang().get("cash_err_amount_positive"));
            return;
        }
        CashUser user = udata.get(player.getUniqueId());
        if (user == null) {
            sender.sendMessage(plugin.getPrefix() + plugin.getLang().get("cash_err_user_data"));
            return;
        }
        user.setCurrentCash(user.getCurrentCash() + amount);
        user.setTotalCashEarned(user.getTotalCashEarned() + amount);
        CommonFunction.saveUser(user);
        udata.put(player.getUniqueId(), user);
        sender.sendMessage(plugin.getPrefix() + plugin.getLang().getWithArgs("cash_msg_given", String.valueOf(amount), player.getName()));
        if (player.isOnline()) {
            player.getPlayer().sendMessage(plugin.getPrefix() + plugin.getLang().getWithArgs("cash_msg_received", String.valueOf(amount)));
        }
    }
    public static void takeCash(CommandSender sender, OfflinePlayer player, int amount) {
        if (player == null || !player.hasPlayedBefore()) {
            sender.sendMessage(plugin.getPrefix() + plugin.getLang().get("cash_err_player_not_found"));
            return;
        }
        if (amount <= 0) {
            sender.sendMessage(plugin.getPrefix() + plugin.getLang().get("cash_err_amount_positive"));
            return;
        }
        CashUser user = udata.get(player.getUniqueId());
        if (user == null) {
            sender.sendMessage(plugin.getPrefix() + plugin.getLang().get("cash_err_user_data"));
            return;
        }
        if (user.getCurrentCash() < amount) {
            sender.sendMessage(plugin.getPrefix() + plugin.getLang().get("cash_err_not_enough").replace("{player}", player.getName()));
            return;
        }
        user.setCurrentCash(user.getCurrentCash() - amount);
        user.setTotalCashSpent(user.getTotalCashSpent() + amount);
        CommonFunction.saveUser(user);
        udata.put(player.getUniqueId(), user);
        sender.sendMessage(plugin.getPrefix() + plugin.getLang().getWithArgs("cash_msg_taken", String.valueOf(amount), player.getName()));
        if (player.isOnline()) {
            player.getPlayer().sendMessage(plugin.getPrefix() + plugin.getLang().getWithArgs("cash_msg_lost", String.valueOf(amount)));
        }
    }
    public static void giveMileage(CommandSender sender, OfflinePlayer player, int amount) {
        if (player == null || !player.hasPlayedBefore()) {
            sender.sendMessage(plugin.getPrefix() + plugin.getLang().get("cash_err_player_not_found"));
            return;
        }
        if (amount <= 0) {
            sender.sendMessage(plugin.getPrefix() + plugin.getLang().get("cash_err_amount_positive"));
            return;
        }
        CashUser user = udata.get(player.getUniqueId());
        if (user == null) {
            sender.sendMessage(plugin.getPrefix() + plugin.getLang().get("cash_err_user_data"));
            return;
        }
        user.setCurrentMileage(user.getCurrentMileage() + amount);
        user.setTotalMileageEarned(user.getTotalMileageEarned() + amount);
        CommonFunction.saveUser(user);
        udata.put(player.getUniqueId(), user);
        sender.sendMessage(plugin.getPrefix() + plugin.getLang().getWithArgs("mileage_msg_given", String.valueOf(amount), player.getName()));
        if (player.isOnline()) {
            player.getPlayer().sendMessage(plugin.getPrefix() + plugin.getLang().getWithArgs("mileage_msg_received", String.valueOf(amount)));
        }
    }
    public static void takeMileage(CommandSender sender, OfflinePlayer player, int amount) {
        if (player == null || !player.hasPlayedBefore()) {
            sender.sendMessage(plugin.getPrefix() + plugin.getLang().get("cash_err_player_not_found"));
            return;
        }
        if (amount <= 0) {
            sender.sendMessage(plugin.getPrefix() + plugin.getLang().get("cash_err_amount_positive"));
            return;
        }
        CashUser user = udata.get(player.getUniqueId());
        if (user == null) {
            sender.sendMessage(plugin.getPrefix() + plugin.getLang().get("cash_err_user_data"));
            return;
        }
        if (user.getCurrentMileage() < amount) {
            sender.sendMessage(plugin.getPrefix() + plugin.getLang().get("mileage_err_not_enough").replace("{player}", player.getName()));
            return;
        }
        user.setCurrentMileage(user.getCurrentMileage() - amount);
        user.setTotalMileageSpent(user.getTotalMileageSpent() + amount);
        CommonFunction.saveUser(user);
        udata.put(player.getUniqueId(), user);
        sender.sendMessage(plugin.getPrefix() + plugin.getLang().getWithArgs("mileage_msg_taken", String.valueOf(amount), player.getName()));
        if (player.isOnline()) {
            player.getPlayer().sendMessage(plugin.getPrefix() + plugin.getLang().getWithArgs("mileage_msg_lost", String.valueOf(amount)));
        }
    }
    public static void setCash(CommandSender sender, OfflinePlayer player, int amount) {
        if (player == null || !player.hasPlayedBefore()) {
            sender.sendMessage(plugin.getPrefix() + plugin.getLang().get("cash_err_player_not_found"));
            return;
        }
        if (amount < 0) {
            sender.sendMessage(plugin.getPrefix() + plugin.getLang().get("cash_err_amount_zero"));
            return;
        }
        CashUser user = udata.get(player.getUniqueId());
        if (user == null) {
            sender.sendMessage(plugin.getPrefix() + plugin.getLang().get("cash_err_user_data"));
            return;
        }
        user.setCurrentCash(amount);
        CommonFunction.saveUser(user);
        udata.put(player.getUniqueId(), user);
        sender.sendMessage(plugin.getPrefix() + plugin.getLang().getWithArgs("cash_msg_set", player.getName(), String.valueOf(amount)));
        if (player.isOnline()) {
            player.getPlayer().sendMessage(plugin.getPrefix() + plugin.getLang().getWithArgs("cash_msg_set_self", String.valueOf(amount)));
        }
    }
    public static void setMileage(CommandSender sender, OfflinePlayer player, int amount) {
        if (player == null || !player.hasPlayedBefore()) {
            sender.sendMessage(plugin.getPrefix() + plugin.getLang().get("cash_err_player_not_found"));
            return;
        }
        if (amount < 0) {
            sender.sendMessage(plugin.getPrefix() + plugin.getLang().get("cash_err_amount_zero"));
            return;
        }
        CashUser user = udata.get(player.getUniqueId());
        if (user == null) {
            sender.sendMessage(plugin.getPrefix() + plugin.getLang().get("cash_err_user_data"));
            return;
        }
        user.setCurrentMileage(amount);
        CommonFunction.saveUser(user);
        udata.put(player.getUniqueId(), user);
        sender.sendMessage(plugin.getPrefix() + plugin.getLang().getWithArgs("mileage_msg_set", player.getName(), String.valueOf(amount)));
        if (player.isOnline()) {
            player.getPlayer().sendMessage(plugin.getPrefix() + plugin.getLang().getWithArgs("mileage_msg_set_self", String.valueOf(amount)));
        }
    }
    public static void resetCash(CommandSender sender, OfflinePlayer player) {
        if (player == null || !player.hasPlayedBefore()) {
            sender.sendMessage(plugin.getPrefix() + plugin.getLang().get("cash_err_player_not_found"));
            return;
        }
        CashUser user = udata.get(player.getUniqueId());
        if (user == null) {
            sender.sendMessage(plugin.getPrefix() + plugin.getLang().get("cash_err_user_data"));
            return;
        }
        user.setCurrentCash(0);
        user.setTotalCashEarned(0);
        user.setTotalCashSpent(0);
        CommonFunction.saveUser(user);
        udata.put(player.getUniqueId(), user);
        sender.sendMessage(plugin.getPrefix() + plugin.getLang().getWithArgs("cash_msg_reset", player.getName()));
        if (player.isOnline()) {
            player.getPlayer().sendMessage(plugin.getPrefix() + plugin.getLang().getWithArgs("cash_msg_reset_self"));
        }
    }
    public static void resetMileage(CommandSender sender, OfflinePlayer player) {
        if (player == null || !player.hasPlayedBefore()) {
            sender.sendMessage(plugin.getPrefix() + plugin.getLang().get("cash_err_player_not_found"));
            return;
        }
        CashUser user = udata.get(player.getUniqueId());
        if (user == null) {
            sender.sendMessage(plugin.getPrefix() + plugin.getLang().get("cash_err_user_data"));
            return;
        }
        user.setCurrentMileage(0);
        user.setTotalMileageEarned(0);
        user.setTotalMileageSpent(0);
        CommonFunction.saveUser(user);
        udata.put(player.getUniqueId(), user);
        sender.sendMessage(plugin.getPrefix() + plugin.getLang().getWithArgs("mileage_msg_reset", player.getName()));
        if (player.isOnline()) {
            player.getPlayer().sendMessage(plugin.getPrefix() + plugin.getLang().getWithArgs("mileage_msg_reset_self"));
        }
    }
    public static void infoCash(CommandSender sender, OfflinePlayer player) {
        if (player == null || !player.hasPlayedBefore()) {
            sender.sendMessage(plugin.getPrefix() + plugin.getLang().get("cash_err_player_not_found"));
            return;
        }
        CashUser user = udata.get(player.getUniqueId());
        if (user == null) {
            sender.sendMessage(plugin.getPrefix() + plugin.getLang().get("cash_err_user_data"));
            return;
        }
        sender.sendMessage(plugin.getPrefix() + plugin.getLang().getWithArgs("cash_info_title", player.getName()));
        sender.sendMessage(plugin.getPrefix() + plugin.getLang().getWithArgs("cash_info_current", String.valueOf(user.getCurrentCash())));
        sender.sendMessage(plugin.getPrefix() + plugin.getLang().getWithArgs("cash_info_earned", String.valueOf(user.getTotalCashEarned())));
        sender.sendMessage(plugin.getPrefix() + plugin.getLang().getWithArgs("cash_info_spent", String.valueOf(user.getTotalCashSpent())));
    }
    public static void infoMileage(CommandSender sender, OfflinePlayer player) {
        if (player == null || !player.hasPlayedBefore()) {
            sender.sendMessage(plugin.getPrefix() + plugin.getLang().get("cash_err_player_not_found"));
            return;
        }
        CashUser user = udata.get(player.getUniqueId());
        if (user == null) {
            sender.sendMessage(plugin.getPrefix() + plugin.getLang().get("cash_err_user_data"));
            return;
        }
        sender.sendMessage(plugin.getPrefix() + plugin.getLang().getWithArgs("mileage_info_title", player.getName()));
        sender.sendMessage(plugin.getPrefix() + plugin.getLang().getWithArgs("mileage_info_current", String.valueOf(user.getCurrentMileage())));
        sender.sendMessage(plugin.getPrefix() + plugin.getLang().getWithArgs("mileage_info_earned", String.valueOf(user.getTotalMileageEarned())));
        sender.sendMessage(plugin.getPrefix() + plugin.getLang().getWithArgs("mileage_info_spent", String.valueOf(user.getTotalMileageSpent())));
    }
}