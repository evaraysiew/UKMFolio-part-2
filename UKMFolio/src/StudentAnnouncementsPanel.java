import javax.swing.*;
import java.awt.*;

public class StudentAnnouncementsPanel extends JPanel {
    private DefaultListModel<String> announcementsListModel;
    private JList<String> announcementsList;
    private JTextPane announcementDetails;

    public StudentAnnouncementsPanel() {
        setLayout(new BorderLayout());

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

    // Refresh the UI with the latest announcements
    public void refreshAnnouncements() {
        announcementsListModel.clear();
        for (String announcement : AnnouncementsManager.getInstance().getAnnouncements()) {
            announcementsListModel.addElement(announcement);
        }
    }
}
