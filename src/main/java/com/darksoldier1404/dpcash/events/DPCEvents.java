package com.darksoldier1404.dpcash.events;

import com.darksoldier1404.dpcash.enums.Currency;
import com.darksoldier1404.dpcash.enums.ShopType;
import com.darksoldier1404.dpcash.functions.CommonFunction;
import com.darksoldier1404.dpcash.functions.ShopFunction;
import com.darksoldier1404.dpcash.obj.Shop;
import com.darksoldier1404.dppc.api.inventory.DInventory;
import com.darksoldier1404.dppc.utils.NBT;
import com.darksoldier1404.dppc.utils.Triple;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

import static com.darksoldier1404.dpcash.CashPlugin.*;

public class DPCEvents implements Listener {
    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        CommonFunction.initUser(e.getPlayer());
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        CommonFunction.saveUserAndQuit(e.getPlayer());
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        if (e.getClickedInventory() == null) return;
        if (e.getClickedInventory().getHolder() == null) return;
        if (e.getClickedInventory().getHolder() instanceof DInventory) {
            DInventory inv = (DInventory) e.getClickedInventory().getHolder();
            Player p = (Player) e.getWhoClicked();
            if (inv.isValidHandler(plugin)) {
                Shop shop = ShopFunction.getShop(inv.getObj().toString());
                ItemStack item = e.getCurrentItem();
                if (item != null && item.getType().isAir()) {
                    return;
                }
                ClickType clickType = e.getClick();
                if (NBT.hasTagKey(item, "dpcash.prevpage")) {
                    inv.applyChanges();
                    inv.prevPage();
                    e.setCancelled(true);
                    return;
                }
                if (NBT.hasTagKey(item, "dpcash.nextpage")) {
                    inv.applyChanges();
                    inv.nextPage();
                    e.setCancelled(true);
                    return;
                }
                if (NBT.hasTagKey(item, "dpcash.clickcancel")) {
                    e.setCancelled(true);
                    return;
                }
                if (inv.isValidChannel(0)) {
                    e.setCancelled(true);
                    if (e.getClickedInventory() != null && e.getClickedInventory().getType() == InventoryType.PLAYER) {
                        return;
                    }
                    switch (shop.getType()) {
                        case CASH:
                            if (clickType == ClickType.LEFT) {
                                ShopFunction.buyItem(p, shop.getName(), inv.getCurrentPage(), e.getSlot(), Currency.CASH);
                            } else if (clickType == ClickType.RIGHT) {
                                ShopFunction.sellItem(p, shop.getName(), inv.getCurrentPage(), e.getSlot(), Currency.CASH);
                            }
                            break;
                        case MILEAGE:
                            if (clickType == ClickType.LEFT) {
                                ShopFunction.buyItem(p, shop.getName(), inv.getCurrentPage(), e.getSlot(), Currency.MILEAGE);
                            } else if (clickType == ClickType.RIGHT) {
                                ShopFunction.sellItem(p, shop.getName(), inv.getCurrentPage(), e.getSlot(), Currency.MILEAGE);
                            }
                            break;
                        case HYBRID:
                            if (clickType == ClickType.LEFT) {
                                ShopFunction.buyItem(p, shop.getName(), inv.getCurrentPage(), e.getSlot(), Currency.CASH);
                            } else if (clickType == ClickType.RIGHT) {
                                ShopFunction.sellItem(p, shop.getName(), inv.getCurrentPage(), e.getSlot(), Currency.CASH);
                            } else if (clickType == ClickType.SHIFT_LEFT) {
                                ShopFunction.buyItem(p, shop.getName(), inv.getCurrentPage(), e.getSlot(), Currency.MILEAGE);
                            } else if (clickType == ClickType.SHIFT_RIGHT) {
                                ShopFunction.sellItem(p, shop.getName(), inv.getCurrentPage(), e.getSlot(), Currency.MILEAGE);
                            }
                            break;
                    }
                    return;
                }
                if (inv.isValidChannel(1)) {
                    return;
                }
                if (inv.isValidChannel(2)) {
                    if (e.getClickedInventory() != null && e.getClickedInventory().getType() != InventoryType.PLAYER) {
                        e.setCancelled(true);
                        if (clickType == ClickType.LEFT) {
                            if (shop.getType() == ShopType.MILEAGE) {
                                p.sendMessage(plugin.getPrefix() + plugin.getLang().getWithArgs("shop_only_mileage_price_setting"));
                                return;
                            }
                            currentEdit.put(p.getUniqueId(), Triple.of(e.getSlot(), inv, Currency.CASH));
                        } else if (clickType == ClickType.RIGHT) {
                            if (shop.getType() == ShopType.CASH) {
                                p.sendMessage(plugin.getPrefix() + plugin.getLang().getWithArgs("shop_only_cash_price_setting"));
                                return;
                            }
                            currentEdit.put(p.getUniqueId(), Triple.of(e.getSlot(), inv, Currency.MILEAGE));
                        } else {
                            p.sendMessage(plugin.getPrefix() + plugin.getLang().getWithArgs("shop_price_setting_click_guide"));
                            return;
                        }
                        p.closeInventory();
                        return;
                    }
                }
            }
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent e) {
        if (e.getInventory().getHolder() instanceof DInventory) {
            Player p = (Player) e.getPlayer();
            DInventory inv = (DInventory) e.getInventory().getHolder();
            if (inv.isValidHandler(plugin)) {
                if (inv.getChannel() == 1) {
                    ShopFunction.saveShopItems(p, inv);
                    return;
                }
            }
        }
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent e) {
        Player p = e.getPlayer();
        if (currentEdit.containsKey(p.getUniqueId())) {
            int slot = currentEdit.get(p.getUniqueId()).getA();
            DInventory inv = currentEdit.get(p.getUniqueId()).getB();
            Currency currency = currentEdit.get(p.getUniqueId()).getC();
            if (inv != null && inv.isValidHandler(plugin) && inv.getChannel() == 2) {
                e.setCancelled(true);
                String shopName = (String) inv.getObj();
                String message = e.getMessage();
                if (message.matches("\\d+:\\d+")) {
                    int buyPrice = Integer.parseInt(message.split(":")[0]);
                    int sellPrice = Integer.parseInt(message.split(":")[1]);
                    if (currency == Currency.CASH) {
                        ShopFunction.setShopCashPrice(inv.getCurrentPage(), shopName, buyPrice, sellPrice, inv.getCurrentPage(), slot);
                    } else if (currency == Currency.MILEAGE) {
                        ShopFunction.setShopMileagePrice(inv.getCurrentPage(), shopName, buyPrice, sellPrice, inv.getCurrentPage(), slot);
                    }
                    p.sendMessage(plugin.getPrefix() + plugin.getLang().getWithArgs("shop_price_set", shopName, String.valueOf(buyPrice), String.valueOf(sellPrice)));
                } else if (message.matches("\\d+")) {
                    int price = Integer.parseInt(message);
                    if (currency == Currency.CASH) {
                        ShopFunction.setShopCashPrice(inv.getCurrentPage(), shopName, price, 0, inv.getCurrentPage(), slot);
                    } else if (currency == Currency.MILEAGE) {
                        ShopFunction.setShopMileagePrice(inv.getCurrentPage(), shopName, price, 0, inv.getCurrentPage(), slot);
                    }
                    p.sendMessage(plugin.getPrefix() + plugin.getLang().getWithArgs("shop_price_set", shopName, String.valueOf(price), "0"));
                } else {
                    p.sendMessage(plugin.getPrefix() + plugin.getLang().getWithArgs("shop_price_setting_number_guide"));
                }
                currentEdit.remove(p.getUniqueId());
                Bukkit.getScheduler().runTask(plugin, () -> ShopFunction.openShopPriceSetting(p, shopName));
            }
        }
    }
}
