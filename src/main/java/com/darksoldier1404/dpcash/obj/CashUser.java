package com.darksoldier1404.dpcash.obj;

import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;
import java.util.Map;

import com.darksoldier1404.dppc.data.DataCargo;
import org.bukkit.configuration.file.YamlConfiguration;

public class CashUser implements DataCargo {
    private UUID uuid;
    HashMap<Long, CashLog> cashLogs = new HashMap<>();
    private int currentCash;
    private int currentMileage;
    private int totalCashEarned;
    private int totalCashSpent;
    private int totalMileageEarned;
    private int totalMileageSpent;

    public CashUser() {
    }

    public CashUser(UUID uuid) {
        this.uuid = uuid;
        this.currentCash = 0;
        this.currentMileage = 0;
        this.totalCashEarned = 0;
        this.totalCashSpent = 0;
        this.totalMileageEarned = 0;
        this.totalMileageSpent = 0;
    }

    public CashUser(UUID uuid, HashMap<Long, CashLog> cashLogs, int currentCash, int currentMileage, int totalCashEarned, int totalCashSpent, int totalMileageEarned, int totalMileageSpent) {
        this.uuid = uuid;
        this.cashLogs = cashLogs;
        this.currentCash = currentCash;
        this.currentMileage = currentMileage;
        this.totalCashEarned = totalCashEarned;
        this.totalCashSpent = totalCashSpent;
        this.totalMileageEarned = totalMileageEarned;
        this.totalMileageSpent = totalMileageSpent;
    }

    public UUID getUUID() {
        return uuid;
    }

    public int getCurrentCash() {
        return currentCash;
    }

    public void setCurrentCash(int currentCash) {
        this.currentCash = currentCash;
    }

    public int getCurrentMileage() {
        return currentMileage;
    }

    public void setCurrentMileage(int currentMileage) {
        this.currentMileage = currentMileage;
    }

    public int getTotalCashEarned() {
        return totalCashEarned;
    }

    public void setTotalCashEarned(int totalCashEarned) {
        this.totalCashEarned = totalCashEarned;
    }

    public int getTotalCashSpent() {
        return totalCashSpent;
    }

    public void setTotalCashSpent(int totalCashSpent) {
        this.totalCashSpent = totalCashSpent;
    }

    public int getTotalMileageEarned() {
        return totalMileageEarned;
    }

    public void setTotalMileageEarned(int totalMileageEarned) {
        this.totalMileageEarned = totalMileageEarned;
    }

    public int getTotalMileageSpent() {
        return totalMileageSpent;
    }

    public void setTotalMileageSpent(int totalMileageSpent) {
        this.totalMileageSpent = totalMileageSpent;
    }

    public HashMap<Long, CashLog> getCashLogs() {
        return cashLogs;
    }

    public boolean hasEnoughCash(int amount) {
        return amount > 0 && currentCash >= amount;
    }

    public boolean hasEnoughMileage(int amount) {
        return amount > 0 && currentMileage >= amount;
    }

    public void addCash(int amount) {
        if (amount > 0) {
            currentCash += amount;
            totalCashEarned += amount;
        }
    }

    public boolean spendCash(int amount) {
        if (amount > 0 && currentCash >= amount) {
            currentCash -= amount;
            totalCashSpent += amount;
            return true;
        }
        return false;
    }

    public void addMileage(int amount) {
        if (amount > 0) {
            currentMileage += amount;
            totalMileageEarned += amount;
        }
    }

    public boolean spendMileage(int amount) {
        if (amount > 0 && currentMileage >= amount) {
            currentMileage -= amount;
            totalMileageSpent += amount;
            return true;
        }
        return false;
    }

    @Override
    public YamlConfiguration serialize() {
        YamlConfiguration data = new YamlConfiguration();
        data.set("uuid", uuid.toString());
        data.set("currentCash", currentCash);
        data.set("currentMileage", currentMileage);
        data.set("totalCashEarned", totalCashEarned);
        data.set("totalCashSpent", totalCashSpent);
        data.set("totalMileageEarned", totalMileageEarned);
        data.set("totalMileageSpent", totalMileageSpent);

        YamlConfiguration logsSection = new YamlConfiguration();
        for (Map.Entry<Long, CashLog> entry : cashLogs.entrySet()) {
            logsSection.set(entry.getKey().toString(), entry.getValue().serialize());
        }
        data.set("cashLogs", logsSection);
        return data;
    }

    @Override
    public CashUser deserialize(YamlConfiguration data) {
        String suuid = data.getString("uuid");
        if (suuid == null) {
            return null;
        }
        UUID uuid = UUID.fromString(suuid);
        int currentCash = data.getInt("currentCash", 0);
        int currentMileage = data.getInt("currentMileage", 0);
        int totalCashEarned = data.getInt("totalCashEarned", 0);
        int totalCashSpent = data.getInt("totalCashSpent", 0);
        int totalMileageEarned = data.getInt("totalMileageEarned", 0);
        int totalMileageSpent = data.getInt("totalMileageSpent", 0);

        HashMap<Long, CashLog> cashLogs = new HashMap<>();
        Object logsObj = data.get("cashLogs");
        if (logsObj instanceof YamlConfiguration) {
            YamlConfiguration logsSection = (YamlConfiguration) logsObj;
            for (String key : logsSection.getKeys(false)) {
                try {
                    long timestamp = Long.parseLong(key);
                    YamlConfiguration logConf = (YamlConfiguration) logsSection.get(key);
                    CashLog log = CashLog.deserialize(Objects.requireNonNull(logConf));
                    cashLogs.put(timestamp, log);
                } catch (Exception ignored) {
                }
            }
        }
        return new CashUser(uuid, cashLogs, currentCash, currentMileage, totalCashEarned, totalCashSpent, totalMileageEarned, totalMileageSpent);
    }
}