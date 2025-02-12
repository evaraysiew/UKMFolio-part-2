import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Map;

public class StudentScoresForm extends JFrame {
    public StudentScoresForm(Quiz quiz) {
        setTitle("Student Scores - " + quiz.getTitle());
        setSize(400, 300);
        setLayout(new BorderLayout());

        DefaultListModel<String> scoreListModel = new DefaultListModel<>();
        JList<String> scoreList = new JList<>(scoreListModel);

        // Get all users and filter only students
        List<User> allUsers = User.getUsers();

        for (User user : allUsers) {
            if (user.getRole().equalsIgnoreCase("student")) {  // Only check students
                String studentName = user.getUsername();
                int score = StudentMarks.getMarks(studentName, quiz.getTitle());

                if (score == 0 && !StudentQuizzesPanel.isQuizSubmitted(studentName, quiz.getTitle())) {
                    scoreListModel.addElement(studentName + " - Not Yet Submitted");
                } else {
                    scoreListModel.addElement(studentName + " - " + score + "/" + quiz.getQuestions().size());
                }
            }
        }

        add(new JScrollPane(scoreList), BorderLayout.CENTER);

        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(e -> dispose());
        add(closeButton, BorderLayout.SOUTH);

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);
    }
}
