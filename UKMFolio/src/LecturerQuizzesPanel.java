import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.*;

public class LecturerQuizzesPanel extends JPanel{
	private DefaultListModel<String> quizListModel;
	private JList<String> quizList;
	private JButton addQuizButton, editQuizButton, deleteQuizButton, viewScoresButton;
	
	public LecturerQuizzesPanel() {
		setLayout(new BorderLayout());
		
		//quiz list
		quizListModel = new DefaultListModel<>();
		quizList = new JList<>(quizListModel);
		add(new JScrollPane(quizList), BorderLayout.CENTER);
		
		//buttons
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new GridLayout(2, 2, 5, 5));
		
		addQuizButton = new JButton("Add Quiz");
		editQuizButton = new JButton("Edit Quiz");
		deleteQuizButton = new JButton("Delete Quiz");
		viewScoresButton = new JButton("View Student Scores");
		
		buttonPanel.add(addQuizButton);
		buttonPanel.add(editQuizButton);
		buttonPanel.add(deleteQuizButton);
		buttonPanel.add(viewScoresButton);
		
		add(buttonPanel, BorderLayout.SOUTH);
		
		refreshQuizList();
		
		//button action
		addQuizButton.addActionListener(e -> addQuiz());
		editQuizButton.addActionListener(e -> editQuiz());
		deleteQuizButton.addActionListener(e -> deleteQuiz());
		viewScoresButton.addActionListener(e -> viewScores());
	}
	
	private void addQuiz() {
		String quizTitle = JOptionPane.showInputDialog("Enter quiz title:");
		if(quizTitle != null && !quizTitle.trim().isEmpty()) {
			new Quiz(quizTitle);
            quizListModel.addElement(quizTitle);
		}
	}
	
	private void editQuiz() {
		int selectedIndex = quizList.getSelectedIndex();
		if (selectedIndex != -1) {
			Quiz selectedQuiz = Quiz.getQuizzes().get(selectedIndex);
            new EditQuizDialog(selectedQuiz, quizListModel, selectedIndex);
		}else {
			JOptionPane.showMessageDialog(this, "Select a quiz to edit.");
		}
	}
	
	private void deleteQuiz() {
		int selectedIndex = quizList.getSelectedIndex();
		if (selectedIndex != -1) {
			Quiz.getQuizzes().remove(selectedIndex);
			quizListModel.remove(selectedIndex);
		}else {
			JOptionPane.showMessageDialog(this, "Select a quiz to delete.");
		}
	}	
	
	 private void viewScores() {
	        int selectedIndex = quizList.getSelectedIndex();
	        if (selectedIndex != -1) {
	            Quiz selectedQuiz = Quiz.getQuizzes().get(selectedIndex);
	            new StudentScoresForm(selectedQuiz);
	        } else {
	            JOptionPane.showMessageDialog(this, "Select a quiz to view scores.");
	        }
	    }
	
	private void refreshQuizList() {
        quizListModel.clear();
        for (Quiz quiz : Quiz.getQuizzes()) {
            quizListModel.addElement(quiz.getTitle());
        }
    }
}
