package mainapp;

public class SmartSecurityLock extends SmartAppliance implements Lockable {
    private boolean isLocked;

    public SmartSecurityLock(String name) {
        super(name);
        this.isLocked = true;
    }

    public void lock() {
        this.isLocked = true;
    }

    public void unlock() {
        this.isLocked = false;
    }

    public boolean isLocked() {
        return isLocked;
    }

    @Override
    protected double calculateEnergyConsumption() {
        // Example: 5W base, +2W when locked
        return 5.0 + (isLocked ? 2.0 : 0.0);
    }

    @Override
    public void displayStatus() {
        System.out.println(getName() + " | Lock Status: " + (isLocked ? "Locked" : "Unlocked") +
                           " | Energy: " + getEnergyConsumption() + "W");
    }
}