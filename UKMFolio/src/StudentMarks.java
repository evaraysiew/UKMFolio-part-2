import java.util.HashMap;
import java.util.Map;

public class StudentMarks {
    private static final Map<String, Map<String, Integer>> studentMarks = new HashMap<>();

    public static void storeMarks(String username, String quizTitle, int score) {
    	 studentMarks.computeIfAbsent(username, k -> new HashMap<>()).put(quizTitle, score);
    }

    public static int getMarks(String username, String quiz) {
        return studentMarks.getOrDefault(username, new HashMap<>()).getOrDefault(quiz, 0);
    }
    

}
