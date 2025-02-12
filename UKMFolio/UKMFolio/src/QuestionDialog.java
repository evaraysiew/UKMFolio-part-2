import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.ArrayList;

public class QuestionDialog extends JDialog{
	private JTextField questionTextField;
    private DefaultListModel<String> optionsListModel;
    private JList<String> optionsList;
    private int correctAnswerIndex = -1;
    private Quiz quiz;
    private Question question;
    private int questionIndex;
    private DefaultListModel<String> questionListModel;
    private List<String> options;
    
    public QuestionDialog(Quiz quiz, Question question, int questionIndex, DefaultListModel<String> questionListModel) {
    	super((Frame)null,true);
    	this.quiz = quiz;
        this.question = question;
        this.questionIndex = questionIndex;
        this.questionListModel = questionListModel;

        setTitle(question == null ? "Add Question" : "Edit Question");
        setSize(400, 400);
        setLayout(new BorderLayout());

        // Question input
        JPanel questionPanel = new JPanel(new BorderLayout());
        questionPanel.add(new JLabel("Question: "), BorderLayout.WEST);
        questionTextField = new JTextField(question == null ? "" : question.getText(), 20);
        questionPanel.add(questionTextField, BorderLayout.CENTER);
        add(questionPanel, BorderLayout.NORTH);

     // Options List
        options = (question == null) ? new ArrayList<>() : new ArrayList<>(question.getOptions());
        optionsListModel = new DefaultListModel<>();
        for (String option : options) {
            optionsListModel.addElement(option);
        }
        optionsList = new JList<>(optionsListModel);
        add(new JScrollPane(optionsList), BorderLayout.CENTER);

        // Buttons for managing options
        JPanel optionsPanel = new JPanel(new GridLayout(2,2,5,5));
        JButton addOptionButton = new JButton("Add Option");
        JButton editOptionButton = new JButton("Edit Option");
        JButton deleteOptionButton = new JButton("Delete Option");
        JButton setCorrectButton = new JButton("Set Correct Answer");

        optionsPanel.add(addOptionButton);
        optionsPanel.add(editOptionButton);
        optionsPanel.add(deleteOptionButton);
        optionsPanel.add(setCorrectButton);

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(optionsPanel, BorderLayout.CENTER);
        
        JButton saveButton = new JButton("Save Question");
        bottomPanel.add(saveButton, BorderLayout.SOUTH);
        add(bottomPanel, BorderLayout.SOUTH);

        // Button Actions
        addOptionButton.addActionListener(e -> addOption());
        editOptionButton.addActionListener(e -> editOption());
        deleteOptionButton.addActionListener(e -> deleteOption());
        setCorrectButton.addActionListener(e -> setCorrectAnswer());
        saveButton.addActionListener(e -> saveQuestion());

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setVisible(true);
    }
    
    private void addOption() {
        String newOption = JOptionPane.showInputDialog(this, "Enter option:");
        if (newOption != null && !newOption.trim().isEmpty()) {
            options.add(newOption);
            optionsListModel.addElement(newOption);
        }
    }
    
    private void editOption() {
        int selectedIndex = optionsList.getSelectedIndex();
        if (selectedIndex != -1) {
            String newOption = JOptionPane.showInputDialog(this, "Edit option:", options.get(selectedIndex));
            if (newOption != null && !newOption.trim().isEmpty()) {
                options.set(selectedIndex, newOption);
                optionsListModel.setElementAt(newOption, selectedIndex);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Select an option to edit.");
        }
    }
    
    private void deleteOption() {
        int selectedIndex = optionsList.getSelectedIndex();
        if (selectedIndex != -1) {
            options.remove(selectedIndex);
            optionsListModel.remove(selectedIndex);
            if(correctAnswerIndex == selectedIndex) {
            	correctAnswerIndex = -1; //reset correct option
            }else if (selectedIndex < correctAnswerIndex) {
                correctAnswerIndex--;
            }
        } else {
            JOptionPane.showMessageDialog(this, "Select an option to delete.");
        }
    }
    
    private void setCorrectAnswer() {
        int selectedIndex = optionsList.getSelectedIndex();
        if (selectedIndex != -1) {
            correctAnswerIndex = selectedIndex;
            JOptionPane.showMessageDialog(this, "Correct answer set: " + options.get(selectedIndex));
        } else {
            JOptionPane.showMessageDialog(this, "Select an option to set as correct answer.");
        }
    }
    
    private void saveQuestion() {
        String questionText = questionTextField.getText();
        
        if (questionText.trim().isEmpty() || options.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Question and at least one option are required.");
            return;
        }

        if(options.isEmpty()) {
        	JOptionPane.showMessageDialog(this, "At least one option is required.");
            return;
        }
        
        if (correctAnswerIndex == -1) {
            JOptionPane.showMessageDialog(this, "You must select a correct answer before saving.");
            return;
        }
        
        if (question == null) {
            question = new Question(questionText, options, correctAnswerIndex);
            quiz.getQuestions().add(question);
            questionListModel.addElement(questionText);
        } else {
            question.setText(questionText);
            question.setOptions(options);
            question.setCorrectAnswerIndex(correctAnswerIndex);
            questionListModel.setElementAt(questionText, questionIndex);
        }

        System.out.println("Saving Question: " + questionText);
        System.out.println("Options: " + options);
        System.out.println("Correct Answer Index: " + correctAnswerIndex + " (" + options.get(correctAnswerIndex) + ")");
        
        JOptionPane.showMessageDialog(this, "Question saved successfully!");
        dispose();
    }
}