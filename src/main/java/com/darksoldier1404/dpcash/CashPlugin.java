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
import com.darksoldier1404.dppc.data.DPlugin;
import com.darksoldier1404.dppc.data.DataContainer;
import com.darksoldier1404.dppc.data.DataType;
import com.darksoldier1404.dppc.utils.Triple;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static com.darksoldier1404.dppc.utils.PluginUtil.addPlugin;

public class CashPlugin extends DPlugin {
    public static CashPlugin plugin;
    public static DataContainer<UUID, CashUser> udata;
    public static DataContainer<String, Shop> shops;
    public static final Map<UUID, Triple<Integer, DInventory, Currency>> currentEdit = new HashMap<>();

    public static CashPlugin getInstance() {
        return plugin;
    }

    public CashPlugin() {
        super(true);
        plugin = this;
    }

    @Override
    public void onLoad() {
        init();
        addPlugin(plugin, 26291);
        udata = loadDataContainer(new DataContainer<UUID, CashUser>(this, DataType.CUSTOM, "users"), CashUser.class);
        shops = loadDataContainer(new DataContainer<String, Shop>(this, DataType.CUSTOM, "shops"), Shop.class);
        CommonFunction.initPlaceholders();
    }

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(new DPCEvents(), plugin);
        getCommand("cashshop").setExecutor(new CashShopCommand().getExecutor());
        getCommand("cash").setExecutor(new CashCommand().getExecutor());
        getCommand("mileage").setExecutor(new MileageCommand().getExecutor());
    }

    @Override
    public void onDisable() {
        saveDataContainer();
    }
}
