package mainapp.applianceClass;

public interface WashCycleControl {
    void startCycle();
    void stopCycle();
    boolean getCycleStatus();
    int getWaterUsage();
}
