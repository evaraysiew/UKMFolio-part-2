import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;

public class LecturerAnnouncementsPanel extends JPanel {
    private DefaultListModel<String> announcementsListModel;
    private JList<String> announcementsList;
    private StudentAnnouncementsPanel studentPanel; 

    public LecturerAnnouncementsPanel(StudentAnnouncementsPanel studentPanel) {
        this.studentPanel = studentPanel;
        setLayout(new BorderLayout());

        announcementsListModel = new DefaultListModel<>();
        announcementsList = new JList<>(announcementsListModel);
        announcementsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        for (String announcement : AnnouncementsManager.getInstance().getAnnouncements()) {
            announcementsListModel.addElement(announcement);
        }

        JScrollPane listScrollPane = new JScrollPane(announcementsList);
        add(listScrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        JButton addButton = new JButton("Add Announcement");
        JButton editButton = new JButton("Edit Announcement");

        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        add(buttonPanel, BorderLayout.SOUTH);

        addButton.addActionListener(e -> showAnnouncementDialog(null));
        editButton.addActionListener(e -> {
            int selectedIndex = announcementsList.getSelectedIndex();
            if (selectedIndex != -1) {
                showAnnouncementDialog(selectedIndex);
            } else {
                JOptionPane.showMessageDialog(this, "Please select an announcement to edit.");
            }
        });
    }

    private void showAnnouncementDialog(Integer editIndex) {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Announcement", true);
        dialog.setSize(400, 300);
        dialog.setLayout(new BorderLayout());

        JPanel inputPanel = new JPanel(new GridLayout(2, 1));

        JTextField titleField = new JTextField();
        JTextArea contentArea = new JTextArea(5, 20);
        JScrollPane contentScrollPane = new JScrollPane(contentArea);

        inputPanel.add(new JLabel("Title:"));
        inputPanel.add(titleField);
        inputPanel.add(new JLabel("Text:"));
        inputPanel.add(contentScrollPane);

        if (editIndex != null) {
            String existingAnnouncement = announcementsListModel.get(editIndex);
            titleField.setText(extractTitle(existingAnnouncement));
            contentArea.setText(extractContent(existingAnnouncement));
        }

        dialog.add(inputPanel, BorderLayout.CENTER);

        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(e -> {
            String title = titleField.getText().trim();
            String content = contentArea.getText().trim();
            String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());

            if (!title.isEmpty() && !content.isEmpty()) {
                String announcementHTML = "<html><b style='font-size:14px;'>" + title + "</b><br>"
                        + "<i>Posted by: Lecturer on " + date + "</i><br><br>" + content + "</html>";

                if (editIndex == null) {
                    AnnouncementsManager.getInstance().addAnnouncement(announcementHTML);
                    announcementsListModel.addElement(announcementHTML);
                } else {
                    AnnouncementsManager.getInstance().updateAnnouncement(editIndex, announcementHTML);
                    announcementsListModel.set(editIndex, announcementHTML);
                }

                studentPanel.refreshAnnouncements();
                dialog.dispose();
            } else {
                JOptionPane.showMessageDialog(dialog, "Title and Text cannot be empty.");
            }
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(saveButton);
        dialog.add(buttonPanel, BorderLayout.SOUTH);

        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private String extractTitle(String html) {
        return html.replaceAll(".*<b style='font-size:14px;'>(.*?)</b>.*", "$1");
    }

    private String extractContent(String html) {
        return html.replaceAll(".*<br><br>(.*?)</html>", "$1");
    }
}
