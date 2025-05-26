package mainapp;

public class SmartAC extends SmartAppliance implements TemperatureControl {
    private int temperature;

    public SmartAC(String name) {
        super(name);
        temperature = 24;
    }

    @Override
    public void setTemperature(int temp) {
        this.temperature = temp;
        updateEnergyConsumption();
    }

    @Override
    protected double calculateEnergyConsumption() {
        // Example: 1000W base, +50W per degree below 24°C, +100W for "cool" mode
        double base = 1000.0;
        double tempFactor = (24 - temperature) * 50.0;
        double modeFactor = "cool".equalsIgnoreCase(getMode()) ? 100.0 : 0.0;
        return base + tempFactor + modeFactor;
    }

    @Override
    public void displayStatus() {
        System.out.println(getName() + " is " + (isOn() ? "On" : "Off") +
                " | Temp: " + temperature + "°C | Mode: " + getMode() +
                " | Energy: " + getEnergyConsumption() + "W");
    }

    // Getter for GUI compatibility
    public int getTemperature() {
        return temperature;
    }
}