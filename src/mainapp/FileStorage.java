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

                String type = parts[0];
                String name = parts[1];

                Map<String, String> params = new HashMap<>();
                for (int i = 2; i < parts.length; i++) {
                    String[] kv = parts[i].split("=");
                    if (kv.length == 2) {
                        params.put(kv[0], kv[1]);
                    }
                }

                SmartAppliance appliance = null;
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

                    // Add more appliance types here (e.g., SmartLight, SmartWasher)

                    default:
                        System.out.println("Unknown appliance type: " + type);
                }

                if (appliance != null) {
                    appliances.add(appliance);
                }
            }
        }

        return appliances;
    }
}