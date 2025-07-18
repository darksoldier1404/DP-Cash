package com.darksoldier1404.dpcash.obj;

import com.darksoldier1404.dpcash.enums.TradeType;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

import java.util.Date;
import java.util.Objects;
import java.util.UUID;

public class CashLog {
    private final TradeType tradeType;
    private final ItemStack item;
    private final Date date;
    private final int price;
    private final UUID traderUUID;
    private final String tradeFrom;

    public CashLog(TradeType tradeType, ItemStack item, Date date, int price, UUID traderUUID, String tradeFrom) {
        this.tradeType = tradeType;
        this.item = item;
        this.date = date;
        this.price = price;
        this.traderUUID = traderUUID;
        this.tradeFrom = tradeFrom;
    }

    public TradeType getTradeType() {
        return tradeType;
    }

    public ItemStack getItem() {
        return item;
    }

    public Date getDate() {
        return date;
    }

    public int getPrice() {
        return price;
    }

    public UUID getTraderUUID() {
        return traderUUID;
    }

    public String getTradeFrom() {
        return tradeFrom;
    }

    public YamlConfiguration serialize() {
        YamlConfiguration log = new YamlConfiguration();
        log.set("tradeType", tradeType.name());
        log.set("item", item.serialize());
        log.set("date", date.getTime());
        log.set("price", price);
        log.set("traderUUID", traderUUID.toString());
        log.set("tradeFrom", tradeFrom);
        return log;
    }

    public static CashLog deserialize(YamlConfiguration log) {
        TradeType tradeType = TradeType.valueOf(log.getString("tradeType"));
        ItemStack item = log.getItemStack("item");
        Date date = new Date(log.getLong("date"));
        int price = log.getInt("price");
        UUID traderUUID = UUID.fromString(Objects.requireNonNull(log.getString("traderUUID")));
        String tradeFrom = log.getString("tradeFrom");
        return new CashLog(tradeType, item, date, price, traderUUID, tradeFrom);
    }
}