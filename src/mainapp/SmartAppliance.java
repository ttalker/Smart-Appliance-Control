package mainapp;

public abstract class SmartAppliance implements Controls {
    private String name;
    private boolean isOn;
    private double energyConsumption; // in watts
    private String mode;

    public SmartAppliance(String name) {
        this.name = name;
        this.isOn = false;
        this.energyConsumption = 0.0;
        this.mode = "default";
    }

    public String getName() { return name; }
    public boolean isOn() { return isOn; }
    public double getEnergyConsumption() { return energyConsumption; }
    public String getMode() { return mode; }

    protected void setEnergyConsumption(double consumption) {
        this.energyConsumption = consumption >= 0 ? consumption : 0;
    }

    @Override
    public void turnOn() {
        isOn = true;
        updateEnergyConsumption();
    }

    @Override
    public void turnOff() {
        isOn = false;
        energyConsumption = 0.0;
    }

    @Override
    public void changeMode(String mode) {
        if (mode != null && !mode.trim().isEmpty()) {
            this.mode = mode;
            if (isOn) {
                updateEnergyConsumption();
            }
        }
    }

    protected abstract double calculateEnergyConsumption();

    protected void updateEnergyConsumption() {
        if (isOn) {
            energyConsumption = calculateEnergyConsumption();
        } else {
            energyConsumption = 0.0;
        }
    }

    public abstract void displayStatus();
}