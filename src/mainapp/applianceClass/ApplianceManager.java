package mainapp.applianceClass;

import java.util.ArrayList;
import java.util.List;

public class ApplianceManager {
    private final List<SmartAppliance> appliances;

    public ApplianceManager() {
        appliances = new ArrayList<>();
    }

    public void addAppliance(SmartAppliance appliance) {
        appliances.add(appliance);
    }

    public void removeAppliance(SmartAppliance appliance) {
        appliances.remove(appliance);
    }

    public List<SmartAppliance> getAppliances() {
        return new ArrayList<>(appliances);
    }

    public double getTotalEnergyConsumption() {
        return appliances.stream()
                .mapToDouble(SmartAppliance::getEnergyConsumption)
                .sum();
    }

    public void turnAllOn() {
        appliances.forEach(SmartAppliance::turnOn);
    }

    public void turnAllOff() {
        appliances.forEach(SmartAppliance::turnOff);
    }
}
