import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.io.File;

public class Assignment {
    // Core properties
    private String title;
    private String description;
    private LocalDateTime submissionOpenDate;
    private LocalDateTime submissionDeadline;
    private File assignmentFile; // The task file uploaded by lecturer
    private List<Submission> submissions;
    
    // Assignment status tracking
    private boolean isOpen;
    private String status; // "Not Started", "Open", "Closed"
    
    public Assignment(String title, String description, LocalDateTime submissionOpenDate, LocalDateTime submissionDeadline) {
        this.title = title;
        this.description = description;
        this.submissionOpenDate = submissionOpenDate;
        this.submissionDeadline = submissionDeadline;
        this.submissions = new ArrayList<>();
        this.status = "Not Started";
        updateStatus();
    }
    
    // Method to update assignment status based on current time
    private void updateStatus() {
        LocalDateTime now = LocalDateTime.now();
        if (now.isBefore(submissionOpenDate)) {
            status = "Not Started";
            isOpen = false;
        } else if (now.isAfter(submissionDeadline)) {
            status = "Closed";
            isOpen = false;
        } else {
            status = "Open";
            isOpen = true;
        }
    }
    
    // Submission management
    public void addSubmission(Submission submission) {
        updateStatus();
        if (isOpen) {
            // Remove any existing submission from this student
            submissions.removeIf(s -> s.getStudentUsername().equals(submission.getStudentUsername()));
            // Add the new submission
            submissions.add(submission);
            // Also update the AssignmentManager
            AssignmentManager.getInstance().addSubmission(this, submission);
        } else {
            throw new IllegalStateException("Assignment is not open for submissions");
        }
    }
    public List<Submission> getSubmissions() {
        return new ArrayList<>(submissions); // Return a copy to preserve encapsulation
    }
    
    public Submission getSubmissionByStudent(String studentUsername) {
        return submissions.stream()
            .filter(s -> s.getStudentUsername().equals(studentUsername))
            .findFirst()
            .orElse(null);
    }
    
    // Getters and setters
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public LocalDateTime getSubmissionOpenDate() {
        return submissionOpenDate;
    }
    
    public void setSubmissionOpenDate(LocalDateTime submissionOpenDate) {
        this.submissionOpenDate = submissionOpenDate;
        updateStatus();
    }
    
    public LocalDateTime getSubmissionDeadline() {
        return submissionDeadline;
    }
    
    public void setSubmissionDeadline(LocalDateTime submissionDeadline) {
        this.submissionDeadline = submissionDeadline;
        updateStatus();
    }
    
    public File getAssignmentFile() {
        return assignmentFile;
    }
    
    public void setAssignmentFile(File assignmentFile) {
        this.assignmentFile = assignmentFile;
    }
    
    public String getStatus() {
        updateStatus();
        return status;
    }
    
    public boolean isOpen() {
        updateStatus();
        return isOpen;
    }
    
    // Utility methods
    public String getFormattedSubmissionOpenDate() {
        return submissionOpenDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
    }
    
    public String getFormattedSubmissionDeadline() {
        return submissionDeadline.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
    }
    
    public String getTimeRemaining() {
        updateStatus();
        if (!isOpen) {
            return status;
        }
        
        LocalDateTime now = LocalDateTime.now();
        long hours = java.time.Duration.between(now, submissionDeadline).toHours();
        long minutes = java.time.Duration.between(now, submissionDeadline).toMinutesPart();
        
        return String.format("%d hours %d minutes remaining", hours, minutes);
    }
    
    public boolean hasStudentSubmitted(String username) {
        return submissions.stream()
                .anyMatch(s -> s.getStudentUsername().equals(username));
    }
    
    @Override
    public String toString() {
        return String.format("Assignment: %s (Status: %s)", title, getStatus());
    }
}