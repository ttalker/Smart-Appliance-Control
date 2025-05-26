package mainapp;

public class SmartPlug extends SmartAppliance {
    public SmartPlug(String name) {
        super(name);
    }

    @Override
    protected double calculateEnergyConsumption() {
        // Example: 5W when on, 0W when off
        return isOn() ? 5.0 : 0.0;
    }

    @Override
    public void displayStatus() {
        System.out.println(getName() + " is " + (isOn() ? "On" : "Off") +
                           " | Energy: " + getEnergyConsumption() + "W");
    }
}