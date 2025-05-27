package mainapp.applianceClass;

public class SmartSecurityCam extends SmartAppliance implements AlertSystem {
    private boolean alertsEnabled;

    public SmartSecurityCam(String name) {
        super(name);
        this.alertsEnabled = false;
    }

    @Override
    public void enableAlerts() {
        this.alertsEnabled = true;
        updateEnergyConsumption();
    }

    @Override
    public void disableAlerts() {
        this.alertsEnabled = false;
        updateEnergyConsumption();
    }

    @Override
    public boolean isAlertsEnabled() {
        return alertsEnabled;
    }

    @Override
    protected double calculateEnergyConsumption() {
        // Example: 20W base, +10W when alerts are enabled
        return 20.0 + (alertsEnabled ? 10.0 : 0.0);
    }

    @Override
    public void displayStatus() {
        System.out.println(getName() + " is " + (isOn() ? "On" : "Off") +
                " | Alerts: " + (alertsEnabled ? "Enabled" : "Disabled") +
                " | Mode: " + getMode() +
                " | Energy: " + getEnergyConsumption() + "W");
    }
}