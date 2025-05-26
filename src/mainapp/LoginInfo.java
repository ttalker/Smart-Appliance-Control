package mainapp;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import javax.swing.JOptionPane;

public class LoginInfo {
    private String username;
    private String password;

    public LoginInfo(String username, String password) {
        this.username = username;
        this.password = password;
    }

    // for convenience on storing to txt file
    public String toCSV() {
        return username + "," + password;
    }
    // gets the username and password separately
    public static LoginInfo fromCSV(String line) {
        String[] parts = line.split(",");
        return new LoginInfo(parts[0], parts[1]);
    }

    public String getUsername() { return username; }
    public String getPassword() { return password; }

    //check if there is a user from the logins.txt file
    public boolean validateLogin(String username, String password) {
        try {
            for (String line : Files.readAllLines(Paths.get("appdata/logins.txt"))) {
                LoginInfo info = LoginInfo.fromCSV(line);
                if (info.getUsername().equals(username) && info.getPassword().equals(password)) {
                    return true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }


    static void saveToFile(LoginInfo info) {
        try {
            Files.write(Paths.get("logins.txt"), 
                (info.toCSV() + System.lineSeparator()).getBytes(), 
                StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        } catch (IOException ex) {
            ex.printStackTrace();
           JOptionPane.showMessageDialog(null, "Login info saved.");
        }

    }
}

        
