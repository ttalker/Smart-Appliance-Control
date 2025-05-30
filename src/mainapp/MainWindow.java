package mainapp;


import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLightLaf;
import com.formdev.flatlaf.intellijthemes.FlatDarkFlatIJTheme;
import com.formdev.flatlaf.intellijthemes.FlatLightFlatIJTheme;

import java.util.List;


import mainapp.applianceClass.*;

public class MainWindow extends JFrame {
    private boolean isDarkMode = false;
    private JPanel sidebar;
    private JPanel mainContent;
    private JPanel topBar;
    private JButton themeToggleBtn;
    private JButton selectedButton = null;
    private Map<String, JPanel> panels;
    private CardLayout cardLayout;
    
    
    // Colors for theming
    private Color lightSidebarBg = new Color(248, 249, 250);
    private Color darkSidebarBg = new Color(40, 44, 52);
    private Color lightMainBg = Color.WHITE;
    private Color darkMainBg = new Color(33, 37, 43);
    private Color accentColor = new Color(66, 139, 202);
    
    // appliance attributes 
    private JTextField locationField;
    private JComboBox<String> applianceComboBox;
    private JButton addApplianceButton; 

    private final FileStorage storage;
    private final ApplianceManager manager;
    private List<JPanel> appliancePanels = new ArrayList<>();
    private JPanel applianceListPanel;


    // userinfo
    private LoginInfo currentUser;


