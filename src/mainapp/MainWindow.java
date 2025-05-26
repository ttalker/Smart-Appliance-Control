package mainapp;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.Map;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLightLaf;
import com.formdev.flatlaf.intellijthemes.FlatDarkFlatIJTheme;
import com.formdev.flatlaf.intellijthemes.FlatLightFlatIJTheme;

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
    private Color hoverColor = new Color(76, 149, 212);

    public MainWindow() {
        initializeComponents();
        setupLayout();
        applyTheme();
        setVisible(true);
    }

    private void initializeComponents() {
        setTitle("Modern Themed Application");
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
    }

    private void setupLayout() {
        add(topBar, BorderLayout.NORTH);
        add(sidebar, BorderLayout.WEST);
        add(mainContent, BorderLayout.CENTER);
    }

    private void createTopBar() {
        topBar = new JPanel();
        topBar.setLayout(new BorderLayout());
        topBar.setPreferredSize(new Dimension(getWidth(), 50));
        topBar.setBorder(new EmptyBorder(8, 15, 8, 15));
        
        // App title/logo area
        JLabel titleLabel = new JLabel("Modern App");
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
        
        topNav.add(homeBtn);
        topNav.add(Box.createVerticalStrut(8));
        topNav.add(accountBtn);
        
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

    // Default panel creators - you can customize these or add your own content
    private JPanel createHomePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        
        JLabel titleLabel = new JLabel("Welcome Home", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setBorder(new EmptyBorder(0, 0, 30, 0));
        
        JTextArea contentArea = new JTextArea();
        contentArea.setText("This is the home panel. You can customize this content as needed.\n\n" +
                           "Features of this modern interface:\n" +
                           "• Clean, modern design\n" +
                           "• Dark/Light theme toggle\n" +
                           "• Smooth hover effects\n" +
                           "• Customizable panels\n" +
                           "• Responsive layout");
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
        formPanel.add(new JTextField(20), gbc);
        
        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1;
        formPanel.add(new JTextField(20), gbc);
        
        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(new JLabel("Full Name:"), gbc);
        gbc.gridx = 1;
        formPanel.add(new JTextField(20), gbc);
        
        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(formPanel, BorderLayout.CENTER);
        
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