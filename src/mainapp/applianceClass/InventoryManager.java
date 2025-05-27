package mainapp.applianceClass;

import java.util.List;

public interface InventoryManager {
    void addFoodItem(String item);
    void removeFoodItem(String item);
    List<String> getInventory();
}
