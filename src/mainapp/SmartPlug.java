package mainapp;

public class SmartPlug extends SmartAppliance {
    public SmartPlug(String name) {
        super(name);
    }

    @Override
    public void displayStatus() {
        System.out.println(getName() + " is " + (isOn() ? "On" : "Off"));
    }
}