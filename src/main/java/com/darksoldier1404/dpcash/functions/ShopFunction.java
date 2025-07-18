package com.darksoldier1404.dpcash.functions;

import com.darksoldier1404.dpcash.enums.Currency;
import com.darksoldier1404.dpcash.enums.ShopType;
import com.darksoldier1404.dpcash.obj.Shop;
import com.darksoldier1404.dpcash.obj.ShopPrices;
import com.darksoldier1404.dppc.api.inventory.DInventory;
import com.darksoldier1404.dppc.utils.InventoryUtils;
import com.darksoldier1404.dppc.utils.NBT;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static com.darksoldier1404.dpcash.CashPlugin.*;

public class ShopFunction {
    public static void createShop(Player p, String shopName, String shopSize, String shopType) {
        if (isShopExists(shopName)) {
            p.sendMessage(prefix + lang.getWithArgs("shop_err_exists", shopName));
            return;
        }
        if (!shopSize.matches("^[0-9]+$")) {
            p.sendMessage(prefix + lang.getWithArgs("shop_err_invalid_size"));
            return;
        }
        int size = Integer.parseInt(shopSize);
        if (size < 2 || size > 6) {
            p.sendMessage(prefix + lang.getWithArgs("shop_err_size_range"));
            return;
        }
        ShopType type = ShopType.valueOf(shopType.toUpperCase());
        YamlConfiguration data = new YamlConfiguration();
        data.set("name", shopName);
        data.set("size", size);
        data.set("type", type.name());
        Shop shop = new Shop(shopName, lang.getWithArgs("shop_title", shopName), size, type);
        shop.setInventory(new DInventory(lang.getWithArgs("shop_title", shopName), size * 9, true, plugin));
        shops.put(shopName, shop);
        saveShops();
        p.sendMessage(prefix + lang.getWithArgs("shop_msg_create_success", shopName, String.valueOf(size), type.name()));
    }

    public static boolean isShopExists(String shopName) {
        return shops.containsKey(shopName);
    }

    public static Shop getShop(String shopName) {
        return shops.get(shopName);
    }

    public static DInventory getShopInventory(String shopName) {
        return shops.get(shopName).getInventory();
    }

    public static void saveShops() {
        for (Shop s : shops.values()) {
            s.save();
        }
    }

    public static void openShop(Player p, String name) {
        if (!isShopExists(name)) {
            p.sendMessage(prefix + lang.getWithArgs("shop_err_not_exist", name));
            return;
        }
        Shop shop = shops.get(name);
        DInventory inv = shop.getInventory().clone();
        inv.updateTitle(shop.getTitle());
        inv.setObj(name);
        inv.setChannel(0);
        inv.setCurrentPage(0);
        inv.applyAllItemChanges(
                item -> applyPlaceholderForPriceSetting(shop, item)
        );
        inv.setPageTools(getPageTools());
        inv.update();
        inv.openInventory(p);
    }

    public static void openShopItemSetting(Player p, String name) {
        if (!isShopExists(name)) {
            p.sendMessage(prefix + lang.getWithArgs("shop_err_not_exist", name));
            return;
        }
        DInventory inv = shops.get(name).getInventory().clone();
        inv.updateTitle(lang.getWithArgs("shop_item_setting_title", name));
        inv.setObj(name);
        inv.setChannel(1);
        inv.setCurrentPage(0);
        inv.setPageTools(getPageTools());
        inv.update();
        inv.openInventory(p);
    }

    public static void saveShopItems(Player p, DInventory inv) {
        String shopName = (String) inv.getObj();
        if (!isShopExists(shopName)) {
            p.sendMessage(prefix + lang.getWithArgs("shop_err_not_exist", shopName));
            return;
        }
        inv.applyChanges();
        Shop shop = shops.get(shopName);
        shop.setInventory(inv);
        shops.put(shopName, shop);
        saveShops();
        p.sendMessage(prefix + lang.getWithArgs("shop_msg_save_success"));
    }

    public static ItemStack[] getPageTools() {
        ItemStack pane = new ItemStack(org.bukkit.Material.BLACK_STAINED_GLASS_PANE);
        ItemMeta meta = pane.getItemMeta();
        meta.setDisplayName(" ");
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        pane.setItemMeta(meta);
        pane = NBT.setStringTag(pane, "dpcash.clickcancel", "true");
        ItemStack nextPage = new ItemStack(org.bukkit.Material.ARROW);
        ItemMeta nextMeta = nextPage.getItemMeta();
        nextMeta.setDisplayName("§aNext Page");
        nextMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        nextPage.setItemMeta(nextMeta);
        nextPage = NBT.setStringTag(NBT.setStringTag(nextPage, "dpcash.clickcancel", "true"), "dpcash.nextpage", "true");
        ItemStack prevPage = new ItemStack(org.bukkit.Material.ARROW);
        ItemMeta prevMeta = prevPage.getItemMeta();
        prevMeta.setDisplayName("§aPrevious Page");
        prevMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        prevPage.setItemMeta(prevMeta);
        prevPage = NBT.setStringTag(NBT.setStringTag(prevPage, "dpcash.clickcancel", "true"), "dpcash.prevpage", "true");
        return new ItemStack[]{pane, prevPage, pane, pane, pane, pane, pane, nextPage, pane};
    }


