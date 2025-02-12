import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

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
        JButton courseButton = new JButton("<html><body>Course<br>Material</body></html>");
        courseButton.setToolTipText("<html><body>Course Material</body></html>");
        JButton forumButton = new JButton("<html><body>Forum</body></html>");
        JButton assignmentsButton = new JButton("<html><body>Assignments</body></html>");
        JButton quizzesButton = new JButton("<html><body>Quizzes</body></html>");
        JButton announcementsButton = new JButton("<html><body>Announcements</body></html>");
        JButton logoutButton = new JButton("<html><body>Logout</body></html>");
        logoutButton.setToolTipText("Logout");

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
            contentPanel.add(new LecturerCourseMaterialPanel(), "Course Material");
            contentPanel.add(new LecturerForumPanel(), "Forum");
            contentPanel.add(new LecturerAssignmentsPanel(), "Assignments");
            contentPanel.add(new LecturerQuizzesPanel(), "Quizzes");
            contentPanel.add(new LecturerAnnouncementsPanel(), "Announcements");
        } else {
            contentPanel.add(new StudentCourseMaterialPanel(), "Course Material");
            contentPanel.add(new StudentForumPanel(), "Forum");
            contentPanel.add(new StudentAssignmentsPanel(), "Assignments");
            contentPanel.add(new StudentQuizzesPanel(mainFrame.getLoggedInUsername()), "Quizzes");
            contentPanel.add(new StudentAnnouncementsPanel(), "Announcements");
        }


        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, menuPanel, contentPanel);
        splitPane.setDividerLocation(50);
        add(splitPane);
        
        courseButton.addActionListener(new MenuButtonListener("Course Material"));
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
