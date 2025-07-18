package com.darksoldier1404.dpcash;

import com.darksoldier1404.dpcash.commands.CashCommand;
import com.darksoldier1404.dpcash.commands.CashShopCommand;
import com.darksoldier1404.dpcash.commands.MileageCommand;
import com.darksoldier1404.dpcash.enums.Currency;
import com.darksoldier1404.dpcash.events.DPCEvents;
import com.darksoldier1404.dpcash.functions.CommonFunction;
import com.darksoldier1404.dpcash.obj.CashUser;
import com.darksoldier1404.dpcash.obj.Shop;
import com.darksoldier1404.dppc.api.inventory.DInventory;
import com.darksoldier1404.dppc.lang.DLang;
import com.darksoldier1404.dppc.utils.Triple;
import com.darksoldier1404.dppc.utils.Tuple;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static com.darksoldier1404.dppc.utils.PluginUtil.addPlugin;

public class CashPlugin extends JavaPlugin {
    public static CashPlugin plugin;
    public static YamlConfiguration config;
    public static DLang lang;
    public static String prefix;
    public static final Map<UUID, CashUser> cashUsers = new HashMap<>();
    public static final Map<String, Shop> shops = new HashMap<>();
    public static final Map<UUID, Triple<Integer, DInventory, Currency>> currentEdit = new HashMap<>();

    public static CashPlugin getInstance() {
        return plugin;
    }

    @Override
    public void onLoad() {
        plugin = this;
        addPlugin(plugin, 26291);
        CommonFunction.initPlaceholders();
    }

    @Override
    public void onEnable() {
        CommonFunction.init();
        getServer().getPluginManager().registerEvents(new DPCEvents(), plugin);
        getCommand("cashshop").setExecutor(new CashShopCommand().getExecutor());
        getCommand("cash").setExecutor(new CashCommand().getExecutor());
        getCommand("mileage").setExecutor(new MileageCommand().getExecutor());
    }

    @Override
    public void onDisable() {
        CommonFunction.saveConfig();
    }
}
