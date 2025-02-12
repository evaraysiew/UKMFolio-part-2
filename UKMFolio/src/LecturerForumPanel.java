import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;

public class LecturerForumPanel extends JPanel implements ForumEventListener {
    private ForumController controller;
    private JList<ForumDiscussion> discussionList;
    private DefaultListModel<ForumDiscussion> discussionListModel;
    private JPanel discussionPanel;
    private JTextArea contentArea;
    private ForumDiscussion currentDiscussion;
    private SimpleDateFormat dateFormat;

    public LecturerForumPanel() {
        controller = ForumController.getInstance();
        controller.addEventListener(this);
        dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        initializeUI();
    }
    @Override
    public void onForumEvent(ForumEvent event) {
        if (event.getType().equals("NEW_DISCUSSION") || 
            event.getType().equals("NEW_COMMENT")) {
            updateDiscussionList();
            if (currentDiscussion != null) {
                updateDiscussionDisplay();
            }
        }
    }
    private void initializeUI() {
        setLayout(new BorderLayout());
        
        // Create discussion list
        discussionListModel = new DefaultListModel<>();
        discussionList = new JList<>(discussionListModel);
        discussionList.setCellRenderer(new DiscussionListCellRenderer());
        
        // Create discussion creation panel
        JPanel creationPanel = new JPanel(new BorderLayout(5, 5));
        JTextField titleField = new JTextField();
        contentArea = new JTextArea(5, 30);
        contentArea.setLineWrap(true);
        contentArea.setWrapStyleWord(true);
        JButton createButton = new JButton("Create Discussion");

        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.add(new JLabel("Title: "), BorderLayout.WEST);
        titlePanel.add(titleField);
        
        creationPanel.add(titlePanel, BorderLayout.NORTH);
        creationPanel.add(new JScrollPane(contentArea), BorderLayout.CENTER);
        creationPanel.add(createButton, BorderLayout.SOUTH);

        // Discussion view panel
        discussionPanel = new JPanel(new BorderLayout());
        
        // Layout setup
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
            new JScrollPane(discussionList),
            new JScrollPane(discussionPanel));
        splitPane.setDividerLocation(150);

        add(creationPanel, BorderLayout.NORTH);
        add(splitPane, BorderLayout.CENTER);

        // Event handlers
        createButton.addActionListener(e -> {
            String title = titleField.getText().trim();
            String content = contentArea.getText().trim();
            if (!title.isEmpty() && !content.isEmpty()) {
                controller.createDiscussion(title, content, "Lecturer");
                titleField.setText("");
                contentArea.setText("");
            }
        });

        discussionList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                currentDiscussion = discussionList.getSelectedValue();
                updateDiscussionDisplay();
            }
        });

        updateDiscussionList();
    }

    private void updateDiscussionList() {
        discussionListModel.clear();
        for (ForumDiscussion discussion : controller.getDiscussions()) {
            discussionListModel.addElement(discussion);
        }
    }

    private void updateDiscussionDisplay() {
        discussionPanel.removeAll();
        
        if (currentDiscussion != null) {
            JPanel mainPanel = new JPanel();
            mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
            
            // Discussion header
            JPanel headerPanel = new JPanel(new BorderLayout());
            headerPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
            headerPanel.add(new JLabel("<html><h2>" + currentDiscussion.getTitle() + "</h2></html>"), 
                          BorderLayout.NORTH);
            headerPanel.add(new JLabel("Posted by: " + currentDiscussion.getLecturer() + 
                                     " on " + dateFormat.format(currentDiscussion.getTimestamp())), 
                          BorderLayout.CENTER);
            
            // Discussion content
            JTextArea contentArea = new JTextArea(currentDiscussion.getContent());
            contentArea.setWrapStyleWord(true);
            contentArea.setLineWrap(true);
            contentArea.setEditable(false);
            contentArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            
            // Comments section
            JPanel commentsPanel = new JPanel();
            commentsPanel.setLayout(new BoxLayout(commentsPanel, BoxLayout.Y_AXIS));
            commentsPanel.setBorder(BorderFactory.createTitledBorder("Comments"));
            
            for (Comment comment : currentDiscussion.getComments()) {
                commentsPanel.add(createCommentBox(comment));
                commentsPanel.add(Box.createVerticalStrut(10));
            }
            
            mainPanel.add(headerPanel);
            mainPanel.add(new JSeparator());
            mainPanel.add(contentArea);
            mainPanel.add(new JSeparator());
            mainPanel.add(commentsPanel);
            
            JScrollPane scrollPane = new JScrollPane(mainPanel);
            scrollPane.getVerticalScrollBar().setUnitIncrement(16);
            discussionPanel.add(scrollPane);
        }
        
        discussionPanel.revalidate();
        discussionPanel.repaint();
    }

    private JPanel createCommentBox(Comment comment) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.GRAY),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        
        JLabel authorLabel = new JLabel(comment.getAuthor() + " - " + 
                                      dateFormat.format(comment.getTimestamp()));
        authorLabel.setFont(authorLabel.getFont().deriveFont(Font.BOLD));
        
        JTextArea contentArea = new JTextArea(comment.getContent());
        contentArea.setWrapStyleWord(true);
        contentArea.setLineWrap(true);
        contentArea.setEditable(false);
        contentArea.setBackground(panel.getBackground());
        
        panel.add(authorLabel, BorderLayout.NORTH);
        panel.add(contentArea, BorderLayout.CENTER);
        
        return panel;
    }

    /*@Override
    public void update(Observable o, Object arg) {
        if (arg instanceof String) {
            String message = (String) arg;
            if (message.equals("NEW_DISCUSSION") || message.equals("NEW_COMMENT")) {
                updateDiscussionList();
                if (currentDiscussion != null) {
                    updateDiscussionDisplay();
                }
            }
        }
    }*/

    private class DiscussionListCellRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, 
                                                    int index, boolean isSelected, 
                                                    boolean cellHasFocus) {
            super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            if (value instanceof ForumDiscussion) {
                ForumDiscussion discussion = (ForumDiscussion) value;
                setText(discussion.getTitle());
            }
            return this;
        }
    }
}
