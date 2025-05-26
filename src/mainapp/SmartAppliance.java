package mainapp;

public abstract class SmartAppliance implements Controls {
    private String name;
    private boolean isOn;
    private double energyConsumption; // in watts
    private String mode;

    public SmartAppliance(String name) {
        this.name = name;
        this.isOn = false;
        this.energyConsumption = 0;
        this.mode = "default";
    }

    public String getName() { return name; }
    public boolean isOn() { return isOn; }
    public double getEnergyConsumption() { return energyConsumption; }
    public String getMode() { return mode; }

    protected void setEnergyConsumption(double consumption) {
        this.energyConsumption = consumption;
    }

    @Override
    public void turnOn() {
        isOn = true;
    }

    @Override
    public void turnOff() {
        isOn = false;
    }

    @Override
    public void changeMode(String mode) {
        this.mode = mode;
    }

    public abstract void displayStatus(); // For UI/logging
}
