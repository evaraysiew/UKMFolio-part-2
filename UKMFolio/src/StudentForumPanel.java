import javax.swing.*;

import java.awt.*;
import java.text.SimpleDateFormat;

public class StudentForumPanel extends JPanel implements ForumEventListener {
    private ForumController controller;
    private JList<ForumDiscussion> discussionList;
    private DefaultListModel<ForumDiscussion> discussionListModel;
    private JPanel discussionPanel;
    private ForumDiscussion currentDiscussion;
    private SimpleDateFormat dateFormat;

    public StudentForumPanel() {
        controller = ForumController.getInstance();
        controller.addEventListener(this);
        dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        initializeUI();
    }

    private void initializeUI() {
        setLayout(new BorderLayout());
        
        // Create discussion list
        discussionListModel = new DefaultListModel<>();
        discussionList = new JList<>(discussionListModel);
        discussionList.setCellRenderer(new DiscussionListCellRenderer());
        
        // Discussion view panel
        discussionPanel = new JPanel(new BorderLayout());
        
        // Comment creation panel
        JPanel commentPanel = new JPanel(new BorderLayout());
        JTextArea commentArea = new JTextArea(3, 40);
        commentArea.setLineWrap(true);
        commentArea.setWrapStyleWord(true);
        JButton commentButton = new JButton("Add Comment");
        
        commentPanel.add(new JScrollPane(commentArea), BorderLayout.CENTER);
        commentPanel.add(commentButton, BorderLayout.EAST);
        
        // Layout setup
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
            new JScrollPane(discussionList),
            new JScrollPane(discussionPanel));
        splitPane.setDividerLocation(300);

        add(splitPane, BorderLayout.CENTER);
        add(commentPanel, BorderLayout.SOUTH);

        // Event handlers
        commentButton.addActionListener(e -> {
            if (currentDiscussion != null && !commentArea.getText().trim().isEmpty()) {
                controller.addComment(currentDiscussion.getId(), 
                                   commentArea.getText().trim(), "Student");
                commentArea.setText("");
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
}
