import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class LoginPage extends JPanel {
	private JTextField usernameField;
	private JPasswordField passwordField;
	private MainFrame mainFrame;
	
	/***@wbp.parser.entryPoint*/
	public LoginPage(MainFrame mainFrame) {
		this.mainFrame = mainFrame;
		
        setLayout(new GridLayout(3, 2));
        
        JLabel userLabel = new JLabel("Username: ");
        userLabel.setBounds(21, 29, 95,20);
        add(userLabel);
        usernameField = new JTextField();
        usernameField.setBounds(88,29,288,20);
        add(usernameField);
        
        JLabel passLabel = new JLabel("Password: ");
        passLabel.setBounds(21,60,69,20);
        add(passLabel);
        passwordField = new JPasswordField();
        passwordField.setBounds(88,60,288,20);
        add(passwordField);
        
        JButton loginButton = new JButton("Log In");
        loginButton.setBounds(98,91,193,33);
        JButton registerButton = new JButton("Register New User");
        registerButton.setBounds(98,136,193,33);
        
        add(registerButton);
        add(loginButton);
        
        registerButton.addActionListener(e -> mainFrame.showRegistration());
        
        loginButton.addActionListener(e -> loginUser());
        
	}
	
	private void loginUser() {
		String username = usernameField.getText().toLowerCase();
		String password = new String(passwordField.getPassword());
		
		User user = User.getUserByUsername(username);
			if(user!= null) {
				if (user.getPassword().equals(password)) {
					JOptionPane.showMessageDialog(this, "Login Successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
					if (user.getRole().equals("Student")) {
						mainFrame.setLoggedInUsername(username);
						mainFrame.showDashboard("Student");
					}else {
						mainFrame.showDashboard("Lecturer");
					}
				}else {
					JOptionPane.showMessageDialog(this, "Incorrect password!", "Error", JOptionPane.ERROR_MESSAGE);
                    passwordField.setText("");
				}
			}else {
				JOptionPane.showMessageDialog(this, "Username not found! Please register.", "Error", JOptionPane.ERROR_MESSAGE);
			}
	}
	
	public void clearFields() {
        usernameField.setText("");
        passwordField.setText("");
    }
}
