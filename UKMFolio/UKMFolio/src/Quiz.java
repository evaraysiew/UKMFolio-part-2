import java.util.*;

public class Quiz {
	private String title;
	private List<Question> questions;
	private static List<Quiz> quizList = new ArrayList<>();
	
	public Quiz(String t) {
		title=t;
		this.questions = new ArrayList<>();
		quizList.add(this);
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getTitle() {
		return title;
	}
	
	public List<Question> getQuestions(){
		return questions;
	}
	
	public void addQuestion(Question question) {
		questions.add(question);
	}
	
	public void removequestion(Question question) {
		questions.remove(question);
	}
	
	public static List<Quiz> getQuizzes() { // Method to access quizzes
        return quizList;
    }

}
