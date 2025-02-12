import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class EditQuizDialog extends JDialog {
	private JTextField quizTitleField;
	private DefaultListModel<String> questionListModel;
	private JList<String> questionList;
	private Quiz quiz;
	private DefaultListModel<String> quizListModel;
	private int quizIndex;
	
	public EditQuizDialog(Quiz quiz, DefaultListModel<String> qlm, int qi) {
		if (quiz==null) {
			throw new IllegalArgumentException("Quiz cannot be null!");
		}
		this.quiz = quiz;
		quizListModel = qlm;
		quizIndex = qi;
		
		setTitle("Edit Quiz");
		setSize(500,500);
		setLayout(new BorderLayout());
		
		quizTitleField = new JTextField(quiz.getTitle(), 20);
		
		//quiz title
		JPanel titlePanel = new JPanel(new BorderLayout());
		titlePanel.add(new JLabel("Quiz Title: "), BorderLayout.WEST);
		titlePanel.add(quizTitleField,BorderLayout.CENTER);
		add(titlePanel,BorderLayout.NORTH);
		
		//question list
		questionListModel = new DefaultListModel<>();
		updateQuestionListDisplay();
		questionList = new JList<>(questionListModel);
		add(new JScrollPane(questionList),BorderLayout.CENTER);
		
		//buttons
		JPanel buttonPanel = new JPanel();
		JButton editQuestionButton = new JButton("Edit Question");
		JButton deleteQuestionButton = new JButton("Delete Question");
		JButton addQuestionButton = new JButton("Add Question");
		JButton saveButton = new JButton("Save Changes");
		
		buttonPanel.add(editQuestionButton);
		buttonPanel.add(deleteQuestionButton);
		buttonPanel.add(addQuestionButton);
		buttonPanel.add(saveButton);
		add(buttonPanel, BorderLayout.SOUTH);
		
		//button actions
		editQuestionButton.addActionListener(e -> editQuestion());
		deleteQuestionButton.addActionListener(e -> deleteQuestion());
		addQuestionButton.addActionListener(e -> addQuestion());
		saveButton.addActionListener(e -> saveChanges());
		
		setModal(true);
		setLocationRelativeTo(null);
		setVisible(true);
	}
	
	private void updateQuestionListDisplay() {
		questionListModel.clear();
		for(Question q : quiz.getQuestions()) {
			StringBuilder questionText = new StringBuilder(q.getText()+ "[");
			for(int i=0; i<q.getOptions().size(); i++) {
				questionText.append(q.getOptions().get(i));
				if(i<q.getOptions().size() -1) {
					questionText.append(", ");
				}
			}
			questionText.append("]");
			questionListModel.addElement(questionText.toString());
		}
	}
	
	private void editQuestion() {
		int selectedIndex = questionList.getSelectedIndex();
		if (selectedIndex != -1) {
			Question selectedQuestion = quiz.getQuestions().get(selectedIndex);
			new QuestionDialog(quiz, selectedQuestion, selectedIndex, questionListModel);
			updateQuestionListDisplay();
		}else {
			JOptionPane.showMessageDialog(this, "Select a question to edit.");
		}
	}
	
	private void deleteQuestion() {
		int selectedIndex = questionList.getSelectedIndex();
		if(selectedIndex != -1) {
			quiz.getQuestions().remove(selectedIndex);
			updateQuestionListDisplay();
		}else {
			JOptionPane.showMessageDialog(this, "Select a question to delete.");
		}
	}
	
	private void addQuestion() {
		new QuestionDialog(quiz, null, -1, questionListModel);
		updateQuestionListDisplay();
	}
	
	private void saveChanges() {
		quiz.setTitle(quizTitleField.getText());
		quizListModel.setElementAt(quizTitleField.getText(), quizIndex);
		JOptionPane.showMessageDialog(this, "Quiz updated successfully!");
		dispose();
	}
}
