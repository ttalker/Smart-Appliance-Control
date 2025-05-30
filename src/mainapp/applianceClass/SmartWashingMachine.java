package mainapp.applianceClass;

public class SmartWashingMachine extends SmartAppliance implements WashCycleControl {
    private boolean cycleStatus; // "Idle", "Running", "Finished"
    private int waterUsage; // in liters

    public SmartWashingMachine(String name) {
        super(name);
        this.cycleStatus = false;
        this.waterUsage = 0;
    }

    @Override
    public void startCycle() {
        if (!isOn()) {
            turnOn();
        }
        cycleStatus = true;
        
    }

    @Override
    public void stopCycle() {
        cycleStatus = false;
        turnOff();
    }

    @Override
    public boolean getCycleStatus() {
        return cycleStatus;
    }

    @Override
    public int getWaterUsage() {
        return waterUsage;
    }

    @Override
    protected double calculateEnergyConsumption() {
        // Example: 500W base, +200W for "Heavy" mode
        return 500.0 + ("Heavy".equalsIgnoreCase(getMode()) ? 200.0 : 0.0);
    }

    @Override
    public void displayStatus() {
        System.out.println(getName() + " | Status: " + cycleStatus +
                " | Water: " + waterUsage + "L | Mode: " + getMode() +
                " | Energy: " + getEnergyConsumption() + "W");
    }

    @Override
    public String toSaveString() {
        return String.format("Laundry Washer|%s|isRunning=%b",
            getName(), getCycleStatus());
    }
}