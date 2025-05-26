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
    public void displayStatus() {
        System.out.println(getName() + " | Lock Status: " + (isLocked ? "Locked" : "Unlocked"));
    }
}