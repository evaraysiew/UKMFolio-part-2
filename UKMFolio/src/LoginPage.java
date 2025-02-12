import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class LoginPage extends JPanel {
	private JTextField usernameField;
	private JPasswordField passwordField;
	private MainFrame mainFrame;
	
	public LoginPage(MainFrame mainFrame) {
		this.mainFrame = mainFrame;
		setLayout(null);
		
        JLabel userLabel = new JLabel("Username: ");
        userLabel.setBounds(129, 60, 95,20);
        add(userLabel);
        usernameField = new JTextField();
        usernameField.setBounds(196,60,321,20);
        add(usernameField);
        
        JLabel passLabel = new JLabel("Password: ");
        passLabel.setBounds(129,103,69,20);
        add(passLabel);
        passwordField = new JPasswordField();
        passwordField.setBounds(196,103,321,20);
        add(passwordField);
        
        JButton loginButton = new JButton("Log In");
        loginButton.setBounds(223,158,193,33);
        JButton registerButton = new JButton("Register New User");
        registerButton.setBounds(223,203,193,33);
        
        add(registerButton);
        add(loginButton);
        
        JLabel lblTitle = new JLabel("LOG IN");
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
        lblTitle.setFont(new Font("Tahoma", Font.BOLD, 16));
        lblTitle.setEnabled(false);
        lblTitle.setBounds(278, 24, 80, 14);
        add(lblTitle);
        
        registerButton.addActionListener(e -> mainFrame.showRegistration());
        
        loginButton.addActionListener(e -> loginUser());
        
        setVisible(true);
	}
	
	private void loginUser() {
		String username = usernameField.getText().toLowerCase();
		String password = new String(passwordField.getPassword());
		
		User user = User.getUserByUsername(username);
			if(user!= null) {
				if (user.getPassword().equals(password)) {
					JOptionPane.showMessageDialog(this, "Login Successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
					User.setCurrentUser(user);
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
