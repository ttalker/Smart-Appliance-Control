package mainapp.applianceClass;

public class SmartAirPurifier extends SmartAppliance {
    private String airQuality = "Good";
    private int fanSpeed = 1;

    public SmartAirPurifier(String name) {
        super(name);
    }

    public void updateAirQuality(String quality) {
        if (quality != null && !quality.trim().isEmpty()) {
            this.airQuality = quality;
            updateEnergyConsumption();
        }
    }

    @Override
    protected double calculateEnergyConsumption() {
        // Example: 30W base, +10W if air quality is "Poor"
        double base = 30.0;
        double qualityFactor = "Poor".equalsIgnoreCase(airQuality) ? 10.0 : 0.0;
        return base + qualityFactor;
    }

    @Override
    public void displayStatus() {
        System.out.println(getName() + " | Air Quality: " + airQuality + 
                           " | Mode: " + getMode() + 
                           " | Energy: " + getEnergyConsumption() + "W");
    }

    // Getter for GUI compatibility
    public String getAirQuality() {
        return airQuality;
    }

    public int setFanSpeed(int value){
        return fanSpeed = value;
    }
    
    public int getFanSpeed(){
        return fanSpeed;
    }



    @Override
    public String toSaveString() {
        return String.format("Air Purifier|%s|isOn=%b|fanSpeed=%d|airQuality=%s",
            getName(), isOn(), getFanSpeed(), getAirQuality());
    }
}