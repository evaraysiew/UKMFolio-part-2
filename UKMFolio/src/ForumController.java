import java.util.*;

public class ForumController {
    private static ForumController instance;
    private ArrayList<ForumDiscussion> discussions;
    private List<ForumEventListener> listeners;

    private ForumController() {
        discussions = new ArrayList<>();
        listeners = new ArrayList<>();
    }

    public static ForumController getInstance() {
        if (instance == null) {
            instance = new ForumController();
        }
        return instance;
    }

    public void addEventListener(ForumEventListener listener) {
        listeners.add(listener);
    }

    public void removeEventListener(ForumEventListener listener) {
        listeners.remove(listener);
    }

    private void notifyListeners(String eventType, Object data) {
        ForumEvent event = new ForumEvent(eventType, data);
        for (ForumEventListener listener : listeners) {
            listener.onForumEvent(event);
        }
    }

    public void createDiscussion(String title, String content, String lecturer) {
        ForumDiscussion discussion = new ForumDiscussion(title, content, lecturer);
        discussions.add(discussion);
        notifyListeners("NEW_DISCUSSION", discussion);
    }

    public void addComment(int discussionId, String content, String author) {
        ForumDiscussion discussion = getDiscussionById(discussionId);
        if (discussion != null) {
            Comment comment = new Comment(content, author);
            discussion.addComment(comment);
            notifyListeners("NEW_COMMENT", comment);
        }
    }

    public ArrayList<ForumDiscussion> getDiscussions() {
        return new ArrayList<>(discussions);
    }

    public ForumDiscussion getDiscussionById(int id) {
        return discussions.stream()
                .filter(discussion -> discussion.getId() == id)
                .findFirst()
                .orElse(null);
    }
}