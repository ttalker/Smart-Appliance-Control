package mainapp.applianceClass;

public class SmartWashingMachine extends SmartAppliance implements WashCycleControl {
    private String cycleStatus; // "Idle", "Running", "Finished"
    private int waterUsage; // in liters

    public SmartWashingMachine(String name) {
        super(name);
        this.cycleStatus = "Idle";
        this.waterUsage = 0;
    }

    @Override
    public void startCycle(String mode) {
        if (!isOn()) {
            turnOn();
        }
        cycleStatus = "Running";
        changeMode(mode);
        waterUsage = mode.equalsIgnoreCase("Heavy") ? 100 : 50;
    }

    @Override
    public void stopCycle() {
        cycleStatus = "Finished";
        turnOff();
    }

    @Override
    public String getCycleStatus() {
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
}