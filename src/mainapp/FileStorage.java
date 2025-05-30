package mainapp;

import mainapp.applianceClass.*;

import java.io.*;
import java.util.*;

public class FileStorage {

    private final String filename;

    public FileStorage(String username) {
        // Use the appdata folder at the root of the project
        File appDataDir = new File("appdata");

        // Create the directory if it doesn't exist
        if (!appDataDir.exists()) {
            boolean created = appDataDir.mkdirs();
            if (!created) {
                System.err.println("Failed to create appdata directory.");
            }
        }

        // Full path for the user's file (e.g., appdata/user1.txt)
        File userFile = new File(appDataDir, username + ".txt");
        this.filename = userFile.getAbsolutePath();

        // Create the file if it doesn't exist
        if (!userFile.exists()) {
            try {
                boolean fileCreated = userFile.createNewFile();
                if (!fileCreated) {
                    System.err.println("Failed to create file: " + filename);
                }
            } catch (IOException e) {
                System.err.println("Error creating file: " + filename);
                e.printStackTrace();
            }
        }
    }

    public String getFilename() {
        return filename;
    }

    // Save appliances list to file
    public void saveAll(List<SmartAppliance> appliances) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            for (SmartAppliance a : appliances) {
                writer.write(a.toSaveString());
                writer.newLine();
            }
        }
    }

    public List<SmartAppliance> loadAll() throws IOException {
    List<SmartAppliance> appliances = new ArrayList<>();
    File file = new File(filename);
    if (!file.exists()) return appliances;

    try (BufferedReader br = new BufferedReader(new FileReader(file))) {
        String line;
        while ((line = br.readLine()) != null) {
            String[] parts = line.split("\\|");
            if (parts.length < 2) continue;

            // Trim whitespace from type and name
            String type = parts[0].trim();
            String name = parts[1].trim();

            Map<String, String> params = new HashMap<>();
            for (int i = 2; i < parts.length; i++) {
                String[] kv = parts[i].split("=");
                if (kv.length == 2) {
                    // Trim whitespace from key and value
                    params.put(kv[0].trim(), kv[1].trim());
                }
            }

            SmartAppliance appliance = null;
            // Debug: Print the type being processed with quotes to see whitespace
            System.out.println("Loading appliance type: '" + type + "' (length: " + type.length() + ")");
            
            switch (type) {
                case "AC":
                    SmartAC ac = new SmartAC(name);
                    if (params.containsKey("temperature"))
                        ac.setTemperature(Integer.parseInt(params.get("temperature")));
                    if (params.containsKey("mode"))
                        ac.setMode(params.get("mode"));
                    if (params.containsKey("energySaver")) {
                        if (Boolean.parseBoolean(params.get("energySaver")))
                            ac.enableEnergySavingMode();
                        else
                            ac.disableEnergySavingMode();
                    }
                    if (params.containsKey("isOn")) {
                        if (Boolean.parseBoolean(params.get("isOn"))) {
                            ac.turnOn();
                        } else {
                            ac.turnOff();
                        }
                    }
                    appliance = ac;
                    break;

                case "Light":
                    SmartLight light = new SmartLight(name);
                    if (params.containsKey("brightness"))
                        light.setBrightness(Integer.parseInt(params.get("brightness")));
                    if (params.containsKey("color"))
                        light.setColor(params.get("color"));
                    if (params.containsKey("isOn")) {
                        if (Boolean.parseBoolean(params.get("isOn"))) {
                            light.turnOn();
                        } else {
                            light.turnOff();
                        }
                    }
                    appliance = light;
                    break;

                case "Plug":
                    SmartPlug plug = new SmartPlug(name);
                    if (params.containsKey("isOn")) {
                        if (Boolean.parseBoolean(params.get("isOn"))) {
                            plug.turnOn();
                        } else {
                            plug.turnOff();
                        }
                    }
                    appliance = plug;
                    break;

                case "Door Cam":
                case "SmartSecurityCam": // Alternative match
                    SmartSecurityCam doorCam = new SmartSecurityCam(name);
                    if (params.containsKey("alertsEnabled")) {
                        if (Boolean.parseBoolean(params.get("alertsEnabled"))) {
                            doorCam.enableAlerts();
                        } else {
                            doorCam.disableAlerts();
                        }
                    }
                    if (params.containsKey("isOn")) {
                        if (Boolean.parseBoolean(params.get("isOn"))) {
                            doorCam.turnOn();
                        } else {
                            doorCam.turnOff();
                        }
                    }
                    appliance = doorCam;
                    break;

                case "Door Lock":
                case "SmartSecurityLock": // Alternative match
                    SmartSecurityLock doorLock = new SmartSecurityLock(name);
                    if (params.containsKey("isLocked")) {
                        if (Boolean.parseBoolean(params.get("isLocked"))) {
                            doorLock.lock();
                        } else {
                            doorLock.unlock();
                        }
                    }
                    appliance = doorLock;
                    break;

                case "Fridge":
                case "SmartRefrigerator": // Alternative match
                    SmartRefrigerator fridge = new SmartRefrigerator(name);
                    if (params.containsKey("temperature"))
                        fridge.setTemperature(Integer.parseInt(params.get("temperature")));
                    if (params.containsKey("isOn")) {
                        if (Boolean.parseBoolean(params.get("isOn"))) {
                            fridge.turnOn();
                        } else {
                            fridge.turnOff();
                        }
                    }
                    appliance = fridge;
                    break;

                case "Laundry Washer":
                case "SmartWashingMachine": // Alternative match
                    SmartWashingMachine washer = new SmartWashingMachine(name);
                    // Check for both possible parameter names
                    if (params.containsKey("isRunning")) {
                        if (Boolean.parseBoolean(params.get("isRunning"))) {
                            washer.startCycle();
                        } else {
                            washer.stopCycle();
                        }
                    } else if (params.containsKey("cycleStatus")) {
                        if (Boolean.parseBoolean(params.get("cycleStatus"))) {
                            washer.startCycle();
                        } else {
                            washer.stopCycle();
                        }
                    }
                    appliance = washer;
                    break;

                case "Air Purifier":
                case "SmartAirPurifier": // Alternative match
                    SmartAirPurifier airPurifier = new SmartAirPurifier(name);
                    if (params.containsKey("fanSpeed"))
                        airPurifier.setFanSpeed(Integer.parseInt(params.get("fanSpeed")));
                    // Note: airQuality is typically read-only, so we don't set it during loading
                    if (params.containsKey("isOn")) {
                        if (Boolean.parseBoolean(params.get("isOn"))) {
                            airPurifier.turnOn();
                        } else {
                            airPurifier.turnOff();
                        }
                    }
                    appliance = airPurifier;
                    break;

                default:
                    System.out.println("Unknown appliance type: '" + type + "' (length: " + type.length() + ")");
                    // Print each character to debug encoding issues
                    for (int i = 0; i < type.length(); i++) {
                        System.out.println("  Char " + i + ": '" + type.charAt(i) + "' (code: " + (int)type.charAt(i) + ")");
                    }
            }

            if (appliance != null) {
                appliances.add(appliance);
                System.out.println("Successfully loaded: " + appliance.getClass().getSimpleName() + " - " + name);
            } else {
                System.out.println("Failed to create appliance for type: '" + type + "'");
            }
        }
    }

    return appliances;
}


