package mainapp;

import java.util.ArrayList;
import java.util.List;

public class SmartRefrigerator extends SmartAppliance implements TemperatureControl, EnergySaver, InventoryManager {
    private int temperature; // in °C
    private boolean energySavingMode;
    private ArrayList<String> foodInventory;

    public SmartRefrigerator(String name) {
        super(name);
        this.temperature = 4;
        this.energySavingMode = false;
        this.foodInventory = new ArrayList<>();
    }

    public void setTemperature(int temp) {
        this.temperature = temp;
    }

    public void toggleEnergySavingMode() {
        this.energySavingMode = !energySavingMode;
    }

    @Override
    public void addFoodItem(String item) {
        foodInventory.add(item);
    }

    @Override
    public void removeFoodItem(String item) {
        foodInventory.remove(item);
    }

    @Override
    public List<String> getInventory() {
        return new ArrayList<>(foodInventory); // Return a copy
    }

    @Override
    public void enableEnergySavingMode() {
        this.energySavingMode = true;
    }

    @Override
    public void disableEnergySavingMode() {
        this.energySavingMode = false;
    }

    @Override
    public boolean isEnergySavingMode() {
        return energySavingMode;
    }

    @Override
    public void displayStatus() {
        System.out.println(getName() + " is " + (isOn() ? "On" : "Off") +
                " | Temp: " + temperature + "°C | Energy Saving: " + (energySavingMode ? "Enabled" : "Disabled"));
        System.out.println("Inventory: " + foodInventory);
    }
}
