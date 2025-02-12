import javax.swing.*;
import java.awt.*;

public class MainFrame {
	private JFrame frame;
	private JPanel cardPanel;
	private CardLayout cardLayout;
	private RolePanel rolePanel;
	private LoginPage loginPage;
	private UserRegistration userRegistration;
	private String loggedInUsername;
	
	/**
	 * @wbp.parser.entryPoint
	 */
	public MainFrame() {
		frame = new JFrame("UKMFolio");
		frame.setSize(615,380);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		cardLayout = new CardLayout();
		cardPanel = new JPanel(cardLayout);
		
		LoginPage loginPage = new LoginPage(this);
		userRegistration = new UserRegistration(this);
		
		cardPanel.add(loginPage, "Login");
		loginPage.setLayout(null);
		cardPanel.add(userRegistration, "Register");

		
		cardLayout.show(cardPanel, "Login");
		
		frame.getContentPane().add(cardPanel);
		frame.setVisible(true);
		}
	
	public void showRegistration() {
		userRegistration.clearFields();
		cardLayout.show(cardPanel, "Register");
	}
	
	public void showLogin() {
		if (loginPage == null) {
			loginPage = new LoginPage(this);
			cardPanel.add(loginPage, "Login");
		}
		loginPage.clearFields();
        cardLayout.show(cardPanel, "Login");
    }
	
	public void showDashboard(String role) {
        rolePanel = role.equalsIgnoreCase("Student") ? new StudentPanel(this,role) : new LecturerPanel(this, role);
        
        cardPanel.add(rolePanel, role);
        cardLayout.show(cardPanel, role);
    }

	public void setLoggedInUsername(String username) {
	    this.loggedInUsername = username;
	}

	public String getLoggedInUsername() {
	    return loggedInUsername;
	}

	public static void main(String[] args) {
	        SwingUtilities.invokeLater(MainFrame::new); // Initialize MainFrame
	}
}

