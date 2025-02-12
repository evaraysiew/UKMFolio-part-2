

import java.util.ArrayList;
import java.util.Date;

public class ForumDiscussion {
    private int id;
    private String title;
    private String content;
    private String lecturer;
    private Date timestamp;
    private ArrayList<Comment> comments;
    private static int nextId = 1;

    public ForumDiscussion(String title, String content, String lecturer) {
        this.id = nextId++;
        this.title = title;
        this.content = content;
        this.lecturer = lecturer;
        this.timestamp = new Date();
        this.comments = new ArrayList<>();
    }

    // Getters
    public int getId() { return id; }
    public String getTitle() { return title; }
    public String getContent() { return content; }
    public String getLecturer() { return lecturer; }
    public Date getTimestamp() { return timestamp; }
    public ArrayList<Comment> getComments() { return comments; }
    
    public void addComment(Comment comment) {
        comments.add(comment);
    }
}
