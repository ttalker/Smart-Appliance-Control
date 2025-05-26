package mainapp;


import javax.swing.UIManager;
import com.formdev.flatlaf.FlatDarkLaf;

public class Main {
  public static void main(String[] args) {
    System.out.println("some changes");


    try {
          UIManager.setLookAndFeel(new FlatDarkLaf());
        } catch (Exception e) {
            System.err.println("Failed to initialize FlatLaf theme");
            e.printStackTrace();
        }
        
        // Create and show your main window
        LoginForm login = new LoginForm();
        login.setVisible(true);

        
  }

}