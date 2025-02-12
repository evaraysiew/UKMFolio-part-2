
import java.io.File;
import java.time.LocalDateTime;

public class Submission {
    private String studentUsername;
    private File file;
    private LocalDateTime submissionTime;
    private Integer grade;
    private String feedback;

    public Submission(String studentUsername, File file) {
        this.studentUsername = studentUsername;
        this.file = file;
        this.submissionTime = LocalDateTime.now();
    }

    // Getters and setters
    public Integer getGrade() { return grade; }
    public void setGrade(Integer grade) { this.grade = grade; }
    public String getFeedback() { return feedback; }
    public void setFeedback(String feedback) { this.feedback = feedback; }
    public File getFile() { return file; }
    public String getStudentUsername() { return studentUsername; }
	public LocalDateTime getSubmissionTime() { return submissionTime; }
	public void setFile(File selectedFile) { this.file = selectedFile; }
	public void setSubmissionTime(LocalDateTime now) { this.submissionTime = now; }
}