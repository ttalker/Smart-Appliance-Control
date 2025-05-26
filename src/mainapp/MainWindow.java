package mainapp;

import java.awt.*;
import javax.swing.*;

public class MainWindow extends JFrame {
    private ApplianceManager manager;

    public MainWindow() {

        setTitle("Smart Home Control");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

        manager = new ApplianceManager();
        initializeAppliances();

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel appliancePanel = new JPanel(new GridLayout(0, 1, 5, 5));
        JScrollPane scrollPane = new JScrollPane(appliancePanel);
        updateAppliancePanel(appliancePanel);

        JPanel controlPanel = new JPanel(new FlowLayout());
        JButton allOnButton = new JButton("All On");
        JButton allOffButton = new JButton("All Off");
        allOnButton.addActionListener(e -> {
            manager.turnAllOn();
            updateAppliancePanel(appliancePanel);
        });
        allOffButton.addActionListener(e -> {
            manager.turnAllOff();
            updateAppliancePanel(appliancePanel);
        });
        controlPanel.add(allOnButton);
        controlPanel.add(allOffButton);

        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(controlPanel, BorderLayout.SOUTH);

        getContentPane().add(mainPanel);
    }

    private void initializeAppliances() {
        manager.addAppliance(new SmartAC("Living Room AC"));
        manager.addAppliance(new SmartLight("Kitchen Light"));
        manager.addAppliance(new SmartPlug("Coffee Maker Plug"));
        manager.addAppliance(new SmartSecurityCam("Front Door Cam"));
        manager.addAppliance(new SmartSecurityLock("Main Door Lock"));
        manager.addAppliance(new SmartRefrigerator("Kitchen Fridge"));
        manager.addAppliance(new SmartWashingMachine("Laundry Washer"));
        manager.addAppliance(new SmartAirPurifier("Bedroom Purifier"));
    }

    private void updateAppliancePanel(JPanel panel) {
        panel.removeAll();
        for (SmartAppliance appliance : manager.getAppliances()) {
            JPanel applianceRow = new JPanel(new FlowLayout(FlowLayout.LEFT));
            JLabel statusLabel = new JLabel(getStatusText(appliance));
            JButton toggleButton = new JButton(appliance.isOn() ? "Turn Off" : "Turn On");
            toggleButton.addActionListener(e -> {
                if (appliance.isOn()) {
                    appliance.turnOff();
                    toggleButton.setText("Turn On");
                } else {
                    appliance.turnOn();
                    toggleButton.setText("Turn Off");
                }
                statusLabel.setText(getStatusText(appliance));
            });
            applianceRow.add(statusLabel);
            applianceRow.add(toggleButton);

            if (appliance instanceof TemperatureControl temperatureControl) {
                JTextField tempField = new JTextField("24", 4);
                JButton setTempButton = new JButton("Set Temp");
                setTempButton.addActionListener(e -> {
                    try {
                        int temp = Integer.parseInt(tempField.getText());
                        if (temp >= -10 && temp <= 40) {
                            temperatureControl.setTemperature(temp);
                            statusLabel.setText(getStatusText(appliance));
                        } else {
                            JOptionPane.showMessageDialog(this, "Temperature must be between -10 and 40°C");
                        }
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(this, "Invalid temperature!");
                    }
                });
                applianceRow.add(tempField);
                applianceRow.add(setTempButton);
            }
            if (appliance instanceof AdjustableLight adjustableLight) {
                JTextField brightnessField = new JTextField("50", 4);
                JButton setBrightnessButton = new JButton("Set Brightness");
                setBrightnessButton.addActionListener(e -> {
                    try {
                        int brightness = Integer.parseInt(brightnessField.getText());
                        if (brightness >= 0 && brightness <= 100) {
                            adjustableLight.setBrightness(brightness);
                            statusLabel.setText(getStatusText(appliance));
                        } else {
                            JOptionPane.showMessageDialog(this, "Brightness must be between 0 and 100");
                        }
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(this, "Invalid brightness!");
                    }
                });
                applianceRow.add(brightnessField);
                applianceRow.add(setBrightnessButton);
            }
            panel.add(applianceRow);
        }
        panel.revalidate();
        panel.repaint();
    }

    private String getStatusText(SmartAppliance appliance) {
        StringBuilder sb = new StringBuilder(appliance.getName() + ": ");
        switch (appliance) {
            case SmartAC ac -> sb.append(ac.isOn() ? "On" : "Off")
                    .append(" | Temp: ").append(ac.getTemperature()).append("°C")
                    .append(" | Mode: ").append(ac.getMode())
                    .append(" | Energy: ").append(String.format("%.1f", ac.getEnergyConsumption())).append("W");
            case SmartLight light -> sb.append(light.isOn() ? "On" : "Off")
                    .append(" | Brightness: ").append(light.getBrightness())
                    .append(" | Color: ").append(light.getColor())
                    .append(" | Energy: ").append(String.format("%.1f", light.getEnergyConsumption())).append("W");
            case SmartPlug plug -> sb.append(plug.isOn() ? "On" : "Off")
                    .append(" | Energy: ").append(String.format("%.1f", plug.getEnergyConsumption())).append("W");
            case SmartSecurityCam cam -> sb.append(cam.isOn() ? "On" : "Off")
                    .append(" | Alerts: ").append(cam.isAlertsEnabled() ? "Enabled" : "Disabled")
                    .append(" | Mode: ").append(cam.getMode())
                    .append(" | Energy: ").append(String.format("%.1f", cam.getEnergyConsumption())).append("W");
            case SmartSecurityLock lock -> sb.append("Lock Status: ").append(lock.isLocked() ? "Locked" : "Unlocked")
                    .append(" | Energy: ").append(String.format("%.1f", lock.getEnergyConsumption())).append("W");
            case SmartRefrigerator fridge -> sb.append(fridge.isOn() ? "On" : "Off")
                    .append(" | Temp: ").append(fridge.getTemperature()).append("°C")
                    .append(" | Energy Saving: ").append(fridge.isEnergySavingMode() ? "Enabled" : "Disabled")
                    .append(" | Inventory: ").append(fridge.getInventory())
                    .append(" | Energy: ").append(String.format("%.1f", fridge.getEnergyConsumption())).append("W");
            case SmartWashingMachine washer -> sb.append("Status: ").append(washer.getCycleStatus())
                    .append(" | Water: ").append(washer.getWaterUsage()).append("L")
                    .append(" | Mode: ").append(washer.getMode())
                    .append(" | Energy: ").append(String.format("%.1f", washer.getEnergyConsumption())).append("W");
            case SmartAirPurifier purifier -> sb.append("Air Quality: ").append(purifier.getAirQuality())
                    .append(" | Mode: ").append(purifier.getMode())
                    .append(" | Energy: ").append(String.format("%.1f", purifier.getEnergyConsumption())).append("W");
            default -> sb.append("Unknown appliance type");
        }
        return sb.toString();
    }
}
}
