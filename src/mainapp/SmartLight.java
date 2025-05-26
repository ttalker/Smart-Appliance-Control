package mainapp;

public class SmartLight extends SmartAppliance implements AdjustableLight {
    private int brightness; // 0 - 100
    private String color;   // e.g., "warm", "cool", "blue"

    public SmartLight(String name) {
        super(name);
        brightness = 50;
        color = "white";
    }

    public void setBrightness(int brightness) {
        this.brightness = brightness;
    }

    public void setColor(String color) {
        this.color = color;
    }

    @Override
    public void displayStatus() {
        System.out.println(getName() + " is " + (isOn() ? "On" : "Off") +
                " | Brightness: " + brightness +
                " | Color: " + color);
    }
}