package com.darksoldier1404.dpcash.obj;

import com.darksoldier1404.dpcash.enums.ShopType;
import com.darksoldier1404.dppc.api.inventory.DInventory;
import com.darksoldier1404.dppc.data.DataCargo;
import com.darksoldier1404.dppc.utils.ConfigUtils;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.Set;

import static com.darksoldier1404.dpcash.CashPlugin.plugin;

public class Shop implements DataCargo {
    private String name;
    private String title;
    private int size;
    private ShopType type;
    private DInventory inventory;
    private Set<ShopPrices> prices = new HashSet<>();

    public Shop() {
    }

    public Shop(String name, String title, int size, ShopType type) {
        this.name = name;
        this.title = title;
        this.size = size;
        this.type = type;
    }

    public Shop(String name, String title, int size, ShopType type, DInventory inventory) {
        this.name = name;
        this.title = title;
        this.size = size;
        this.type = type;
        this.inventory = inventory;
    }

    public Shop(String name, String title, int size, ShopType type, DInventory inventory, Set<ShopPrices> prices) {
        this.name = name;
        this.title = title;
        this.size = size;
        this.type = type;
        this.inventory = inventory;
        this.prices = prices;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public ShopType getType() {
        return type;
    }

    public void setType(ShopType type) {
        this.type = type;
    }

    public DInventory getInventory() {
        return inventory;
    }

    public void setInventory(DInventory inventory) {
        this.inventory = inventory;
    }

    public Set<ShopPrices> getPrices() {
        return prices;
    }

    public void setPrices(Set<ShopPrices> prices) {
        this.prices = prices;
    }

    @Nullable
    public ShopPrices findPrice(int page, int slot) {
        for (ShopPrices price : prices) {
            if (price.getPage() == page && price.getSlot() == slot) {
                return price;
            }
        }
        return null;
    }

    @Nullable
    public ItemStack findItem(int page, int slot) {
        if (inventory != null) {
            ItemStack item = inventory.getPageItems().get(page)[slot];
            if (item != null) {
                return item.clone();
            }
        }
        return null;
    }

    @Override
    public Object serialize() {
        YamlConfiguration data = new YamlConfiguration();
        data.set("name", name);
        data.set("title", title);
        data.set("size", size);
        data.set("type", type.name());
        if (!prices.isEmpty()) {
            for (ShopPrices price : prices) {
                data.set("prices." + price.getPage() + "." + price.getSlot() + ".cashBuyPrice", price.getCashBuyPrice());
                data.set("prices." + price.getPage() + "." + price.getSlot() + ".cashSellPrice", price.getCashSellPrice());
                data.set("prices." + price.getPage() + "." + price.getSlot() + ".mileageBuyPrice", price.getMileageBuyPrice());
                data.set("prices." + price.getPage() + "." + price.getSlot() + ".mileageSellPrice", price.getMileageSellPrice());
            }
        }
        data = inventory.serialize(data);
        ConfigUtils.saveCustomData(plugin, data, name, "shops");
        return data;
    }

    @Override
    public Shop deserialize(YamlConfiguration data) {
        String name = data.getString("name");
        String title = data.getString("title");
        int size = data.getInt("size");
        ShopType type = ShopType.valueOf(data.getString("type").toUpperCase());
        if (name == null || title == null || size <= 0) {
            throw new IllegalArgumentException("Invalid shop data");
        }
        Set<ShopPrices> prices = new HashSet<>();
        if (data.contains("prices")) {
            for (String pageKey : data.getConfigurationSection("prices").getKeys(false)) {
                int page = Integer.parseInt(pageKey);
                for (String slotKey : data.getConfigurationSection("prices." + page).getKeys(false)) {
                    int slot = Integer.parseInt(slotKey);
                    int cashBuyPrice = data.getInt("prices." + page + "." + slot + ".cashBuyPrice", 0);
                    int cashSellPrice = data.getInt("prices." + page + "." + slot + ".cashSellPrice", 0);
                    int mileageBuyPrice = data.getInt("prices." + page + "." + slot + ".mileageBuyPrice", 0);
                    int mileageSellPrice = data.getInt("prices." + page + "." + slot + ".mileageSellPrice", 0);
                    prices.add(new ShopPrices(page, slot, cashBuyPrice, cashSellPrice, mileageBuyPrice, mileageSellPrice));
                }
            }
        }
        DInventory inventory = new DInventory(title, size * 9, true, plugin);
        inventory = inventory.deserialize(data);
        return new Shop(name, title, size, type, inventory, prices);
    }
}
