package mainapp.applianceClass;

public class SmartAC extends SmartAppliance implements TemperatureControl, EnergySaver {
    private int temperature;
    private String mode; // "fan" or "cool"
    private boolean energySaving;
    private boolean isOn;

    public SmartAC(String name) {
        super(name);
        this.temperature = 24;
        this.mode = "cool"; // default mode
        this.energySaving = false;
        this.isOn = false;
    }

    public void setMode(String mode) {
        if (mode.equalsIgnoreCase("cool") || mode.equalsIgnoreCase("fan")) {
            this.mode = mode.toLowerCase();
            updateEnergyConsumption();
        }
    }

    @Override
    public String getMode() {
        return this.mode;
    }

    @Override
    public void setTemperature(int temp) {
        this.temperature = temp;
        updateEnergyConsumption();
    }

    @Override
    protected double calculateEnergyConsumption() {
        double base = 1000.0;
        double tempFactor = (24 - temperature) * 50.0;
        double modeFactor = "cool".equals(mode) ? 100.0 : 50.0; 
        double energySaverFactor = energySaving ? -200.0 : 0.0;
        return base + tempFactor + modeFactor + energySaverFactor;
    }

    @Override
    public void displayStatus() {
        System.out.println(getName() + " is " + (isOn() ? "On" : "Off") +
            " | Temp: " + temperature + "°C | Mode: " + mode +
            " | Energy Saver: " + (energySaving ? "ON" : "OFF") +
            " | Energy: " + getEnergyConsumption() + "W");
    }

    public int getTemperature() {
        return temperature;
    }

    // EnergySaver interface implementation
    @Override
    public void enableEnergySavingMode() {
        this.energySaving = true;
        updateEnergyConsumption();
    }

    @Override
    public void disableEnergySavingMode() {
        this.energySaving = false;
        updateEnergyConsumption();
    }

    @Override
    public boolean isEnergySavingMode() {
        return energySaving;
    }

    @Override
    public void turnOn() {
        isOn = true;
        updateEnergyConsumption();
    }

    @Override
    public void turnOff() {
        isOn = false;
        
    }

    public boolean isOn() {
        return isOn;
    }

    @Override
    public String getType() {
        return "AC";
    }

    @Override
    public String toSaveString() {
        return String.format("AC|%s|isOn=%b|temperature=%d|mode=%s|energySaver=%b",
        getName(), isOn(), getTemperature(), getMode(), isEnergySavingMode());
    }
}