package mainapp;

public class SmartAC extends SmartAppliance implements TemperatureControl {
    private int temperature;

    public SmartAC(String name) {
        super(name);
        temperature = 24;
    }

    public void setTemperature(int temp) {
        this.temperature = temp;
    }

    @Override
    public void displayStatus() {
        System.out.println(getName() + " is " + (isOn() ? "On" : "Off") +
                " | Temp: " + temperature + "°C | Mode: " + getMode());
    }
}
