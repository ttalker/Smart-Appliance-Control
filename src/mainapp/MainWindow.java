package mainapp;

import java.awt.*;
import javax.swing.*;

import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLightLaf;

public class MainWindow extends JFrame {
    private ApplianceManager manager;
    private boolean isDark = true;
    public MainWindow() {
        manager = new ApplianceManager();
        setTitle("Smart Home Control");
        setSize(900, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setResizable(false);

        // Create tabbed pane
        JTabbedPane tabbedPane = new JTabbedPane();

        // Panel 1
        JPanel panel1 = new JPanel();
        panel1.add(new JLabel("This is Tab 1 panel"));
        tabbedPane.addTab("Tab 1", panel1);

        // Panel 2
        JPanel panel2 = new JPanel();
        panel2.add(new JLabel("This is Tab 2 panel"));
        tabbedPane.addTab("Tab 2", panel2);

        // Theme toggle button
        JButton themeToggle = new JButton("Toggle Light/Dark Mode");
        themeToggle.addActionListener(e -> {
            try {
                if (isDark) {
                    UIManager.setLookAndFeel(new FlatLightLaf());
                } else {
                    UIManager.setLookAndFeel(new FlatDarkLaf());
                }
                SwingUtilities.updateComponentTreeUI(this);
                isDark = !isDark;
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        topPanel.add(themeToggle);

        add(topPanel, BorderLayout.NORTH);
        add(tabbedPane, BorderLayout.CENTER);
        
}
}
