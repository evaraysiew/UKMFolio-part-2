import java.util.ArrayList;
import java.util.List;

public class AnnouncementsManager {
    private static AnnouncementsManager instance;
    private List<String> announcements;

    private AnnouncementsManager() {
        announcements = new ArrayList<>();
    }

    public static AnnouncementsManager getInstance() {
        if (instance == null) {
            instance = new AnnouncementsManager();
        }
        return instance;
    }

    public void addAnnouncement(String announcement) {
        announcements.add(announcement);
    }

    public void updateAnnouncement(int index, String announcement) {
        if (index >= 0 && index < announcements.size()) {
            announcements.set(index, announcement);
        }
    }

    public List<String> getAnnouncements() {
        return announcements;
    }
}
