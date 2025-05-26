package mainapp;
import java.awt.*;
import javax.swing.*;

public class SignupForm extends JFrame{
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;

    public SignupForm(){

        setTitle("Signup Form");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(350, 200);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Username Label + Field
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Username:"), gbc);
        gbc.gridx = 1;
        usernameField = new JTextField(15);
        usernameField.putClientProperty("JTextField.placeholderText", "Enter username");
        panel.add(usernameField, gbc);

        // Password Label + Field
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Password:"), gbc);
        gbc.gridx = 1;
        passwordField = new JPasswordField(15);
        passwordField.putClientProperty("JTextField.placeholderText", "Enter password");
        panel.add(passwordField, gbc);

        // Confirm Password Label + Field
        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(new JLabel("Confirm Password:"), gbc);
        gbc.gridx = 1;
        confirmPasswordField = new JPasswordField(15);
        confirmPasswordField.putClientProperty("JTextField.placeholderText", "Confirm password");
        panel.add(confirmPasswordField, gbc);

        // signupButton
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        JButton signupButton = new JButton("Sign-up");
        signupButton.putClientProperty("Button.arc", 15);
        panel.add(signupButton, gbc);
        add(panel);


        String user = usernameField.getText().trim();
        String pass = new String(passwordField.getPassword());
        String confirmPass = new String(confirmPasswordField.getPassword());

        // validate pass and confirmPass

        if (pass == confirmPass) {
            
        } else {
            
        }



    }


}
