package mainapp;
import java.awt.*;
import javax.swing.*;


public class MainWindow extends JFrame {
    
    public MainWindow() {
       setTitle("Main Window");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 800);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(20, 1, 5, 5));
        for (int i = 1; i <= 20; i++) {
            panel.add(new JLabel("Label number " + i));
        }

        JScrollPane scrollPane = new JScrollPane(panel);
        getContentPane().add(scrollPane);
    }
}
