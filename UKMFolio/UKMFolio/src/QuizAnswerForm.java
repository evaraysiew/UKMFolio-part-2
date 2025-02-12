import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

public class QuizAnswerForm extends JFrame {
	private String username;
    private Quiz quiz;
    private List<Question> questions;
    private JPanel questionPanel;
    private JButton submitButton;
    private boolean isViewMode;
    private Map<Quiz, List<String>> studentQuizAnswers;

    public QuizAnswerForm(String username, Quiz quiz, boolean isViewMode) {
        this.username = username;
        this.quiz = quiz;
        this.questions = quiz.getQuestions();
        this.isViewMode= isViewMode;
        
        studentQuizAnswers = StudentQuizzesPanel.getStudentAnswers()
                .computeIfAbsent(username, k -> new HashMap<>());
        
        List<String> answers = studentQuizAnswers.computeIfAbsent(quiz, k -> 
        new ArrayList<>(Collections.nCopies(quiz.getQuestions().size(), null)));

        setTitle(isViewMode ? "View Quiz: " + quiz.getTitle() : "Answer Quiz: " + quiz.getTitle());
        setSize(500, 400);
        setLayout(new BorderLayout());

        // Panel for Questions
        questionPanel = new JPanel();
        questionPanel.setLayout(new BoxLayout(questionPanel, BoxLayout.Y_AXIS));

        loadQuestions(answers);

        if (!isViewMode) {
            submitButton = new JButton("Submit Answers");
            submitButton.addActionListener(e -> submitAnswers());
            add(submitButton, BorderLayout.SOUTH);
        }else {
        	int score = StudentMarks.getMarks(username, quiz.getTitle());
            JLabel scoreLabel = new JLabel("Your Score: " + score + "/" + questions.size());
            scoreLabel.setFont(new Font("Arial", Font.BOLD, 14));
            scoreLabel.setHorizontalAlignment(SwingConstants.CENTER);
            add(scoreLabel, BorderLayout.SOUTH);
        }

        add(new JScrollPane(questionPanel), BorderLayout.CENTER);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);
    }

    private void loadQuestions(List<String> answers) {
        for (int i = 0; i < questions.size(); i++) {
            Question question = questions.get(i);

            // Question Label
            JLabel questionLabel = new JLabel((i + 1) + ". " + question.getText());
            questionLabel.setFont(new Font("Arial", Font.BOLD, 14));
            questionPanel.add(questionLabel);

            // Answer Options
            ButtonGroup group = new ButtonGroup();
            JPanel optionsPanel = new JPanel(new GridLayout(0, 1, 5, 5)); // Reducing gap

            int correctIndex = question.getCorrectAnswerIndex();
            String correctAnswer = question.getOptions().get(correctIndex);
            String selectedAnswer = answers.get(i);
            
            JLabel correctAnswerLabel = new JLabel();
            
            for (String option : question.getOptions()) {
                JRadioButton radioButton = new JRadioButton(option);
                radioButton.setEnabled(!isViewMode); // Disable buttons in view mode
                group.add(radioButton);
                optionsPanel.add(radioButton);

                if (option.equals(selectedAnswer)) {
                    radioButton.setSelected(true);
                    if (isViewMode) {
                    	 if (selectedAnswer != null && !selectedAnswer.equals(correctAnswer)) {
                             correctAnswerLabel.setText("Correct answer: " + correctAnswer);
                             correctAnswerLabel.setForeground(Color.BLUE);
                         }
                    }
                }
                
                if (!isViewMode) {
                    int questionIndex = i;
                    radioButton.addActionListener(e -> answers.set(questionIndex, option));
                }
            }

            questionPanel.add(optionsPanel);
            if (isViewMode && selectedAnswer != null && !selectedAnswer.equals(correctAnswer)) {
                questionPanel.add(correctAnswerLabel);
            }
            questionPanel.add(Box.createVerticalStrut(10)); // Add spacing between questions
        }
    }

    private void submitAnswers() {
        if (isViewMode) return;

        List<String> answers = studentQuizAnswers.get(quiz);
        
        if (answers.contains(null)) {
            JOptionPane.showMessageDialog(this, "Please answer all questions before submitting.");
            return;
        }

        // Calculate and store marks
        int score = calculateScore(answers);
        StudentMarks.storeMarks(username, quiz.getTitle(), score);
        StudentQuizzesPanel.markQuizAsSubmitted(username, quiz.getTitle());

        System.out.println("Submitted Answers for " + username + ": " + studentQuizAnswers.get(quiz));

        JOptionPane.showMessageDialog(this, "Answers submitted successfully!");
        dispose();
    }
    
    private int calculateScore(List<String> answers) {
        int score = 0;
        for (int i = 0; i < questions.size(); i++) {
            Question question = questions.get(i);
            int correctIndex = question.getCorrectAnswerIndex();
            String correctAnswer = question.getOptions().get(correctIndex);
            String studentAnswer = answers.get(i);

            System.out.println("Q" + (i + 1) + ": Correct Answer = " + correctAnswer + ", Student Answer = " + studentAnswer);
            
            if (studentAnswer != null && studentAnswer.equals(correctAnswer)) {
                score++;
            }
        }
   
        return score;
    }

}