    public static void openShopPriceSetting(Player p, String name) {
        if (!isShopExists(name)) {
            p.sendMessage(prefix + lang.getWithArgs("shop_err_not_exist", name));
            return;
        }
        Shop shop = shops.get(name);
        DInventory inv = shop.getInventory().clone();
        inv.setObj(name);
        inv.setChannel(2);
        inv.applyAllItemChanges(
                item -> applyPlaceholderForPriceSetting(shop, item)
        );
        inv.setPageTools(getPageTools());
        inv.update();
        inv.openInventory(p);
    }

    public static void applyPlaceholderForPriceSetting(Shop shop, DInventory.PageItemSet set) {
        ItemStack item = set.getItem();
        int page = set.getPage();
        int slot = set.getSlot();
        ShopPrices price = shop.findPrice(page, slot);
        if (item != null && item.getType() != org.bukkit.Material.AIR) {
            ItemMeta meta = item.getItemMeta();
            List<String> lore = item.getItemMeta().hasLore() ? meta.getLore() : new ArrayList<>();
            if (lore != null) {
                if (shop.getType() == ShopType.CASH || shop.getType() == ShopType.HYBRID) {
                    lore.add(lang.getWithArgs("shop_lore_cash_buy", price != null && price.getCashBuyPrice() != 0 ? String.valueOf(price.getCashBuyPrice()) : lang.getWithArgs("shop_lore_cant_buy")));
                    lore.add(lang.getWithArgs("shop_lore_cash_sell", price != null && price.getCashSellPrice() != 0 ? String.valueOf(price.getCashSellPrice()) : lang.getWithArgs("shop_lore_cant_sell")));
                }
                if (shop.getType() == ShopType.MILEAGE || shop.getType() == ShopType.HYBRID) {
                    lore.add(lang.getWithArgs("shop_lore_mileage_buy", price != null && price.getMileageBuyPrice() != 0 ? String.valueOf(price.getMileageBuyPrice()) : lang.getWithArgs("shop_lore_cant_buy")));
                    lore.add(lang.getWithArgs("shop_lore_mileage_sell", price != null && price.getMileageSellPrice() != 0 ? String.valueOf(price.getMileageSellPrice()) : lang.getWithArgs("shop_lore_cant_sell")));
                }
            }
            meta.setLore(lore);
            item.setItemMeta(meta);
        }
    }

    public static void setShopCashPrice(int currentPage, String name, int buyPrice, int sellPrice, int page, int slot) {
        if (!isShopExists(name)) {
            throw new IllegalArgumentException("Shop does not exist: " + name);
        }
        Shop shop = shops.get(name);
        shop.getInventory().setCurrentPage(currentPage);
        ShopPrices price = shop.findPrice(page, slot);
        if (price != null) {
            price.setCashBuyPrice(buyPrice);
            price.setCashSellPrice(sellPrice);
        } else {
            price = new ShopPrices(page, slot, buyPrice, sellPrice);
            shop.getPrices().add(price);
        }
        shops.put(name, shop);
        saveShops();
    }

    public static void setShopMileagePrice(int currentPage, String name, int buyPrice, int sellPrice, int page, int slot) {
        if (!isShopExists(name)) {
            throw new IllegalArgumentException("Shop does not exist: " + name);
        }
        Shop shop = shops.get(name);
        shop.getInventory().setCurrentPage(currentPage);
        ShopPrices price = shop.findPrice(page, slot);
        if (price != null) {
            price.setMileageBuyPrice(buyPrice);
            price.setMileageSellPrice(sellPrice);
        } else {
            price = new ShopPrices(page, slot, 0, 0, buyPrice, sellPrice);
            shop.getPrices().add(price);
        }
        shops.put(name, shop);
        saveShops();
    }

    public static void setShopMaxPage(Player p, String name, String maxPage) {
        if (!isShopExists(name)) {
            p.sendMessage(prefix + lang.getWithArgs("shop_err_not_exist", name));
            return;
        }
        if (!maxPage.matches("^[0-9]+$")) {
            p.sendMessage(prefix + lang.getWithArgs("shop_err_invalid_maxpage"));
            return;
        }
        int page = Integer.parseInt(maxPage);
        if (page < 0) {
            p.sendMessage(prefix + lang.getWithArgs("shop_err_maxpage_range"));
            return;
        }
        Shop shop = shops.get(name);
        shop.getInventory().setPages(page);
        shops.put(name, shop);
        saveShops();
        p.sendMessage(prefix + lang.getWithArgs("shop_msg_maxpage_set", name, String.valueOf(page)));
    }

    @Nullable
    public static ItemStack getShopItem(String shopName, int page, int slot) {
        if (!isShopExists(shopName)) {
            return null;
        }
        Shop shop = shops.get(shopName);
        ItemStack item = shop.findItem(page, slot);
        if (item == null || item.getType() == org.bukkit.Material.AIR) {
            return null;
        }
        return item;
    }

