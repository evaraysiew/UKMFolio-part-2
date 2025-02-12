

import java.util.Date;

public class Comment {
	private int id;
    private String content;
    private String author;
    private Date timestamp;
    private static int nextId = 1;

    public Comment(String content, String author) {
        this.id = nextId++;
        this.content = content;
        this.author = author;
        this.timestamp = new Date();
    }
    
    // Getters
    public int getId() { return id; }
    public String getContent() { return content; }
    public String getAuthor() { return author; }
    public Date getTimestamp() { return timestamp; }
}
