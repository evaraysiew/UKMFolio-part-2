import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import javax.swing.table.DefaultTableModel;
import com.github.lgooddatepicker.components.DateTimePicker;
import com.github.lgooddatepicker.components.DatePicker;
import com.github.lgooddatepicker.components.TimePicker;

public class LecturerAssignmentsPanel extends JPanel {
	private final AssignmentManager manager;
    private JTable assignmentsTable;
    private DefaultTableModel tableModel;
    private JButton createButton, gradeButton, viewSubmissionsButton;

    public LecturerAssignmentsPanel() {
        manager = AssignmentManager.getInstance();
        initializeUI();
        setupListeners();
    }

    private void initializeUI() {
        // Previous initialization code remains the same
        setLayout(new BorderLayout(10, 10));
        
        JPanel titlePanel = new JPanel();
        JLabel titleLabel = new JLabel("Assignment Management");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titlePanel.add(titleLabel);
        
        String[] columns = {"Title", "Description", "Open Date", "Due Date", "Status", "Submissions"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        assignmentsTable = new JTable(tableModel);
        assignmentsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        createButton = new JButton("Create Assignment");
        gradeButton = new JButton("Grade Submission");
        viewSubmissionsButton = new JButton("View Submissions");
        
        controlPanel.add(createButton);
        controlPanel.add(gradeButton);
        controlPanel.add(viewSubmissionsButton);
        
        add(titlePanel, BorderLayout.NORTH);
        add(new JScrollPane(assignmentsTable), BorderLayout.CENTER);
        add(controlPanel, BorderLayout.SOUTH);
        
        refreshTable();
        updateButtonStates();
    }

    // Modified viewSubmissions method to include file viewing
    private void viewSubmissions() {
        int selectedRow = assignmentsTable.getSelectedRow();
        if (selectedRow == -1) return;
        
        Assignment assignment = manager.getAssignments().get(selectedRow);
        List<Submission> submissions = manager.getSubmissions(assignment);
        
        if (submissions.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "No submissions available for this assignment.",
                "No Submissions",
                JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        // Create and show submissions dialog with file viewing capability
        showSubmissionsDialog(assignment, submissions);
    }

    // New method to show submissions with file viewing
    private void showSubmissionsDialog(Assignment assignment, List<Submission> submissions) {
        JDialog dialog = new JDialog((Frame)SwingUtilities.getWindowAncestor(this), 
            "View Submissions - " + assignment.getTitle(), true);
        dialog.setLayout(new BorderLayout(10, 10));
        
        // Create table model for submissions
        String[] columns = {"Student", "Submission Status", "Submission Time", "File", "Grade", "Feedback"};
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        // Get list of all registered students
        List<User> students = User.getUsers().stream()
            .filter(user -> user.getRole().equals("Student"))
            .toList();
        
        // Show submission status for all students
        for (User student : students) {
            Submission studentSubmission = assignment.getSubmissionByStudent(student.getUsername());
            
            model.addRow(new Object[]{
                student.getUsername(),
                studentSubmission != null ? "Submitted" : "Not Submitted",
                studentSubmission != null ? 
                    studentSubmission.getSubmissionTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")) : 
                    "-",
                studentSubmission != null ? 
                    (studentSubmission.getFile() != null ? studentSubmission.getFile().getName() : "No file") : 
                    "-",
                studentSubmission != null ? 
                    (studentSubmission.getGrade() != null ? studentSubmission.getGrade() : "Not graded") : 
                    "-",
                studentSubmission != null ? 
                    (studentSubmission.getFeedback() != null ? studentSubmission.getFeedback() : "No feedback") : 
                    "-"
            });
        }
        
        // Rest of the dialog setup remains the same...
        JTable submissionsTable = new JTable(model);
        submissionsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton viewFileButton = new JButton("View File");
        JButton closeButton = new JButton("Close");
        
        viewFileButton.addActionListener(e -> {
            int selectedRow = submissionsTable.getSelectedRow();
            if (selectedRow != -1) {
                String selectedStudent = (String) model.getValueAt(selectedRow, 0);
                Submission selectedSubmission = assignment.getSubmissionByStudent(selectedStudent);
                if (selectedSubmission != null && selectedSubmission.getFile() != null) {
                    showFileContentDialog(selectedSubmission);
                } else {
                    JOptionPane.showMessageDialog(dialog,
                        "No file available for this submission.",
                        "No File",
                        JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });
        
        closeButton.addActionListener(e -> dialog.dispose());
        
        buttonPanel.add(viewFileButton);
        buttonPanel.add(closeButton);
        
        dialog.add(new JScrollPane(submissionsTable), BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        
        dialog.setSize(800, 400);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    // New method to show file content
    private void showFileContentDialog(Submission submission) {
        JDialog fileDialog = new JDialog((Frame)SwingUtilities.getWindowAncestor(this), 
            "File View - " + submission.getFile().getName(), true);
        fileDialog.setLayout(new BorderLayout(10, 10));
        
        // Create header panel with file info
        JPanel headerPanel = new JPanel(new GridLayout(3, 1, 5, 5));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        headerPanel.add(new JLabel("Student: " + submission.getStudentUsername()));
        headerPanel.add(new JLabel("File: " + submission.getFile().getName()));
        headerPanel.add(new JLabel("Submitted: " + 
            submission.getSubmissionTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))));
        
        // Create content panel (simplified for this example)
        JPanel contentPanel = new JPanel(new BorderLayout(10, 10));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JTextArea contentArea = new JTextArea("Assignment Content\n\n" +
            "This is a placeholder for the actual file content.\n" +
            "In a complete implementation, this would show the actual content of:\n" +
            submission.getFile().getName());
        contentArea.setEditable(false);
        contentArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        
        contentPanel.add(new JScrollPane(contentArea), BorderLayout.CENTER);
        
        // Add close button
        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(e -> fileDialog.dispose());
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(closeButton);
        
        // Add components to dialog
        fileDialog.add(headerPanel, BorderLayout.NORTH);
        fileDialog.add(contentPanel, BorderLayout.CENTER);
        fileDialog.add(buttonPanel, BorderLayout.SOUTH);
        
        // Set dialog properties
        fileDialog.setSize(600, 400);
        fileDialog.setLocationRelativeTo(this);
        fileDialog.setVisible(true);
    }

    private void setupListeners() {
        createButton.addActionListener(e -> showCreateDialog());
        gradeButton.addActionListener(e -> gradeSubmission());
        viewSubmissionsButton.addActionListener(e -> viewSubmissions());
        
        assignmentsTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                updateButtonStates();
            }
        });
    }

    private void updateButtonStates() {
        boolean rowSelected = assignmentsTable.getSelectedRow() != -1;
        gradeButton.setEnabled(rowSelected);
        viewSubmissionsButton.setEnabled(rowSelected);
    }

    private void refreshTable() {
        tableModel.setRowCount(0);
        for (Assignment assignment : manager.getAssignments()) {
            List<Submission> submissions = manager.getSubmissions(assignment);
            tableModel.addRow(new Object[]{
                assignment.getTitle(),
                assignment.getDescription(),
                assignment.getFormattedSubmissionOpenDate(),
                assignment.getFormattedSubmissionDeadline(),
                assignment.getStatus(),
                submissions.size()
            });
        }
    }

    private void showCreateDialog() {
        // Create custom dialog
        JDialog dialog = new JDialog((Frame)SwingUtilities.getWindowAncestor(this), "Create New Assignment", true);
        dialog.setLayout(new BorderLayout(10, 10));
        
        // Create form panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 5, 5, 5);
        
        // Title field
        JTextField titleField = new JTextField(20);
        formPanel.add(new JLabel("Title:"), gbc);
        gbc.gridx = 1;
        formPanel.add(titleField, gbc);
        
        // Description field
        gbc.gridx = 0;
        gbc.gridy = 1;
        JTextArea descriptionArea = new JTextArea(3, 20);
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        formPanel.add(new JLabel("Description:"), gbc);
        gbc.gridx = 1;
        formPanel.add(new JScrollPane(descriptionArea), gbc);
        
        // Due Date picker
        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(new JLabel("Due Date:"), gbc);
        gbc.gridx = 1;
        DateTimePicker dueDatePicker = new DateTimePicker();
        formPanel.add(dueDatePicker, gbc);
        
        // Buttons panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton saveButton = new JButton("Save");
        JButton cancelButton = new JButton("Cancel");
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);
        
        // Add panels to dialog
        dialog.add(formPanel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        
        // Button listeners
        saveButton.addActionListener(e -> {
            try {
                // Validate input
                if (titleField.getText().trim().isEmpty()) {
                    throw new IllegalArgumentException("Title cannot be empty");
                }
                if (descriptionArea.getText().trim().isEmpty()) {
                    throw new IllegalArgumentException("Description cannot be empty");
                }
                
                LocalDateTime dueDate = dueDatePicker.getDateTimeStrict();
                if (dueDate == null) {
                    throw new IllegalArgumentException("Due date must be selected");
                }
                
                // Set open date to current date and time
                LocalDateTime openDate = LocalDateTime.now();
                
                // Create new assignment
                Assignment assignment = new Assignment(
                    titleField.getText().trim(),
                    descriptionArea.getText().trim(),
                    openDate,
                    dueDate
                );
                
                manager.addAssignment(assignment);
                refreshTable();
                dialog.dispose();
                
                JOptionPane.showMessageDialog(this,
                    "Assignment created successfully!",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);
                
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(dialog,
                    ex.getMessage(),
                    "Invalid Input",
                    JOptionPane.ERROR_MESSAGE);
            }
        });
        
        cancelButton.addActionListener(e -> dialog.dispose());
        
        // Show dialog
        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private void gradeSubmission() {
        int selectedRow = assignmentsTable.getSelectedRow();
        if (selectedRow == -1) return;
        
        Assignment assignment = manager.getAssignments().get(selectedRow);
        List<Submission> submissions = manager.getSubmissions(assignment);
        
        if (submissions.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "No submissions available for this assignment.",
                "No Submissions",
                JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        // Create submission grading dialog
        showGradingDialog(assignment, submissions);
    }

    private void showGradingDialog(Assignment assignment, List<Submission> submissions) {
        JDialog dialog = new JDialog((Frame)SwingUtilities.getWindowAncestor(this), "Grade Submissions", true);
        dialog.setLayout(new BorderLayout(10, 10));
        
        // Create submission list
        DefaultListModel<String> listModel = new DefaultListModel<>();
        submissions.forEach(s -> listModel.addElement(s.getStudentUsername()));
        JList<String> submissionList = new JList<>(listModel);
        
        // Create grade input panel
        JPanel gradePanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 5, 5, 5);
        
        JSpinner gradeSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 100, 1));
        JTextArea feedbackArea = new JTextArea(4, 30);
        
        gradePanel.add(new JLabel("Grade (0-100):"), gbc);
        gbc.gridx = 1;
        gradePanel.add(gradeSpinner, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 1;
        gradePanel.add(new JLabel("Feedback:"), gbc);
        gbc.gridx = 1;
        gradePanel.add(new JScrollPane(feedbackArea), gbc);
        
        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton saveButton = new JButton("Save Grade");
        JButton cancelButton = new JButton("Close");
        
        saveButton.addActionListener(e -> {
            try {
                int selectedIndex = submissionList.getSelectedIndex();
                if (selectedIndex == -1) {
                    throw new IllegalStateException("Please select a submission to grade");
                }
                
                Submission submission = submissions.get(selectedIndex);
                int grade = (Integer)gradeSpinner.getValue();
                String feedback = feedbackArea.getText().trim();
                
                /*if (feedback.isEmpty()) {
                    throw new IllegalArgumentException("Please provide feedback");
                }*/
                
                manager.gradeSubmission(submission, grade, feedback);
                
                JOptionPane.showMessageDialog(dialog,
                    "Grade saved successfully!",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);
                
                refreshTable();
                
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog,
                    ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        });
        
        cancelButton.addActionListener(e -> dialog.dispose());
        
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);
        
        // Add components to dialog
        dialog.add(new JScrollPane(submissionList), BorderLayout.WEST);
        dialog.add(gradePanel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        
        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }
}
