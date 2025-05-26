package mainapp;

public class SmartSecurityCam extends SmartAppliance implements AlertSystem {
    private boolean alertsEnabled;

    public SmartSecurityCam(String name) {
        super(name);
        this.alertsEnabled = false;
    }

    public void toggleAlerts() {
        this.alertsEnabled = !alertsEnabled;
    }

    public boolean isAlertsEnabled() {
        return alertsEnabled;
    }

    @Override
    public void enableAlerts(){
        this.alertsEnabled = true;
    }
    
    @Override
    public void disableAlerts(){
        this.alertsEnabled = false;
    }
    
    @Override
    public void displayStatus() {
        System.out.println(getName() + " is " + (isOn() ? "On" : "Off") +
                " | Alerts: " + (alertsEnabled ? "Enabled" : "Disabled") +
                " | Mode: " + getMode());
    }
}