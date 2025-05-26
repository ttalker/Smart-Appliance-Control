package mainapp;

public interface WashCycleControl {
    void startCycle(String mode);
    void stopCycle();
    String getCycleStatus();
    int getWaterUsage();
}
