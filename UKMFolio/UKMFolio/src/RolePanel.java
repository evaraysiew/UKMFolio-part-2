import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public abstract class RolePanel extends JPanel {
	protected JPanel menuPanel;
	protected JPanel contentPanel;
	private String userRole;
	private CardLayout cardLayout;
	private MainFrame mainFrame;
	
	public RolePanel(MainFrame mainFrame, String role) {
		this.mainFrame = mainFrame;
		userRole = role;
		setLayout(new BorderLayout());
		
		// Menu Panel
        menuPanel = new JPanel();
        menuPanel.setLayout(new GridLayout(1, 6));  
        JButton courseButton = new JButton("Course Material");
        JButton forumButton = new JButton("Forum");
        JButton assignmentsButton = new JButton("Assignments");
        JButton quizzesButton = new JButton("Quizzes");
        JButton announcementsButton = new JButton("Announcements");
        JButton logoutButton = new JButton("Logout");

        menuPanel.add(courseButton);
        menuPanel.add(forumButton);
        menuPanel.add(assignmentsButton);
        menuPanel.add(quizzesButton);
        menuPanel.add(announcementsButton);
        menuPanel.add(logoutButton);
        
        // Content Panel
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        
        if (userRole.equals("Lecturer")) {
            contentPanel.add(new LecturerCourseMaterialPanel(), "CourseMaterial");
            contentPanel.add(new LecturerForumPanel(), "Forum");
            contentPanel.add(new LecturerAssignmentsPanel(), "Assignments");
            contentPanel.add(new LecturerQuizzesPanel(), "Quizzes");
            contentPanel.add(new LecturerAnnouncementsPanel(), "Announcements");
        } else {
            contentPanel.add(new StudentCourseMaterialPanel(), "CourseMaterial");
            contentPanel.add(new StudentForumPanel(), "Forum");
            contentPanel.add(new StudentAssignmentsPanel(), "Assignments");
            contentPanel.add(new StudentQuizzesPanel(mainFrame.getLoggedInUsername()), "Quizzes");
            contentPanel.add(new StudentAnnouncementsPanel(), "Announcements");
        }

        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, menuPanel, contentPanel);
        splitPane.setDividerLocation(50);
        add(splitPane);
        
        courseButton.addActionListener(new MenuButtonListener("CourseMaterial"));
        forumButton.addActionListener(new MenuButtonListener("Forum"));
        assignmentsButton.addActionListener(new MenuButtonListener("Assignments"));
        quizzesButton.addActionListener(new MenuButtonListener("Quizzes"));
        announcementsButton.addActionListener(new MenuButtonListener("Announcements"));
        logoutButton.addActionListener(e -> mainFrame.showLogin()); //return to login page
	}
	 
    private class MenuButtonListener implements ActionListener {
    	 private String panelName;

         public MenuButtonListener(String panelName) {
             this.panelName = panelName;
         }

         @Override
         public void actionPerformed(ActionEvent e) {
             cardLayout.show(contentPanel, panelName);
         }
    }
}