    public static boolean hasEnoughBalance(Player p, String shopName, int page, int slot, Currency currency) {
        if (!isShopExists(shopName)) {
            return false;
        }
        Shop shop = shops.get(shopName);
        ShopPrices price = shop.findPrice(page, slot);
        if (price == null) {
            return false;
        }
        if (currency == Currency.CASH) {
            return cashUsers.get(p.getUniqueId()).getCurrentCash() >= price.getCashBuyPrice();
        } else if (currency == Currency.MILEAGE) {
            return cashUsers.get(p.getUniqueId()).getCurrentMileage() >= price.getMileageBuyPrice();
        }
        return false;
    }

    public static boolean buyItem(Player p, String shopName, int page, int slot, Currency currency) {
        if (!isShopExists(shopName)) {
            p.sendMessage(prefix + lang.getWithArgs("shop_err_not_exist", shopName));
            return false;
        }
        Shop shop = shops.get(shopName);
        ShopPrices price = shop.findPrice(page, slot);
        if (price == null) {
            p.sendMessage(prefix + lang.getWithArgs("shop_err_no_price"));
            return false;
        }
        if (currency == Currency.CASH && price.getCashBuyPrice() <= 0) {
            p.sendMessage(prefix + lang.getWithArgs("shop_err_no_cash_buy"));
            return false;
        }
        if (currency == Currency.MILEAGE && price.getMileageBuyPrice() <= 0) {
            p.sendMessage(prefix + lang.getWithArgs("shop_err_no_mileage_buy"));
            return false;
        }
        ItemStack item = shop.findItem(page, slot);
        if (item == null || item.getType() == org.bukkit.Material.AIR) {
            p.sendMessage(prefix + lang.getWithArgs("shop_err_no_item"));
            return false;
        }
        if (!InventoryUtils.hasEnoughSpace(p.getInventory().getStorageContents(), item)) {
            p.sendMessage(prefix + lang.getWithArgs("shop_err_no_space"));
            return false;
        }
        if (currency == Currency.CASH) {
            if (cashUsers.get(p.getUniqueId()).getCurrentCash() < price.getCashBuyPrice()) {
                p.sendMessage(prefix + lang.getWithArgs("shop_err_no_cash"));
                return false;
            }
            cashUsers.get(p.getUniqueId()).spendCash(price.getCashBuyPrice());
        } else if (currency == Currency.MILEAGE) {
            if (cashUsers.get(p.getUniqueId()).getCurrentMileage() < price.getMileageBuyPrice()) {
                p.sendMessage(prefix + lang.getWithArgs("shop_err_no_mileage"));
                return false;
            }
            cashUsers.get(p.getUniqueId()).spendMileage(price.getMileageBuyPrice());
        }
        p.getInventory().addItem(item.clone());
        p.sendMessage(prefix + lang.getWithArgs("shop_msg_buy", String.valueOf(item.getAmount()), item.getType().name(), shopName));
        return true;
    }

    public static boolean sellItem(Player p, String shopName, int page, int slot, Currency currency) {
        if (!isShopExists(shopName)) {
            p.sendMessage(prefix + lang.getWithArgs("shop_err_not_exist", shopName));
            return false;
        }
        Shop shop = shops.get(shopName);
        ShopPrices price = shop.findPrice(page, slot);
        if (price == null) {
            p.sendMessage(prefix + lang.getWithArgs("shop_err_no_price"));
            return false;
        }
        if (currency == Currency.CASH && price.getCashSellPrice() <= 0) {
            p.sendMessage(prefix + lang.getWithArgs("shop_err_no_cash_sell"));
            return false;
        }
        if (currency == Currency.MILEAGE && price.getMileageSellPrice() <= 0) {
            p.sendMessage(prefix + lang.getWithArgs("shop_err_no_mileage_sell"));
            return false;
        }
        ItemStack item = shop.findItem(page, slot);
        if (item == null || item.getType() == org.bukkit.Material.AIR) {
            p.sendMessage(prefix + lang.getWithArgs("shop_err_no_item"));
            return false;
        }
        if (!p.getInventory().containsAtLeast(item, item.getAmount())) {
            p.sendMessage(prefix + lang.getWithArgs("shop_err_no_have_item"));
            return false;
        }
        if (currency == Currency.CASH) {
            cashUsers.get(p.getUniqueId()).addCash(price.getCashSellPrice());
        } else if (currency == Currency.MILEAGE) {
            cashUsers.get(p.getUniqueId()).addMileage(price.getMileageSellPrice());
        }
        p.getInventory().removeItem(item.clone());
        p.sendMessage(prefix + lang.getWithArgs("shop_msg_sell", String.valueOf(item.getAmount()), item.getType().name(), shopName));
        return true;
    }

    public static void deleteShop(Player p, String name) {
        if (!isShopExists(name)) {
            p.sendMessage(prefix + lang.getWithArgs("shop_err_not_exist", name));
            return;
        }
        new File(plugin.getDataFolder(), "shops/" + name + ".yml").delete();
        shops.remove(name);
        p.sendMessage(prefix + lang.getWithArgs("shop_msg_delete_success", name));
    }
}
