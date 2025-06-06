package mainapp.applianceClass;

public class SmartLight extends SmartAppliance implements AdjustableLight {
    private int brightness; // 0 - 100
    private String color;   // e.g., "warm", "cool", "blue"
    private boolean isOn;

    public SmartLight(String name) {
        super(name);
        brightness = 50;
        color = "white";
    }

    @Override
    public void setBrightness(int brightness) {
        if (brightness >= 0 && brightness <= 100) {
            this.brightness = brightness;
            updateEnergyConsumption();
        }
    }

    @Override
    public void setColor(String color) {
        this.color = color;
        updateEnergyConsumption();
    }

    @Override
    protected double calculateEnergyConsumption() {
        // Example: 10W base + 0.5W per brightness level
        return 10.0 + (brightness * 0.5);
    }

    @Override
    public void displayStatus() {
        System.out.println(getName() + " is " + (isOn() ? "On" : "Off") +
                " | Brightness: " + brightness +
                " | Color: " + color +
                " | Energy: " + getEnergyConsumption() + "W");
    }

    // Getters for GUI
    public int getBrightness() {
        return brightness;
    }

    public String getColor() {
        return color;
    }

    public boolean isOn() {
        return isOn;
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
    public String getType() {
        return "Light";
    }

    @Override
    public String toSaveString() {
        return String.format("Light|%s|isOn=%b|brightness=%d|color=%s",
            getName(), isOn(), getBrightness(), getColor());
    }

}