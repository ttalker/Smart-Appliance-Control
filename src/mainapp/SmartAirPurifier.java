package mainapp;

public class SmartAirPurifier extends SmartAppliance {
    private String airQuality = "Good";

    public SmartAirPurifier(String name) {
        super(name);
    }

    public void updateAirQuality(String quality) {
        this.airQuality = quality;
    }

    @Override
    public void displayStatus() {
        System.out.println(getName() + " | Air Quality: " + airQuality + " | Mode: " + getMode());
    }
}
