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
        setSize(350, 250);
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

        // signupButton
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        JButton loginbutton = new JButton("Return to Login");
        signupButton.putClientProperty("Button.arc", 15);
        panel.add(loginbutton, gbc);
        add(panel);

        

        
        // sign up button action listener
        signupButton.addActionListener(e -> {
            String user = usernameField.getText().trim();
            String pass = new String(passwordField.getPassword());
            String confirmPass = new String(confirmPasswordField.getPassword());
            LoginInfo currentUser = new LoginInfo(user, pass);
            if (user.isEmpty() || pass.isEmpty() || confirmPass.isEmpty()) {
            JOptionPane.showMessageDialog(panel, "Please fill in all fields!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
            }
            
            if (currentUser.validateLogin(user, pass)) {
                JOptionPane.showMessageDialog(panel, "This username and password already exists", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                 if (pass.equals(confirmPass)) {
                
                LoginInfo.saveToFile(currentUser);
                JOptionPane.showMessageDialog(panel, "Signed-up successfully!!!!");

                LoginForm login = new LoginForm();
                dispose();
                login.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(panel, "Passwords do not match!", "Error", JOptionPane.ERROR_MESSAGE);
            }
            }

           
        });

        loginbutton.addActionListener(e ->{
            LoginForm login = new LoginForm();
            dispose();
            login.setVisible(true);
        });

    }
}
