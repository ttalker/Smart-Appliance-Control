package mainapp;
import java.awt.*;
import javax.swing.*;

public class LoginForm extends JFrame{
    public LoginForm(){
        setTitle("LOGIN PAGE");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 200);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(30, 30, 30));  // dark background
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Label
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Name:"), gbc);

        // Text Field
        gbc.gridx = 1;
        gbc.gridy = 0;
        JTextField nameField = new JTextField(20);
        nameField.putClientProperty("JTextField.placeholderText", "Enter your name");
        panel.add(nameField, gbc);

        // Label
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel("Password:"), gbc);

        // Text Field
        gbc.gridx = 1;
        gbc.gridy = 1;
        JTextField emailField = new JTextField(20);
        emailField.putClientProperty("JTextField.placeholderText", "Enter your password");
        panel.add(emailField, gbc);

        // Submit Button
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        JButton submit = new JButton("Submit");
        submit.putClientProperty("Button.arc", 20);
        panel.add(submit, gbc);
        add(panel);
    }
}
