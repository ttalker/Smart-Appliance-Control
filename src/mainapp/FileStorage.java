package mainapp;

import mainapp.applianceClass.*;

import java.io.*;
import java.util.*;

public class FileStorage {

    private final String filename;

    public FileStorage(String username) {
        // Filename depends on user, e.g. user1.txt
        this.filename = username + ".txt";
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

    // Load appliances list from file
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
                        if (params.containsKey("energySaver"))
                            if (Boolean.parseBoolean(params.get("energySaver"))) ac.enableEnergySavingMode();
                            else ac.disableEnergySavingMode();

                        appliance = ac;
                        break;

                    // Add cases for other appliance types here, e.g.:
                    // case "Light":
                    //     appliance = new SmartLight(name);
                    //     // set properties ...
                    //     break;

                    default:
                        // Unknown appliance type, skip or create generic
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