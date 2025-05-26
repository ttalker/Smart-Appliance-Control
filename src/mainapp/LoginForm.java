package mainapp;
import java.awt.*;
import javax.swing.*;

public class LoginForm extends JFrame{
    private JTextField usernameField;
    private JPasswordField passwordField;

    public LoginForm() {
        setTitle("Login Form");
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

        // Login Button
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        JButton loginButton = new JButton("Login");
        loginButton.putClientProperty("Button.arc", 15);
        panel.add(loginButton, gbc);

        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        JButton signupButton = new JButton("Sign-up");
        loginButton.putClientProperty("Button.arc", 15);
        panel.add(signupButton, gbc);


        add(panel);

        // login button action listener
        loginButton.addActionListener(e -> {
            String user = usernameField.getText().trim();
            String pass = new String(passwordField.getPassword());
            
            LoginInfo currentUser = new LoginInfo(user, pass);
            
            if (currentUser.validateLogin(user,pass)) {
                JOptionPane.showMessageDialog(panel, "Logged in successfully...");
                // call the mainwindow
                SwingUtilities.invokeLater(() -> {
                MainWindow window = new MainWindow();
                dispose();
                window.setVisible(true);
                });

            }
            else{
                JOptionPane.showMessageDialog(panel, "username or password is incorrect!");
            }
        });
        
        // sign up button action listener
         signupButton.addActionListener(e -> {
            SignupForm sign = new SignupForm();
            dispose();
            sign.setVisible(true);
        });
    }
}