private String getApplianceTypeString(SmartAppliance appliance) {
    if (appliance instanceof SmartAC) return "AC";
    if (appliance instanceof SmartLight) return "Light";
    if (appliance instanceof SmartPlug) return "Plug";
    if (appliance instanceof SmartSecurityCam) return "Door Cam";
    if (appliance instanceof SmartSecurityLock) return "Door Lock";
    if (appliance instanceof SmartRefrigerator) return "Fridge";
    if (appliance instanceof SmartWashingMachine) return "Laundry Washer";
    if (appliance instanceof SmartAirPurifier) return "Air Purifier";
    return "Unknown";
}

public void removeAppliance(SmartAppliance applianceToRemove) throws IOException {
    String applianceType = getApplianceTypeString(applianceToRemove);
    String applianceName = applianceToRemove.getName();
    
    // Use the direct file manipulation method
    removeApplianceFromFile(applianceName, applianceType);
}

    public void removeApplianceFromFile(String applianceName, String applianceType) throws IOException {
        File file = new File(filename);
        if (!file.exists()) {
            System.out.println("File does not exist: " + filename);
            return;
        }
        
        List<String> lines = new ArrayList<>();
        boolean found = false;
        String targetLineStart = applianceType.trim() + "|" + applianceName.trim() + "|";
        System.out.println("Looking for line starting with: [" + targetLineStart + "]");
        
        // Read all lines and collect those that don't match our target
        System.out.println("Saving to file: " + file.getAbsolutePath());
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                System.out.println("Checking line: [" + line + "]");
                // Check if this line starts with our target appliance
                if (line.startsWith(targetLineStart)) {
                    found = true;
                    System.out.println("Found and removing line: " + line);
                    
                    continue;
                }
                lines.add(line);
                
            }
            
        }
        
        if (!found) {
            System.out.println("Appliance not found in file: " + applianceName + " (" + applianceType + ")");
            return;
        }
        
        // Write all remaining lines back to the file
        try (FileWriter fw = new FileWriter(file, false);
            BufferedWriter writer = new BufferedWriter(fw)) {
            for (String line : lines) {
                writer.write(line);
                writer.newLine();
            }
            writer.flush();
            System.out.println("Write operation complete.");
        }
        System.out.println("Verifying file contents after write:");
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                System.out.println(line);
            }
        }
        
        System.out.println("Successfully removed appliance from file: " + applianceName + " (" + applianceType + ")");
    }

    // Alternative method that removes by exact line match
    public void removeExactLineFromFile(String exactLine) throws IOException {
        File file = new File(filename);
        if (!file.exists()) {
            System.out.println("File does not exist: " + filename);
            return;
        }
        
        List<String> lines = new ArrayList<>();
        boolean found = false;
        
        // Read all lines and collect those that don't match our target
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.equals(exactLine)) {
                    found = true;
                    System.out.println("Found and removing exact line: " + line);
                    // Skip this line (don't add it to the list)
                    continue;
                }
                lines.add(line);
            }
        }
        
        if (!found) {
            System.out.println("Exact line not found in file: " + exactLine);
            return;
        }
        
        // Write all remaining lines back to the file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            for (String line : lines) {
                writer.write(line);
                writer.newLine();
            }
        }
        
        System.out.println("Successfully removed exact line from file");
    }

}