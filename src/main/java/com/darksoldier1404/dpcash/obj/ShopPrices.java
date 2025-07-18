package com.darksoldier1404.dpcash.obj;

public class ShopPrices {
    private int page;
    private int slot;
    private int cashBuyPrice;
    private int cashSellPrice;
    private int mileageBuyPrice;
    private int mileageSellPrice;

    public ShopPrices(int page, int slot, int cashBuyPrice, int cashSellPrice) {
        this.page = page;
        this.slot = slot;
        this.cashBuyPrice = cashBuyPrice;
        this.cashSellPrice = cashSellPrice;
        this.mileageBuyPrice = 0;
        this.mileageSellPrice = 0;
    }

    public ShopPrices(int page, int slot, int cashBuyPrice, int cashSellPrice, int mileageBuyPrice, int mileageSellPrice) {
        this.page = page;
        this.slot = slot;
        this.cashBuyPrice = cashBuyPrice;
        this.cashSellPrice = cashSellPrice;
        this.mileageBuyPrice = mileageBuyPrice;
        this.mileageSellPrice = mileageSellPrice;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getSlot() {
        return slot;
    }

    public void setSlot(int slot) {
        this.slot = slot;
    }

    public int getCashBuyPrice() {
        return cashBuyPrice;
    }

    public void setCashBuyPrice(int cashBuyPrice) {
        this.cashBuyPrice = cashBuyPrice;
    }

    public int getCashSellPrice() {
        return cashSellPrice;
    }

    public void setCashSellPrice(int cashSellPrice) {
        this.cashSellPrice = cashSellPrice;
    }

    public int getMileageBuyPrice() {
        return mileageBuyPrice;
    }

    public void setMileageBuyPrice(int mileageBuyPrice) {
        this.mileageBuyPrice = mileageBuyPrice;
    }

    public int getMileageSellPrice() {
        return mileageSellPrice;
    }

    public void setMileageSellPrice(int mileageSellPrice) {
        this.mileageSellPrice = mileageSellPrice;
    }
}
