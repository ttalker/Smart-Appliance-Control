package mainapp;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.util.HashMap;
import java.util.Map;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.event.*;

import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLightLaf;
import com.formdev.flatlaf.intellijthemes.FlatDarkFlatIJTheme;
import com.formdev.flatlaf.intellijthemes.FlatLightFlatIJTheme;

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
    
    
    // Colors for theming(JeemUpdate)
    private Color lightSidebarBg = new Color(250, 251, 252);
    private Color darkSidebarBg = new Color(30, 35, 42);
    private Color lightMainBg = new Color(255, 255, 255);
    private Color darkMainBg = new Color(24, 28, 33);
    private Color accentColor = new Color(79, 70, 229); // Professional indigo
    private Color lightBorderColor = new Color(229, 231, 235);
    private Color darkBorderColor = new Color(55, 65, 81);
    
    // appliance attributes 
    private JTextField locationField;
    private JComboBox<String> applianceComboBox;
    private JButton addApplianceButton; 

    // userinfo
    private LoginInfo currentUser;


    public MainWindow(LoginInfo currentUser) {
        this.currentUser = currentUser;
        initializeComponents();
        setupLayout();
        applyTheme();
        setVisible(true);
    }

    private void initializeComponents() {
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

    // creates the topbar (JeemUpdate)
private void createTopBar() {
    topBar = new JPanel();
    topBar.setLayout(new BorderLayout());
    topBar.setPreferredSize(new Dimension(getWidth(), 64));
    topBar.setBorder(BorderFactory.createCompoundBorder(
        BorderFactory.createMatteBorder(0, 0, 1, 0, isDarkMode ? darkBorderColor : lightBorderColor),
        new EmptyBorder(16, 24, 16, 24)
    ));
    
    // App title with better typography
    JLabel titleLabel = new JLabel("Smart Appliances Control");
    titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
    titleLabel.setForeground(isDarkMode ? new Color(243, 244, 246) : new Color(17, 24, 39));
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
    sidebar.setPreferredSize(new Dimension(280, getHeight()));
    sidebar.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, isDarkMode ? darkBorderColor : lightBorderColor));
    
    // Main navigation section
    JPanel navContainer = new JPanel(new BorderLayout());
    navContainer.setBackground(isDarkMode ? darkSidebarBg : lightSidebarBg);
    navContainer.setBorder(new EmptyBorder(32, 0, 32, 0));
    
    // Top navigation buttons
    JPanel topNav = new JPanel();
    topNav.setLayout(new BoxLayout(topNav, BoxLayout.Y_AXIS));
    topNav.setOpaque(false);
    topNav.setBorder(new EmptyBorder(0, 24, 0, 24));
    
    // Navigation section header
    JLabel navHeader = new JLabel("NAVIGATION");
    navHeader.setFont(new Font("Segoe UI", Font.BOLD, 11));
    navHeader.setForeground(isDarkMode ? new Color(156, 163, 175) : new Color(107, 114, 128));
    navHeader.setBorder(new EmptyBorder(0, 16, 16, 0));
    topNav.add(navHeader);
    
    JButton homeBtn = createSidebarButton("🏠 Home", "Home");
    JButton accountBtn = createSidebarButton("👤 Account", "Account");
    JButton addApplianceBtn = createSidebarButton("⚡ Add Appliances", "Appliances");
    JButton logsBtn = createSidebarButton("📜 Logs", "Logs");
    
    topNav.add(homeBtn);
    topNav.add(Box.createVerticalStrut(4));
    topNav.add(accountBtn);
    topNav.add(Box.createVerticalStrut(4));
    topNav.add(addApplianceBtn);
    topNav.add(Box.createVerticalStrut(4));
    topNav.add(logsBtn);
    
    // Bottom navigation section
    JPanel bottomNav = new JPanel();
    bottomNav.setLayout(new BoxLayout(bottomNav, BoxLayout.Y_AXIS));
    bottomNav.setOpaque(false);
    bottomNav.setBorder(new EmptyBorder(0, 24, 0, 24));
    
    // Settings section header
    JLabel settingsHeader = new JLabel("SETTINGS");
    settingsHeader.setFont(new Font("Segoe UI", Font.BOLD, 11));
    settingsHeader.setForeground(isDarkMode ? new Color(156, 163, 175) : new Color(107, 114, 128));
    settingsHeader.setBorder(new EmptyBorder(24, 16, 16, 0));
    bottomNav.add(settingsHeader);
    
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
            System.exit(0);
        }
    });
    
    bottomNav.add(settingsBtn);
    bottomNav.add(Box.createVerticalStrut(4));
    bottomNav.add(moreInfoBtn);
    bottomNav.add(Box.createVerticalStrut(4));
    bottomNav.add(logoutBtn);
    
    navContainer.add(topNav, BorderLayout.NORTH);
    navContainer.add(bottomNav, BorderLayout.SOUTH);
    sidebar.add(navContainer, BorderLayout.CENTER);
    
    // Select home by default
    SwingUtilities.invokeLater(() -> {
        homeBtn.doClick();
    });
}

    private JButton createSidebarButton(String text, String panelName) {
    JButton button = new JButton(text);
    button.setHorizontalAlignment(SwingConstants.LEFT);
    button.setPreferredSize(new Dimension(232, 44));
    button.setMaximumSize(new Dimension(232, 44));
    button.setFocusPainted(false);
    button.setBorderPainted(false);
    button.setContentAreaFilled(false);
    button.setCursor(new Cursor(Cursor.HAND_CURSOR));
    button.setFont(new Font("Segoe UI", Font.BOLD, 14));
    button.setBorder(new EmptyBorder(12, 16, 12, 16));
    button.setForeground(isDarkMode ? new Color(209, 213, 219) : new Color(75, 85, 99));
    
    // Professional hover and selection effects
    button.addMouseListener(new MouseAdapter() {
        @Override
        public void mouseEntered(MouseEvent e) {
            if (selectedButton != button) {
                button.setContentAreaFilled(true);
                button.setBackground(isDarkMode ? new Color(55, 65, 81) : new Color(243, 244, 246));
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
        button.addActionListener(e -> selectProfessionalButton(button, panelName));
    }
    
    return button;
}

private void selectProfessionalButton(JButton button, String panelName) {
    // Update previous selected button
    if (selectedButton != null) {
        selectedButton.setContentAreaFilled(false);
        selectedButton.setBackground(null);
        selectedButton.setForeground(isDarkMode ? new Color(209, 213, 219) : new Color(75, 85, 99));
    }
    
    // Update new selected button with professional styling
    selectedButton = button;
    button.setContentAreaFilled(true);
    button.setBackground(accentColor);
    button.setForeground(Color.WHITE);
    
    // Show corresponding panel
    showPanel(panelName);
}

// Updated updateComponentColors method(JeemUpdate)
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
   
    private JPanel createHomePanel() {
        JPanel panel = new JPanel(new BorderLayout());

        JLabel titleLabel = new JLabel("Current Appliances", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setBorder(new EmptyBorder(0, 0, 30, 0));

        // Appliance container panel (list inside scroll pane)
        JPanel applianceListPanel = new JPanel();
        applianceListPanel.setLayout(new BoxLayout(applianceListPanel, BoxLayout.Y_AXIS));
        applianceListPanel.setOpaque(false);

        // Add many appliances
        String[] appliances = {
            "AC", "Light", "Plug", "Door Cam",
            "Door Lock", "Fridge", "Laundry Washer", "Air Purifier",
            "Hello", "Hello", "1", "2", "3", "4", "5", "6", "7", "8", "9"
        };

        for (String name : appliances) {
            JPanel itemPanel = new JPanel(new BorderLayout());
            itemPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            itemPanel.add(new JLabel(name), BorderLayout.WEST);
            applianceListPanel.add(itemPanel);
        }

        
        JScrollPane scrollPane = new JScrollPane(applianceListPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        scrollPane.getViewport().setOpaque(false);
        scrollPane.setOpaque(false);
        scrollPane.setBorder(null);

        
        applianceListPanel.setPreferredSize(new Dimension(300, appliances.length * 40));

        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    // add helper methods to make 

    private void makeAppliancePanel(SmartAppliance sa){

    }

    private void loadAllAppliances(){



    }



private JPanel createAccountPanel() {
    JPanel panel = new JPanel(new BorderLayout());
    panel.setBackground(isDarkMode ? darkMainBg : lightMainBg);

    // Title with better spacing and typography
    JLabel titleLabel = new JLabel("Account Information");
    titleLabel.setForeground(isDarkMode ? new Color(243, 244, 246) : new Color(17, 24, 39));
    titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
    titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
    titleLabel.setBorder(new EmptyBorder(20, 0, 40, 0));
    panel.add(titleLabel, BorderLayout.NORTH);

    // Main content wrapper with card-like appearance
    JPanel cardPanel = new JPanel(new BorderLayout());
    cardPanel.setBackground(isDarkMode ? new Color(31, 41, 55) : Color.WHITE);
    cardPanel.setBorder(BorderFactory.createCompoundBorder(
        BorderFactory.createLineBorder(isDarkMode ? darkBorderColor : lightBorderColor, 1),
        new EmptyBorder(40, 40, 40, 40)
    ));

    JPanel formPanel = new JPanel(new GridBagLayout());
    formPanel.setBackground(isDarkMode ? new Color(31, 41, 55) : Color.WHITE);
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.insets = new Insets(0, 0, 24, 0);
    gbc.anchor = GridBagConstraints.NORTHWEST;
    gbc.fill = GridBagConstraints.HORIZONTAL;

    String[] labels = {"Username", "Email Address", "Full Name"};
    String[] placeholders = {"Enter your username", "Enter your email address", "Enter your full name"};
    JTextField[] fields = new JTextField[labels.length];

    for (int i = 0; i < labels.length; i++) {
        // Label styling
        gbc.gridx = 0;
        gbc.gridy = i * 2;
        gbc.gridwidth = 1;
        gbc.weightx = 0;
        JLabel label = new JLabel(labels[i]);
        label.setForeground(isDarkMode ? new Color(209, 213, 219) : new Color(55, 65, 81));
        label.setFont(new Font("Segoe UI", Font.BOLD, 14));
        label.setBorder(new EmptyBorder(0, 0, 8, 0));
        formPanel.add(label, gbc);

        // Text field styling
        gbc.gridx = 0;
        gbc.gridy = i * 2 + 1;
        gbc.weightx = 1.0;
        JTextField textField = new JTextField();
        textField.setPreferredSize(new Dimension(400, 44));
        textField.setBackground(isDarkMode ? new Color(17, 24, 39) : new Color(249, 250, 251));
        textField.setForeground(isDarkMode ? new Color(243, 244, 246) : new Color(17, 24, 39));
        textField.setCaretColor(isDarkMode ? new Color(243, 244, 246) : new Color(17, 24, 39));
        textField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(isDarkMode ? new Color(75, 85, 99) : new Color(209, 213, 219), 1),
            new EmptyBorder(12, 16, 12, 16)
        ));
        textField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        
        // Add focus border effect
        textField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                textField.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(accentColor, 2),
                    new EmptyBorder(11, 15, 11, 15)
                ));
            }

            @Override
            public void focusLost(FocusEvent e) {
                textField.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(isDarkMode ? new Color(75, 85, 99) : new Color(209, 213, 219), 1),
                    new EmptyBorder(12, 16, 12, 16)
                ));
            }
        });
        
        addPlaceholder(textField, placeholders[i]);
        formPanel.add(textField, gbc);
        fields[i] = textField;
    }

    cardPanel.add(formPanel, BorderLayout.CENTER);
    
    // Wrapper for card with proper margins
    JPanel wrapperPanel = new JPanel(new BorderLayout());
    wrapperPanel.setBackground(isDarkMode ? darkMainBg : lightMainBg);
    wrapperPanel.setBorder(new EmptyBorder(0, 60, 60, 60));
    wrapperPanel.add(cardPanel, BorderLayout.CENTER);
    
    panel.add(wrapperPanel, BorderLayout.CENTER);
    return panel;
}
//
private void addPlaceholder(JTextField textField, String placeholder) {
    Font normalFont = new Font("Segoe UI", Font.PLAIN, 14);
    Color placeholderColor = isDarkMode ? new Color(107, 114, 128) : new Color(156, 163, 175);
    Color normalColor = isDarkMode ? new Color(243, 244, 246) : new Color(17, 24, 39);

    textField.setForeground(placeholderColor);
    textField.setText(placeholder);

    textField.addFocusListener(new FocusAdapter() {
        @Override
        public void focusGained(FocusEvent e) {
            if (textField.getText().equals(placeholder)) {
                textField.setText("");
                textField.setForeground(normalColor);
            }
        }

        @Override
        public void focusLost(FocusEvent e) {
            if (textField.getText().trim().isEmpty()) {
                textField.setForeground(placeholderColor);
                textField.setText(placeholder);
            }
        }
    });
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
        contentArea.setText("Settings Panel\n\nConfigure your application preferences here.\n\n" +
                           "You can add various settings controls such as:\n" +
                           "• Theme preferences\n" +
                           "• Notification settings\n" +
                           "• Language selection\n" +
                           "• Privacy options\n" +
                           "• Data management");
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
        contentArea.setText("Additional Information\n\nThis panel can contain:\n\n" +
                           "• Help documentation\n" +
                           "• FAQ section\n" +
                           "• Contact information\n" +
                           "• Version information\n" +
                           "• Legal notices\n" +
                           "• Support resources\n\n" +
                           "Customize this content based on your application's needs.");
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

    // Utility method to create custom panels easily
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