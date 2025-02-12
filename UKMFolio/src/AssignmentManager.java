import java.util.*;


public class AssignmentManager {
    private static AssignmentManager instance;
    private List<Assignment> assignments;
    private Map<Assignment, List<Submission>> submissionsMap;

    private AssignmentManager() {
        assignments = new ArrayList<>();
        submissionsMap = new HashMap<>();
    }

    public static synchronized AssignmentManager getInstance() {
        if (instance == null) instance = new AssignmentManager();
        return instance;
    }

    public void addAssignment(Assignment assignment) {
        assignments.add(assignment);
        submissionsMap.put(assignment, new ArrayList<>());
    }

    public void addSubmission(Assignment assignment, Submission submission) {
        // Get or create the submission list for this assignment
        List<Submission> assignmentSubmissions = submissionsMap.computeIfAbsent(assignment, k -> new ArrayList<>());
        
        // Remove any existing submission from this student
        assignmentSubmissions.removeIf(s -> s.getStudentUsername().equals(submission.getStudentUsername()));
        
        // Add the new submission
        assignmentSubmissions.add(submission);
    }

    public List<Submission> getSubmissions(Assignment assignment) {
        return submissionsMap.getOrDefault(assignment, new ArrayList<>());
    }

    public void gradeSubmission(Submission submission, int grade, String feedback) {
        submission.setGrade(grade);
        submission.setFeedback(feedback);
    }
    
    public List<Assignment> getAssignments(){
    	return assignments;
    }
}