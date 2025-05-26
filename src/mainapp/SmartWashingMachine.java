package mainapp;

public class SmartWashingMachine extends SmartAppliance implements WashCycleControl {
    private String cycleStatus; // "Idle", "Running", "Finished"
    private int waterUsage; // in liters

    public SmartWashingMachine(String name) {
        super(name);
        this.cycleStatus = "Idle";
        this.waterUsage = 0;
    }

    public void startCycle(String mode) {
        if (!isOn()) {
            turnOn();
        }
        cycleStatus = "Running";
        changeMode(mode);
        waterUsage = mode.equalsIgnoreCase("Heavy") ? 100 : 50;
    }

    public void stopCycle() {
        cycleStatus = "Finished";
        turnOff();
    }

    public String getCycleStatus() {
        return cycleStatus;
    }

    public int getWaterUsage() {
        return waterUsage;
    }

    @Override
    public void displayStatus() {
        System.out.println(getName() + " | Status: " + cycleStatus +
                " | Water: " + waterUsage + "L | Mode: " + getMode());
    }
}