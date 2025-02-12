import java.util.List;

public class StudentPanel extends RolePanel {
    private String username;
    private List<Quiz> quizzes;

    public StudentPanel(MainFrame mainFrame, String username) {
        super(mainFrame, "Student");  // Call RolePanel constructor
    }
}