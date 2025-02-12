import javax.swing.*;
import java.awt.*;
import java. awt.event.*;
import java.util.*;

public class UserRegistration extends JPanel{
	// GUI components
	private JTextField usernameField;
	private JPasswordField passwordField;
	private JComboBox<String> roleBox;
	private MainFrame mainFrame;
	
	public UserRegistration(MainFrame mainFrame) {
		this.mainFrame = mainFrame;
		setLayout(null);
		
		//User name
		JLabel label = new JLabel("Username: ");
		label.setBounds(91, 39, 99, 39);
		add(label);
		usernameField = new JTextField();
		usernameField.setBounds(160, 48, 348, 20);
		add(usernameField);
		
		//password
		JLabel label_1 = new JLabel("Password: ");
		label_1.setBounds(91, 79, 99, 39);
		add(label_1);
		passwordField = new JPasswordField();
		passwordField.setBounds(160, 88, 348, 20);
		add(passwordField);
		
		//Role
		JLabel label_2 = new JLabel("Role:");
		label_2.setBounds(91, 119, 99, 39);
		add(label_2);
		roleBox = new JComboBox(); // Non-generic JComboBox
		roleBox.setBounds(160, 128, 348, 20);
		roleBox.addItem("Student");
		roleBox.addItem("Lecturer");		
		add(roleBox);
		
		//register
		JButton registerButton = new JButton("Register");
		registerButton.setBounds(183, 182, 225, 39);
		add(registerButton);
		
		//event listener for register
		registerButton.addActionListener(e -> registerUser());
		
		setVisible(true);
	}
	
	public void registerUser() {
		//get input
		String username = usernameField.getText().toLowerCase();
		String password = new String(passwordField.getPassword());
		String role = (String) roleBox.getSelectedItem();
		
		//validate input
		if (username.isEmpty() || password.isEmpty()) {
			JOptionPane.showMessageDialog(this,  "Please fill in all fields.", "Error", JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		//Check if username already exists
		if(User.getUserByUsername(username) != null) {
			JOptionPane.showMessageDialog(this, "Username already exists!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
		}
		
		//insert new user
		new User(username, password, role);
		JOptionPane.showMessageDialog(this, "Registration Successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
		mainFrame.showLogin();
	}
	
	public void clearFields() {
        usernameField.setText("");
        passwordField.setText("");
    }
}
