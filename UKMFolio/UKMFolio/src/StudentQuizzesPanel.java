import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class StudentQuizzesPanel extends JPanel{
	private DefaultListModel<String> quizListModel;
    private JList<String> quizList;
    private JButton startQuizButton, viewQuizButton;
    private List<Quiz> quizzes;
    private String currentStudent;
    private static Map<String, Map<Quiz, List<String>>> studentAnswers = new HashMap<>();
    private static Map<String, Set<String>> submittedQuizzes = new HashMap<>();
    
	public StudentQuizzesPanel(String username) {
		this.quizzes = Quiz.getQuizzes();
		this.currentStudent = username;
		
		setLayout(new BorderLayout());
		
		quizListModel = new DefaultListModel<>();
		quizList = new JList<>(quizListModel);
		JScrollPane scrollPane = new JScrollPane(quizList);
        add(scrollPane, BorderLayout.CENTER);
        
        if (quizzes.isEmpty()) {
            quizListModel.addElement("No quizzes available.");
        } else {
            for (Quiz quiz : quizzes) {
                quizListModel.addElement(quiz.getTitle());
            }
        }
		
        JPanel buttonPanel = new JPanel();
        startQuizButton = new JButton("Start Quiz");
        buttonPanel.add(startQuizButton);
        viewQuizButton = new JButton("View Quiz");
        buttonPanel.add(viewQuizButton);
        add(buttonPanel, BorderLayout.SOUTH);
		
        startQuizButton.addActionListener(e -> openQuizForm(false)); // Start Quiz Mode
        viewQuizButton.addActionListener(e -> openQuizForm(true));
	}
	
	private void openQuizForm(boolean isViewMode) {
	    int selectedIndex = quizList.getSelectedIndex();
	    if (selectedIndex != -1) {
	        Quiz selectedQuiz = quizzes.get(selectedIndex);
	        
	        if (isViewMode) { 
	            // Only allow viewing if quiz was submitted
	            if (!submittedQuizzes.getOrDefault(currentStudent, new HashSet<>()).contains(selectedQuiz.getTitle())) {
	                JOptionPane.showMessageDialog(this, "You can only view quizzes you have submitted.");
	                return;
	            }
	        }else {
	            // Prevent retaking if already submitted
	            if (submittedQuizzes.getOrDefault(currentStudent, new HashSet<>()).contains(selectedQuiz.getTitle())) {
	                JOptionPane.showMessageDialog(this, "You have already submitted this quiz. You cannot retake it.");
	                return;
	            }
	        }

	        new QuizAnswerForm(currentStudent, selectedQuiz, isViewMode);
	    } else {
	        JOptionPane.showMessageDialog(this, "Select a quiz first.");
	    }
	}
	
	public static Map<String, Map<Quiz, List<String>>> getStudentAnswers() {
        return studentAnswers;
    }
	
	public static void markQuizAsSubmitted(String student, String quizTitle) {
	    submittedQuizzes.computeIfAbsent(student, k -> new HashSet<>()).add(quizTitle);
	}
	
	public static boolean isQuizSubmitted(String username, String quizTitle) {
        return submittedQuizzes.getOrDefault(username, Collections.emptySet()).contains(quizTitle);
    }
}
