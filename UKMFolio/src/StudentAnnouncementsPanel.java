import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class StudentAnnouncementsPanel extends JPanel {
    private static final List<StudentAnnouncementsPanel> instances = new ArrayList<>();
    private DefaultListModel<String> announcementsListModel;
    private JList<String> announcementsList;
    private JTextPane announcementDetails;

    public StudentAnnouncementsPanel() {
        setLayout(new BorderLayout());
        instances.add(this);  // Register this instance

        announcementsListModel = new DefaultListModel<>();
        announcementsList = new JList<>(announcementsListModel);
        announcementsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        announcementsList.addListSelectionListener(e -> showSelectedAnnouncement());

        JScrollPane listScrollPane = new JScrollPane(announcementsList);
        add(listScrollPane, BorderLayout.WEST);

        announcementDetails = new JTextPane();
        announcementDetails.setContentType("text/html");
        announcementDetails.setEditable(false);

        JScrollPane detailsScrollPane = new JScrollPane(announcementDetails);
        add(detailsScrollPane, BorderLayout.CENTER);

        refreshAnnouncements();
    }

    private void showSelectedAnnouncement() {
        if (!announcementsList.isSelectionEmpty()) {
            String selectedAnnouncement = announcementsList.getSelectedValue();
            announcementDetails.setText(selectedAnnouncement);
        }
    }

    public void refreshAnnouncements() {
        announcementsListModel.clear();
        for (String announcement : AnnouncementsManager.getInstance().getAnnouncements()) {
            announcementsListModel.addElement(announcement);
        }
    }

    // Static method to refresh all student panels when the lecturer adds/edits an announcement
    public static void refreshAll() {
        for (StudentAnnouncementsPanel panel : instances) {
            panel.refreshAnnouncements();
        }
    }
}
