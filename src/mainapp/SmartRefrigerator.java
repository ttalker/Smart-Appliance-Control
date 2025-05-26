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

    @Override
    public void setTemperature(int temp) {
        this.temperature = temp;
        updateEnergyConsumption();
    }

    @Override
    public void enableEnergySavingMode() {
        this.energySavingMode = true;
        updateEnergyConsumption();
    }

    @Override
    public void disableEnergySavingMode() {
        this.energySavingMode = false;
        updateEnergyConsumption();
    }

    @Override
    public boolean isEnergySavingMode() {
        return energySavingMode;
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
        return new ArrayList<>(foodInventory);
    }

    @Override
    protected double calculateEnergyConsumption() {
        // Example: 150W base, +20W per degree below 4°C, -50W if energy saving
        double base = 150.0;
        double tempFactor = (4 - temperature) * 20.0;
        double energySavingFactor = energySavingMode ? -50.0 : 0.0;
        return base + tempFactor + energySavingFactor;
    }

    @Override
    public void displayStatus() {
        System.out.println(getName() + " is " + (isOn() ? "On" : "Off") +
                " | Temp: " + temperature + "°C | Energy Saving: " + (energySavingMode ? "Enabled" : "Disabled") +
                " | Energy: " + getEnergyConsumption() + "W");
        System.out.println("Inventory: " + foodInventory);
    }

    // Getter for temperature (for GUI)
    public int getTemperature() {
        return temperature;
    }
}