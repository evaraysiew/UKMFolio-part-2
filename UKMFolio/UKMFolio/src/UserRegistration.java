import javax.swing.*;
import java.awt.*;

public class UserRegistration extends JPanel{
	// GUI components
	private JTextField usernameField;
	private JPasswordField passwordField;
	private JComboBox<String> roleBox;
	private MainFrame mainFrame;
	
	public UserRegistration(MainFrame mainFrame) {
		this.mainFrame = mainFrame;
		
		//set frame
		setLayout(new GridLayout(4,2));
		
		//User name
		add(new JLabel("Username: "));
		usernameField = new JTextField();
		add(usernameField);
		
		//password
		add(new JLabel("Password: "));
		passwordField = new JPasswordField();
		add(passwordField);
		
		//Role
		add(new JLabel("Role:"));
		roleBox = new JComboBox<>(new String[] {"Student", "Lecturer"});
		add(roleBox);
		
		//register
		JButton registerButton = new JButton("Register");
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