    public MainWindow(LoginInfo currentUser) throws IOException {
        this.currentUser = currentUser;
        this.manager = new ApplianceManager();
        this.storage = new FileStorage(currentUser.getUsername());
        initializeComponents();
        setupLayout();
        applyTheme();
        setVisible(true);

        
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                int result = JOptionPane.showConfirmDialog(
                    MainWindow.this,
                    "Do you want to save before exiting?",
                    "Exit Confirmation",
                    JOptionPane.YES_NO_OPTION
                );

                if (result == JOptionPane.YES_OPTION) {
                    try {
                        storage.saveAll(manager.getAppliances());
                    } catch (IOException ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(MainWindow.this,
                                "Failed to save data: " + ex.getMessage(),
                                "Save Error", JOptionPane.ERROR_MESSAGE);
                    }
                }

                if (result == JOptionPane.YES_OPTION || result == JOptionPane.NO_OPTION) {
                    System.exit(0);
                }
            }
        });


    }

    private void initializeComponents() throws IOException {
        setTitle("Smart Appliances Control App");
        setSize(1200, 800);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(0, 0));
        
        // Initialize panels map
        panels = new HashMap<>();
        cardLayout = new CardLayout();
        
        // Create components
        createTopBar();
        createSidebar();
        createMainContent();
        
        // Add default panels
        addPanel("Home", createHomePanel());
        addPanel("Account", createAccountPanel());
        addPanel("Settings", createSettingsPanel());
        addPanel("More Info", createMoreInfoPanel());
        addPanel("Appliances", createAddAppliancesPanel());
        addPanel("Logs", createLogsPanel());
    }

    private void setupLayout() {
        add(topBar, BorderLayout.NORTH);
        add(sidebar, BorderLayout.WEST);
        add(mainContent, BorderLayout.CENTER);
    }

    // creates the topbar 
    private void createTopBar() {
        topBar = new JPanel();
        topBar.setLayout(new BorderLayout());
        topBar.setPreferredSize(new Dimension(getWidth(), 50));
        topBar.setBorder(new EmptyBorder(8, 15, 8, 15));
        
        // App title/logo area
        JLabel titleLabel = new JLabel("Smart Appliances Control App");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        topBar.add(titleLabel, BorderLayout.WEST);
        
        // Right panel for theme toggle
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        rightPanel.setOpaque(false);
        
        createThemeToggleButton();
        rightPanel.add(themeToggleBtn);
        
        topBar.add(rightPanel, BorderLayout.EAST);
    }

    private void createThemeToggleButton() {
        themeToggleBtn = new JButton();
        themeToggleBtn.setPreferredSize(new Dimension(40, 32));
        themeToggleBtn.setFocusPainted(false);
        themeToggleBtn.setBorderPainted(false);
        themeToggleBtn.setContentAreaFilled(false);
        themeToggleBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        themeToggleBtn.setToolTipText("Toggle Theme");
        
        // Add hover effect
        themeToggleBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                themeToggleBtn.setContentAreaFilled(true);
                themeToggleBtn.setBackground(isDarkMode ? 
                    new Color(70, 74, 82) : new Color(240, 240, 240));
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                themeToggleBtn.setContentAreaFilled(false);
            }
        });
        
        themeToggleBtn.addActionListener(e -> toggleTheme());
        updateThemeToggleIcon();
    }

    private void updateThemeToggleIcon() {
        String icon = isDarkMode ? "☀" : "🌙";
        themeToggleBtn.setText(icon);
        themeToggleBtn.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 16));
    }

    private void createSidebar() {
        sidebar = new JPanel();
        sidebar.setLayout(new BorderLayout());
        sidebar.setPreferredSize(new Dimension(240, getHeight()));
        sidebar.setBorder(new EmptyBorder(20, 0, 20, 0));
        
        // Top navigation buttons
        JPanel topNav = new JPanel();
        topNav.setLayout(new BoxLayout(topNav, BoxLayout.Y_AXIS));
        topNav.setOpaque(false);
        topNav.setBorder(new EmptyBorder(0, 15, 0, 15));
        
        JButton homeBtn = createSidebarButton("🏠 Home", "Home");
        JButton accountBtn = createSidebarButton("👤 Account", "Account");
        JButton addApplianceBtn = createSidebarButton("⚡ Add Appliances", "Appliances");
        JButton logsBtn = createSidebarButton("📜 Logs", "Logs");
        
        topNav.add(homeBtn);
        topNav.add(Box.createVerticalStrut(8));
        topNav.add(accountBtn);
        topNav.add(Box.createVerticalStrut(8));
        topNav.add(addApplianceBtn);
        topNav.add(Box.createVerticalStrut(8));
        topNav.add(logsBtn);
        
        // Bottom navigation buttons
        JPanel bottomNav = new JPanel();
        bottomNav.setLayout(new BoxLayout(bottomNav, BoxLayout.Y_AXIS));
        bottomNav.setOpaque(false);
        bottomNav.setBorder(new EmptyBorder(0, 15, 0, 15));
        
        JButton settingsBtn = createSidebarButton("⚙️ Settings", "Settings");
        JButton moreInfoBtn = createSidebarButton("ℹ️ More Info", "More Info");
        JButton logoutBtn = createSidebarButton("🚪 Logout", null);
        
        // Add logout functionality
        logoutBtn.addActionListener(e -> {
            int result = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to logout?",
                "Confirm Logout",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
            );
            if (result == JOptionPane.YES_OPTION) {
                try {
                    storage.saveAll(manager.getAppliances());
                    LoginForm login = new LoginForm();
                    login.setVisible(true);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                System.exit(0);
            }
        });
        
        bottomNav.add(settingsBtn);
        bottomNav.add(Box.createVerticalStrut(8));
        bottomNav.add(moreInfoBtn);
        bottomNav.add(Box.createVerticalStrut(8));
        bottomNav.add(logoutBtn);
        
        sidebar.add(topNav, BorderLayout.NORTH);
        sidebar.add(bottomNav, BorderLayout.SOUTH);
        
        // Select home by default
        SwingUtilities.invokeLater(() -> {
            homeBtn.doClick();
        });
    }

    private JButton createSidebarButton(String text, String panelName) {
        JButton button = new JButton(text);
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setPreferredSize(new Dimension(210, 42));
        button.setMaximumSize(new Dimension(210, 42));
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        button.setBorder(new EmptyBorder(8, 16, 8, 16));
        
        // Add hover and selection effects
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (selectedButton != button) {
                    button.setContentAreaFilled(true);
                    button.setBackground(isDarkMode ? 
                        new Color(60, 64, 72) : new Color(240, 242, 245));
                }
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                if (selectedButton != button) {
                    button.setContentAreaFilled(false);
                }
            }
        });
        
        if (panelName != null) {
            button.addActionListener(e -> selectButton(button, panelName));
        }
        
        return button;
    }

    private void selectButton(JButton button, String panelName) {
        // Update previous selected button
        if (selectedButton != null) {
            selectedButton.setContentAreaFilled(false);
            selectedButton.setBackground(null);
            // Reset foreground color to default theme color
            selectedButton.setForeground(null);
        }
        
        // Update new selected button
        selectedButton = button;
        button.setContentAreaFilled(true);
        button.setBackground(accentColor);
        button.setForeground(Color.WHITE);
        
        // Show corresponding panel
        showPanel(panelName);
    }

    private void createMainContent() {
        mainContent = new JPanel(cardLayout);
        mainContent.setBorder(new EmptyBorder(20, 20, 20, 20));
    }

    public void addPanel(String name, JPanel panel) {
        panels.put(name, panel);
        mainContent.add(panel, name);
    }

    public void showPanel(String name) {
        if (panels.containsKey(name)) {
            cardLayout.show(mainContent, name);
        }
    }



    // Default panel creators 
   
    private JPanel createHomePanel() throws IOException {
    JPanel panel = new JPanel(new BorderLayout());
    
    JLabel titleLabel = new JLabel("Current Appliances", SwingConstants.CENTER);
    titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
    titleLabel.setBorder(new EmptyBorder(0, 0, 30, 0));

    // Create appliance list panel
    applianceListPanel = new JPanel();
    applianceListPanel.setLayout(new BoxLayout(applianceListPanel, BoxLayout.Y_AXIS));
    applianceListPanel.setOpaque(true); // Change to true for debugging
    

    // Load appliances
    FileStorage storage = new FileStorage(currentUser.getUsername());
    List<SmartAppliance> loaded = storage.loadAll();

    System.out.println("Total appliances loaded: " + loaded.size());

    for (SmartAppliance a : loaded) {
    manager.addAppliance(a);
    
    try {
        JPanel newPanel = makeAppliancePanel(a);
        if (newPanel != null) {
            applianceListPanel.add(newPanel);
            applianceListPanel.add(Box.createRigidArea(new Dimension(0, 5))); // Consistent spacing
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
}
    // Force layout update
    applianceListPanel.revalidate();
    applianceListPanel.repaint();

    // Create scroll pane
    JScrollPane scrollPane = new JScrollPane(applianceListPanel);
    scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
    scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    scrollPane.setPreferredSize(new Dimension(350, 400));

    // Add to main panel
    panel.add(titleLabel, BorderLayout.NORTH);
    panel.add(scrollPane, BorderLayout.CENTER);

    // Final layout update
    SwingUtilities.invokeLater(() -> {
        panel.revalidate();
        panel.repaint();
    });

    return panel;
}

    // add helper methods to make 

    private JPanel makeApplianceTemplate(String applianceType, String location) {
        // Main horizontal panel
        JPanel appliancePanel = new JPanel(new BorderLayout());
        appliancePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Set consistent sizing for all panels
        appliancePanel.setPreferredSize(new Dimension(300, 80));
        appliancePanel.setMinimumSize(new Dimension(300, 80));
        appliancePanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));
        appliancePanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Left: Appliance label
        JLabel nameLabel = new JLabel(applianceType + " " + location);
        nameLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        appliancePanel.add(nameLabel, BorderLayout.WEST);

        // Right: Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        
        JButton toggleButton = new JButton("On");
        JButton deleteButton = new JButton("Delete");

        buttonPanel.add(toggleButton);
        buttonPanel.add(deleteButton);

        appliancePanel.add(buttonPanel, BorderLayout.EAST);

        // CENTER: Placeholder panel for specific controls later
        JPanel customControlsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        customControlsPanel.setOpaque(false);
        appliancePanel.add(customControlsPanel, BorderLayout.CENTER);

        appliancePanel.putClientProperty("customControls", customControlsPanel);

        deleteButton.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(
                appliancePanel,
                "Are you sure you want to delete this appliance?",
                "Confirm Deletion",
                JOptionPane.YES_NO_OPTION
            );

            if (confirm == JOptionPane.YES_OPTION) {
                // Get the associated appliance from client property
                SmartAppliance associatedAppliance = (SmartAppliance) appliancePanel.getClientProperty("appliance");
                
                // Remove from manager if appliance exists
                if (associatedAppliance != null) {
                    try {
                        storage.removeAppliance(associatedAppliance);
                        manager.removeAppliance(associatedAppliance);
                    } catch (IOException ex) {
                        JOptionPane.showMessageDialog(appliancePanel, 
                            "Error removing appliance: " + ex.getMessage(), 
                            "Error", 
                            JOptionPane.ERROR_MESSAGE);
                        return; // Don't remove from UI if removal from manager failed
                    }
                }
                
                appliancePanels.remove(appliancePanel);
                
                applianceListPanel.remove(appliancePanel);
                applianceListPanel.revalidate();
                applianceListPanel.repaint();
            }
        });
        return appliancePanel;
    }


    private JPanel makeAppliancePanel(SmartAppliance appliance) {
        String currentAppliance;
        String location;

        // Determine appliance type and location
        if (appliance == null) {
            currentAppliance = applianceComboBox.getSelectedItem().toString().trim();
            location = locationField.getText().trim();
        } else {
            // Map class names to switch case values
            String className = appliance.getClass().getSimpleName();
            
            switch (className) {
                case "SmartAC":
                    currentAppliance = "AC";
                    break;
                case "SmartLight":
                    currentAppliance = "Light";
                    break;
                case "SmartPlug":
                    currentAppliance = "Plug";
                    break;
                case "SmartSecurityCam":
                    currentAppliance = "Door Cam";
                    break;
                case "SmartSecurityLock":
                    currentAppliance = "Door Lock";
                    break;
                case "SmartRefrigerator":
                    currentAppliance = "Fridge";
                    break;
                case "SmartWashingMachine":
                    currentAppliance = "Laundry Washer";
                    break;
                case "SmartAirPurifier":
                    currentAppliance = "Air Purifier";
                    break;
                default:
                    return new JPanel(); // Return empty panel for unknown types
            }
            
            // Extract location from appliance name
            // Remove the appliance type suffix to get location
            String applianceName = appliance.getName();
            if (applianceName.endsWith(" " + currentAppliance)) {
                location = applianceName.substring(0, applianceName.length() - (" " + currentAppliance).length());
            } else {
                // Fallback: try to extract location differently
                location = applianceName.replace(currentAppliance, "").trim();
            }
        }

        JPanel newAppliance = new JPanel();

        switch (currentAppliance) {
            case "AC":
                SmartAC ac;
                if (appliance == null) {
                    ac = new SmartAC(location + " AC");
                    manager.addAppliance(ac);
                } else {
                    ac = (SmartAC) appliance;
                }

                newAppliance = makeApplianceTemplate(location, currentAppliance);
                newAppliance.putClientProperty("appliance", ac);

                JPanel buttonPanel = (JPanel) ((BorderLayout) newAppliance.getLayout()).getLayoutComponent(BorderLayout.EAST);
                JButton toggleButton = (JButton) buttonPanel.getComponent(0); 

                toggleButton.setText(ac.isOn() ? "On" : "Off");
                toggleButton.addActionListener(e -> {
                    if (ac.isOn()) {
                        ac.turnOff();
                        toggleButton.setText("Off");
                    } else {
                        ac.turnOn();
                        toggleButton.setText("On");
                    }
                });

                JPanel customControlsPanel = (JPanel) newAppliance.getClientProperty("customControls");
                customControlsPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 0));
                
                // Temperature slider
                JSlider tempSlider = new JSlider(16, 30, ac.getTemperature());
                tempSlider.setMajorTickSpacing(2);
                tempSlider.setPaintTicks(true);
                tempSlider.setPaintLabels(true);
                tempSlider.addChangeListener(e -> {
                    ac.setTemperature(tempSlider.getValue());
                });

                // Mode selector
                String[] modes = {"cool", "fan"};
                JComboBox<String> modeSelector = new JComboBox<>(modes);
                modeSelector.setSelectedItem(ac.getMode());
                modeSelector.addActionListener(e -> {
                    ac.setMode((String) modeSelector.getSelectedItem());
                });

                // Energy Saver checkbox
                JCheckBox energySaverToggle = new JCheckBox("Energy Saver");
                energySaverToggle.setSelected(ac.isEnergySavingMode());
                energySaverToggle.addActionListener(e -> {
                    if (energySaverToggle.isSelected()) {
                        ac.enableEnergySavingMode();
                    } else {
                        ac.disableEnergySavingMode();
                    }
                });

                customControlsPanel.add(new JLabel("Temp:"));
                customControlsPanel.add(tempSlider);
                customControlsPanel.add(new JLabel("Mode:"));
                customControlsPanel.add(modeSelector);
                customControlsPanel.add(energySaverToggle);
                break;

            case "Light":
                SmartLight light;
                if (appliance == null) {
                    light = new SmartLight(location + " Light");
                    manager.addAppliance(light);
                } else {
                    light = (SmartLight) appliance;
                }

                newAppliance = makeApplianceTemplate(location, currentAppliance);
                newAppliance.putClientProperty("appliance", light);

                JPanel lightButtonPanel = (JPanel) ((BorderLayout) newAppliance.getLayout()).getLayoutComponent(BorderLayout.EAST);
                JButton lightToggleButton = (JButton) lightButtonPanel.getComponent(0);

                lightToggleButton.setText(light.isOn() ? "On" : "Off");
                lightToggleButton.addActionListener(e -> {
                    if (light.isOn()) {
                        light.turnOff();
                        lightToggleButton.setText("Off");
                    } else {
                        light.turnOn();
                        lightToggleButton.setText("On");
                    }
                });

                JPanel lightControlsPanel = (JPanel) newAppliance.getClientProperty("customControls");
                lightControlsPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 0));

                // Brightness slider
                JSlider brightnessSlider = new JSlider(0, 100, light.getBrightness());
                brightnessSlider.setMajorTickSpacing(25);
                brightnessSlider.setPaintTicks(true);
                brightnessSlider.setPaintLabels(true);
                brightnessSlider.addChangeListener(e -> {
                    light.setBrightness(brightnessSlider.getValue());
                });

                // Color selector
                String[] colors = {"white", "warm", "cool", "red", "green", "blue"};
                JComboBox<String> colorSelector = new JComboBox<>(colors);
                colorSelector.setSelectedItem(light.getColor());
                colorSelector.addActionListener(e -> {
                    light.setColor((String) colorSelector.getSelectedItem());
                });

                lightControlsPanel.add(new JLabel("Brightness:"));
                lightControlsPanel.add(brightnessSlider);
                lightControlsPanel.add(new JLabel("Color:"));
                lightControlsPanel.add(colorSelector);
                break;

            case "Plug":
                SmartPlug plug;
                if (appliance == null) {
                    plug = new SmartPlug(location + " Plug");
                    manager.addAppliance(plug);
                } else {
                    plug = (SmartPlug) appliance;
                }

                newAppliance = makeApplianceTemplate(location, currentAppliance);
                newAppliance.putClientProperty("appliance", plug);

                JPanel plugButtonPanel = (JPanel) ((BorderLayout) newAppliance.getLayout()).getLayoutComponent(BorderLayout.EAST);
                JButton plugToggleButton = (JButton) plugButtonPanel.getComponent(0);

                plugToggleButton.setText(plug.isOn() ? "On" : "Off");
                plugToggleButton.addActionListener(e -> {
                    if (plug.isOn()) {
                        plug.turnOff();
                        plugToggleButton.setText("Off");
                    } else {
                        plug.turnOn();
                        plugToggleButton.setText("On");
                    }
                });
                break;

            case "Door Cam":
                SmartSecurityCam doorCam;
                if (appliance == null) {
                    doorCam = new SmartSecurityCam(location + " Door Cam");
                    manager.addAppliance(doorCam);
                } else {
                    doorCam = (SmartSecurityCam) appliance;
                }

                newAppliance = makeApplianceTemplate(location, currentAppliance);
                newAppliance.putClientProperty("appliance", doorCam);

                JPanel camButtonPanel = (JPanel) ((BorderLayout) newAppliance.getLayout()).getLayoutComponent(BorderLayout.EAST);
                JButton camToggleButton = (JButton) camButtonPanel.getComponent(0);

                camToggleButton.setText(doorCam.isOn() ? "On" : "Off");
                camToggleButton.addActionListener(e -> {
                    if (doorCam.isOn()) {
                        doorCam.turnOff();
                        camToggleButton.setText("Off");
                    } else {
                        doorCam.turnOn();
                        camToggleButton.setText("On");
                    }
                });

                JPanel camControlsPanel = (JPanel) newAppliance.getClientProperty("customControls");
                camControlsPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 0));

                // Alerts toggle
                JCheckBox alertsToggle = new JCheckBox("Motion Alerts");
                alertsToggle.setSelected(doorCam.isAlertsEnabled());
                alertsToggle.addActionListener(e -> {
                    if (alertsToggle.isSelected()) {
                        doorCam.enableAlerts();
                    } else {
                        doorCam.disableAlerts();
                    }
                });

                camControlsPanel.add(alertsToggle);
                break;

            case "Door Lock":
                SmartSecurityLock doorLock;
                if (appliance == null) {
                    doorLock = new SmartSecurityLock(location + " Door Lock");
                    manager.addAppliance(doorLock);
                } else {
                    doorLock = (SmartSecurityLock) appliance;
                }

                newAppliance = makeApplianceTemplate(location, currentAppliance);
                newAppliance.putClientProperty("appliance", doorLock);

                JPanel lockButtonPanel = (JPanel) ((BorderLayout) newAppliance.getLayout()).getLayoutComponent(BorderLayout.EAST);
                JButton lockToggleButton = (JButton) lockButtonPanel.getComponent(0);

                lockToggleButton.setText(doorLock.isLocked() ? "Locked" : "Unlocked");
                lockToggleButton.addActionListener(e -> {
                    if (doorLock.isLocked()) {
                        doorLock.unlock();
                        lockToggleButton.setText("Unlocked");
                    } else {
                        doorLock.lock();
                        lockToggleButton.setText("Locked");
                    }
                });
                break;

            case "Fridge":
                SmartRefrigerator fridge;
                if (appliance == null) {
                    fridge = new SmartRefrigerator(location + " Fridge");
                    manager.addAppliance(fridge);
                } else {
                    fridge = (SmartRefrigerator) appliance;
                }

                newAppliance = makeApplianceTemplate(location, currentAppliance);
                newAppliance.putClientProperty("appliance", fridge);

                JPanel fridgeButtonPanel = (JPanel) ((BorderLayout) newAppliance.getLayout()).getLayoutComponent(BorderLayout.EAST);
                JButton fridgeToggleButton = (JButton) fridgeButtonPanel.getComponent(0);

                fridgeToggleButton.setText(fridge.isOn() ? "On" : "Off");
                fridgeToggleButton.addActionListener(e -> {
                    if (fridge.isOn()) {
                        fridge.turnOff();
                        fridgeToggleButton.setText("Off");
                    } else {
                        fridge.turnOn();
                        fridgeToggleButton.setText("On");
                    }
                });

                JPanel fridgeControlsPanel = (JPanel) newAppliance.getClientProperty("customControls");
                fridgeControlsPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 0));

                // Temperature slider
                JSlider fridgeTempSlider = new JSlider(-5, 5, fridge.getTemperature());
                fridgeTempSlider.setMajorTickSpacing(2);
                fridgeTempSlider.setPaintTicks(true);
                fridgeTempSlider.setPaintLabels(true);
                fridgeTempSlider.addChangeListener(e -> {
                    fridge.setTemperature(fridgeTempSlider.getValue());
                });

                fridgeControlsPanel.add(new JLabel("Temp:"));
                fridgeControlsPanel.add(fridgeTempSlider);
                break;

            case "Laundry Washer":
                SmartWashingMachine washer;
                if (appliance == null) {
                    washer = new SmartWashingMachine(location + " Laundry Washer");
                    manager.addAppliance(washer);
                } else {
                    washer = (SmartWashingMachine) appliance;
                }

                newAppliance = makeApplianceTemplate(location, currentAppliance);
                newAppliance.putClientProperty("appliance", washer);

                JPanel washerButtonPanel = (JPanel) ((BorderLayout) newAppliance.getLayout()).getLayoutComponent(BorderLayout.EAST);
                JButton washerToggleButton = (JButton) washerButtonPanel.getComponent(0);

                washerToggleButton.setText(washer.getCycleStatus() ? "Running" : "Stopped");
                washerToggleButton.addActionListener(e -> {
                    if (washer.getCycleStatus()) {
                        washer.stopCycle();
                        washerToggleButton.setText("Stopped");
                    } else {
                        washer.startCycle();
                        washerToggleButton.setText("Running");
                    }
                });

                JPanel washerControlsPanel = (JPanel) newAppliance.getClientProperty("customControls");
                washerControlsPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 0));
                break;

            case "Air Purifier":
                SmartAirPurifier airPurifier;
                if (appliance == null) {
                    airPurifier = new SmartAirPurifier(location + " Air Purifier");
                    manager.addAppliance(airPurifier);
                } else {
                    airPurifier = (SmartAirPurifier) appliance;
                }

                newAppliance = makeApplianceTemplate(location, currentAppliance);
                newAppliance.putClientProperty("appliance", airPurifier);

                JPanel purifierButtonPanel = (JPanel) ((BorderLayout) newAppliance.getLayout()).getLayoutComponent(BorderLayout.EAST);
                JButton purifierToggleButton = (JButton) purifierButtonPanel.getComponent(0);

                purifierToggleButton.setText(airPurifier.isOn() ? "On" : "Off");
                purifierToggleButton.addActionListener(e -> {
                    if (airPurifier.isOn()) {
                        airPurifier.turnOff();
                        purifierToggleButton.setText("Off");
                    } else {
                        airPurifier.turnOn();
                        purifierToggleButton.setText("On");
                    }
                });

                JPanel purifierControlsPanel = (JPanel) newAppliance.getClientProperty("customControls");
                purifierControlsPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 0));

                // Air quality display
                JLabel airQualityLabel = new JLabel("Air Quality: " + airPurifier.getAirQuality());
                
                // Fan speed slider
                JSlider fanSpeedSlider = new JSlider(1, 3, airPurifier.getFanSpeed());
                fanSpeedSlider.setMajorTickSpacing(1);
                fanSpeedSlider.setPaintTicks(true);
                fanSpeedSlider.setPaintLabels(true);
                fanSpeedSlider.addChangeListener(e -> {
                    airPurifier.setFanSpeed(fanSpeedSlider.getValue());
                });

                purifierControlsPanel.add(airQualityLabel);
                purifierControlsPanel.add(new JLabel("Fan Speed:"));
                purifierControlsPanel.add(fanSpeedSlider);
                break;
                
            default:
                return new JPanel(); // Return empty panel for unknown types
        }

        // Ensure consistent panel sizing
        newAppliance.setPreferredSize(new Dimension(300, 80));
        newAppliance.setMinimumSize(new Dimension(300, 80));
        newAppliance.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));
        newAppliance.setAlignmentX(Component.LEFT_ALIGNMENT);
        newAppliance.revalidate();

        return newAppliance;
    }


    private JPanel createAccountPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        
        JLabel titleLabel = new JLabel("Account Settings", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setBorder(new EmptyBorder(0, 0, 30, 0));
        
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;
        
        // Add some form elements as example
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Username:"), gbc);
        gbc.gridx = 1;
        formPanel.add(new JLabel(currentUser.getUsername()), gbc);
        
        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(formPanel, BorderLayout.WEST);
        
        return panel;
    }

    private JPanel createAddAppliancesPanel() {
    JPanel panel = new JPanel(new BorderLayout());
    
    JLabel titleLabel = new JLabel("Add Appliances", SwingConstants.CENTER);
    titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
    titleLabel.setBorder(new EmptyBorder(0, 0, 30, 0));
    
    JPanel formPanel = new JPanel(new GridBagLayout());
    formPanel.setOpaque(false);
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.insets = new Insets(10, 10, 10, 10);
    gbc.anchor = GridBagConstraints.WEST;
    
    // Location field
    gbc.gridx = 0; gbc.gridy = 0;
    formPanel.add(new JLabel("Location:"), gbc);
    gbc.gridx = 1;
    locationField = new JTextField(20);
    formPanel.add(locationField, gbc);
    
    // ComboBox for appliances
    gbc.gridx = 0; gbc.gridy = 1;
    formPanel.add(new JLabel("Type of Appliance:"), gbc);

    applianceComboBox = new JComboBox<>();
    String[] appliances = {
        "AC", "Light", "Plug", "Door Cam",
        "Door Lock", "Fridge", "Laundry Washer", "Air Purifier"
    };
    for (String appliance : appliances) {
        applianceComboBox.addItem(appliance);
    }

    gbc.gridx = 1;
    formPanel.add(applianceComboBox, gbc);
    
    // Add button
    gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
    gbc.anchor = GridBagConstraints.CENTER;
    addApplianceButton = new JButton("Add Appliance");
    addApplianceButton.putClientProperty("Button.arc", 15);
    formPanel.add(addApplianceButton, gbc);
    
    // buttonl listener
    addApplianceButton.addActionListener(e ->{
        JPanel appliance = makeAppliancePanel(null);
        appliancePanels.add(appliance);
        applianceListPanel.add(appliance);
        applianceListPanel.revalidate();
        applianceListPanel.repaint();
        JOptionPane.showMessageDialog(panel, "Successfully added appliance to home panel!");
        
    });

    panel.add(titleLabel, BorderLayout.NORTH);
    panel.add(formPanel, BorderLayout.CENTER);
    
    return panel;
}

    private JPanel createLogsPanel(){
        JPanel panel = new JPanel(new BorderLayout());
        
        JLabel titleLabel = new JLabel("Logs", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setBorder(new EmptyBorder(0, 0, 30, 0));
        
        JTextArea contentArea = new JTextArea();
        contentArea.setText("Example Logs");
        contentArea.setEditable(false);
        contentArea.setOpaque(false);
        contentArea.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        contentArea.setLineWrap(true);
        contentArea.setWrapStyleWord(true);
        
        JScrollPane scrollPane = new JScrollPane(contentArea);
        scrollPane.setBorder(null);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        
        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }

    private JPanel createSettingsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        
        JLabel titleLabel = new JLabel("Settings", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setBorder(new EmptyBorder(0, 0, 30, 0));
        
        JTextArea contentArea = new JTextArea();
        contentArea.setText("Settings Panel");
        contentArea.setEditable(false);
        contentArea.setOpaque(false);
        contentArea.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        contentArea.setLineWrap(true);
        contentArea.setWrapStyleWord(true);
        
        JScrollPane scrollPane = new JScrollPane(contentArea);
        scrollPane.setBorder(null);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        
        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }

    private JPanel createMoreInfoPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        
        JLabel titleLabel = new JLabel("More Information", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setBorder(new EmptyBorder(0, 0, 30, 0));
        
        JTextArea contentArea = new JTextArea();
        contentArea.setText("Additional Information");
        contentArea.setEditable(false);
        contentArea.setOpaque(false);
        contentArea.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        contentArea.setLineWrap(true);
        contentArea.setWrapStyleWord(true);
        
        JScrollPane scrollPane = new JScrollPane(contentArea);
        scrollPane.setBorder(null);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        
        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }

    private void toggleTheme() {
        isDarkMode = !isDarkMode;
        applyTheme();
        updateThemeToggleIcon();
    }

    private void applyTheme() {
        try {
            if (isDarkMode) {
                UIManager.setLookAndFeel(new FlatDarkLaf());
                FlatDarkFlatIJTheme.setup();
            } else {
                UIManager.setLookAndFeel(new FlatLightLaf());
                FlatLightFlatIJTheme.setup();
            }
            
            // Update custom colors
            updateComponentColors();
            SwingUtilities.updateComponentTreeUI(this);
            
            // Reselect the current button to maintain selection state
            if (selectedButton != null) {
                selectedButton.setContentAreaFilled(true);
                selectedButton.setBackground(accentColor);
                selectedButton.setForeground(Color.WHITE);
            }
            
            // Ensure all non-selected buttons have proper foreground color
            updateSidebarButtonColors();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateComponentColors() {
        // Update sidebar
        sidebar.setBackground(isDarkMode ? darkSidebarBg : lightSidebarBg);
        
        // Update main content
        mainContent.setBackground(isDarkMode ? darkMainBg : lightMainBg);
        
        
        // Update top bar
        topBar.setBackground(isDarkMode ? darkMainBg : lightMainBg);
    }
    
    private void updateSidebarButtonColors() {
        // Reset foreground colors for all sidebar buttons after theme change
        Component[] components = sidebar.getComponents();
        for (Component comp : components) {
            if (comp instanceof JPanel) {
                resetButtonColorsInPanel((JPanel) comp);
            }
        }
    }
    
    private void resetButtonColorsInPanel(JPanel panel) {
        for (Component comp : panel.getComponents()) {
            if (comp instanceof JButton && comp != selectedButton) {
                JButton btn = (JButton) comp;
                btn.setForeground(null); // Reset to theme default
            } else if (comp instanceof JPanel) {
                resetButtonColorsInPanel((JPanel) comp);
            }
        }
    }

    // for scalability
    public JPanel createCustomPanel(String title, Component content) {
        JPanel panel = new JPanel(new BorderLayout());
        
        if (title != null && !title.isEmpty()) {
            JLabel titleLabel = new JLabel(title, SwingConstants.CENTER);
            titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
            titleLabel.setBorder(new EmptyBorder(0, 0, 30, 0));
            panel.add(titleLabel, BorderLayout.NORTH);
        }
        
        if (content != null) {
            panel.add(content, BorderLayout.CENTER);
        }
        
        return panel;
    }
}