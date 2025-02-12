import java.util.*;

public class Question {
	private String text;
	private List<String> options;
	private int correctAnswerIndex;
	
	
	public Question(String text, List<String> options, int correctAnswerIndex) {
		this.text = text;
		this.options = new ArrayList<>(options);
		this.correctAnswerIndex = correctAnswerIndex;
	}
	
	public void setText(String text) {
		this.text = text;
	}
	
	public String getText() {
		return text;
	}
	
	public List<String> getOptions(){
		return options;
	}
	
	public void setOptions(List<String> options) {
        this.options = new ArrayList<>(options);
    }
	
	public int getCorrectAnswerIndex() {
		return correctAnswerIndex;
	}
	
	public void setCorrectAnswerIndex(int correctAnswerIndex) {
		if (correctAnswerIndex  >=0 && correctAnswerIndex < options.size()) {
			this.correctAnswerIndex = correctAnswerIndex;
		}else {
			throw new IllegalArgumentException("Invalid correct answer index");
		}
	}
}
