import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javax.swing.table.DefaultTableModel;
import java.util.List;

public class StudentAssignmentsPanel extends JPanel {
    private final AssignmentManager manager;
    private JTable assignmentsTable;
    private JButton uploadButton, viewDetailsButton;
    private JLabel statusLabel;
    private DefaultTableModel tableModel;
    private Assignment selectedAssignment;

    public StudentAssignmentsPanel() {
        manager = AssignmentManager.getInstance();
        initializeUI();
        setupListeners();
    }

    private void initializeUI() {
        setLayout(new BorderLayout(10, 10));
        
        // Create title panel with improved styling
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel titleLabel = new JLabel("Assignment Dashboard");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titlePanel.add(titleLabel);
        
        // Initialize table with more detailed columns
        String[] columns = {"Title", "Description", "Open Date", "Due Date", "Status", "Time Remaining", "Grade", "Feedback"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        assignmentsTable = new JTable(tableModel);
        assignmentsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        assignmentsTable.getColumnModel().getColumn(1).setPreferredWidth(200); // Make description column wider
        
        // Control panel with improved layout
        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        uploadButton = new JButton("Upload Submission");
        viewDetailsButton = new JButton("View Details");
        statusLabel = new JLabel("Select an assignment to view options");
        statusLabel.setForeground(Color.DARK_GRAY);
        
        controlPanel.add(uploadButton);
        controlPanel.add(viewDetailsButton);
        controlPanel.add(statusLabel);
        
        // Add components to panel
        add(titlePanel, BorderLayout.NORTH);
        add(new JScrollPane(assignmentsTable), BorderLayout.CENTER);
        add(controlPanel, BorderLayout.SOUTH);
        
        refreshTable();
        updateButtonStates(false);
    }

    private void setupListeners() {
        assignmentsTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = assignmentsTable.getSelectedRow();
                if (selectedRow != -1) {
                    try {
                        selectedAssignment = manager.getAssignments().get(selectedRow);
                        boolean canSubmit = selectedAssignment.isOpen();
                        updateButtonStates(true);
                        updateStatusLabel(selectedAssignment);
                    } catch (IndexOutOfBoundsException ex) {
                        handleError("Error accessing assignment details", ex);
                    }
                } else {
                    updateButtonStates(false);
                }
            }
        });

        uploadButton.addActionListener(e -> uploadSubmission());
        viewDetailsButton.addActionListener(e -> viewAssignmentDetails());
    }

    private void updateButtonStates(boolean rowSelected) {
        uploadButton.setEnabled(rowSelected && (selectedAssignment != null && selectedAssignment.isOpen()));
        viewDetailsButton.setEnabled(rowSelected);
    }

    private void updateStatusLabel(Assignment assignment) {
        if (assignment == null) {
            statusLabel.setText("Select an assignment to view options");
            return;
        }

        String currentUsername = User.getCurrentUser().getUsername();
        
        if (assignment.hasStudentSubmitted(currentUsername)) {
            statusLabel.setText("Assignment submitted - No further submissions allowed");
            statusLabel.setForeground(new Color(0, 100, 0)); // Dark green
            uploadButton.setEnabled(false);
        } else if (assignment.isOpen()) {
            statusLabel.setText("Open for submission - " + assignment.getTimeRemaining());
            statusLabel.setForeground(new Color(0, 0, 150)); // Dark blue
            uploadButton.setEnabled(true);
        } else if (assignment.getStatus().equals("Not Started")) {
            statusLabel.setText("Assignment not yet open");
            statusLabel.setForeground(Color.GRAY);
            uploadButton.setEnabled(false);
        } else {
            statusLabel.setText("Submission period has ended");
            statusLabel.setForeground(new Color(150, 0, 0)); // Dark red
            uploadButton.setEnabled(false);
        }
    }

    private void uploadSubmission() {
        try {
            if (selectedAssignment == null) {
                throw new IllegalStateException("No assignment selected");
            }
            
            String currentUsername = User.getCurrentUser ().getUsername();
            
            // Check if student has already submitted
            Submission existingSubmission = selectedAssignment.getSubmissionByStudent(currentUsername);
            
            if (existingSubmission != null) {
                // If the assignment is still open, allow editing
                if (selectedAssignment.isOpen()) {
                    int confirm = JOptionPane.showConfirmDialog(this,
                        "You have already submitted this assignment. Do you want to replace your existing submission?",
                        "Confirm Replacement",
                        JOptionPane.YES_NO_OPTION);
                    
                    if (confirm != JOptionPane.YES_OPTION) {
                        return; // User chose not to replace the submission
                    }
                } else {
                    JOptionPane.showMessageDialog(this,
                        "You have already submitted this assignment. Multiple submissions are not allowed.",
                        "Submission Error",
                        JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }

            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Select Assignment Submission File");
            
            int result = fileChooser.showOpenDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                
                // Validate file size (e.g., 10MB limit)
                if (selectedFile.length() > 10 * 1024 * 1024) {
                    throw new IllegalArgumentException("File size exceeds 10MB limit");
                }
                
                // Confirm submission
                int confirm = JOptionPane.showConfirmDialog(this,
                    "Are you sure you want to submit this file?\nYou will not be able to submit again.",
                    "Confirm Submission",
                    JOptionPane.YES_NO_OPTION);
                    
                if (confirm == JOptionPane.YES_OPTION) {
                    Submission submission = new Submission(currentUsername, selectedFile);
                    
                    // Update to use Assignment's addSubmission method
                    selectedAssignment.addSubmission(submission);
                    
                    JOptionPane.showMessageDialog(this,
                        "Assignment submitted successfully!\nSubmission time: " + 
                        submission.getSubmissionTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE);
                        
                    refreshTable();
                    updateStatusLabel(selectedAssignment);
                }
            }
        } catch (Exception ex) {
            handleError("Error submitting assignment", ex);
        }
    }

    private void viewAssignmentDetails() {
        try {
            if (selectedAssignment == null) return;
            
            String currentUsername = User.getCurrentUser().getUsername();
            Submission submission = selectedAssignment.getSubmissionByStudent(currentUsername);
            
            JDialog dialog = new JDialog((Frame)SwingUtilities.getWindowAncestor(this), "Assignment Details", true);
            dialog.setLayout(new BorderLayout(10, 10));
            
            // Create details panel
            JPanel detailsPanel = new JPanel(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.anchor = GridBagConstraints.WEST;
            gbc.insets = new Insets(5, 5, 5, 5);
            
            addDetailRow(detailsPanel, gbc, "Title:", selectedAssignment.getTitle());
            addDetailRow(detailsPanel, gbc, "Description:", selectedAssignment.getDescription());
            addDetailRow(detailsPanel, gbc, "Open Date:", selectedAssignment.getFormattedSubmissionOpenDate());
            addDetailRow(detailsPanel, gbc, "Due Date:", selectedAssignment.getFormattedSubmissionDeadline());
            addDetailRow(detailsPanel, gbc, "Status:", selectedAssignment.getStatus());
            
            if (submission != null) {
                addDetailRow(detailsPanel, gbc, "Submission Date:", 
                    submission.getSubmissionTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
                addDetailRow(detailsPanel, gbc, "Submitted File:", 
                    submission.getFile() != null ? submission.getFile().getName() : "N/A");
                addDetailRow(detailsPanel, gbc, "Grade:", 
                    submission.getGrade() != null ? submission.getGrade().toString() : "Not graded");
                addDetailRow(detailsPanel, gbc, "Feedback:", 
                    submission.getFeedback() != null ? submission.getFeedback() : "No feedback provided");
            }
            
            JButton closeButton = new JButton("Close");
            closeButton.addActionListener(e -> dialog.dispose());
            
            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            buttonPanel.add(closeButton);
            
            dialog.add(new JScrollPane(detailsPanel), BorderLayout.CENTER);
            dialog.add(buttonPanel, BorderLayout.SOUTH);
            
            dialog.pack();
            dialog.setLocationRelativeTo(this);
            dialog.setVisible(true);
            
        } catch (Exception ex) {
            handleError("Error viewing assignment details", ex);
        }
    }

    private void addDetailRow(JPanel panel, GridBagConstraints gbc, String label, String value) {
        gbc.gridx = 0;
        gbc.gridy++;
        panel.add(new JLabel(label), gbc);
        
        gbc.gridx = 1;
        JTextArea valueArea = new JTextArea(value);
        valueArea.setEditable(false);
        valueArea.setWrapStyleWord(true);
        valueArea.setLineWrap(true);
        valueArea.setBackground(panel.getBackground());
        valueArea.setFont(new JLabel().getFont());
        panel.add(valueArea, gbc);
    }

    private void refreshTable() {
        try {
            tableModel.setRowCount(0);
            String currentUsername = User.getCurrentUser().getUsername();
            
            for (Assignment assignment : manager.getAssignments()) {
                String submissionStatus = assignment.hasStudentSubmitted(currentUsername) ? 
                    "Submitted" : assignment.getStatus();
                    
                Submission studentSubmission = assignment.getSubmissionByStudent(currentUsername);
                
                Object[] rowData = {
                    assignment.getTitle(),
                    assignment.getDescription(),
                    assignment.getFormattedSubmissionOpenDate(),
                    assignment.getFormattedSubmissionDeadline(),
                    submissionStatus,
                    assignment.getTimeRemaining(),
                    studentSubmission != null ? 
                        (studentSubmission.getGrade() != null ? studentSubmission.getGrade() : "Pending") : 
                        "Not Submitted",
                    studentSubmission != null ? 
                        (studentSubmission.getFeedback() != null ? studentSubmission.getFeedback() : "No feedback yet") : 
                        "-"
                };
                
                tableModel.addRow(rowData);
            }
        } catch (Exception ex) {
            handleError("Error refreshing assignment table", ex);
        }
    }

    private void handleError(String message, Exception ex) {
        String errorMessage = message + "\nError: " + ex.getMessage();
        JOptionPane.showMessageDialog(this,
            errorMessage,
            "Error",
            JOptionPane.ERROR_MESSAGE);
        System.err.println(errorMessage);
        ex.printStackTrace();
    }

    public void refreshPanel() {
        refreshTable();
    }
}
