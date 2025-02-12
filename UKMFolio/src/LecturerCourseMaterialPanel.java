import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.*;
import java.util.List;

public class LecturerCourseMaterialPanel extends JPanel{
	private JTextField courseNameField;
	private JTextField topicField;
	private JButton addMaterialButton;
	private JButton uploadButton;
	private JList<String> materialList;
	private DefaultListModel<String> materialListModel;
	
	private List<File> materials;
	
	public LecturerCourseMaterialPanel() {
		setLayout(new BorderLayout(10, 10));
		
		JPanel inputPanel = new JPanel(new GridLayout(3, 2, 10, 10));
		inputPanel.setBorder(BorderFactory.createTitledBorder("Upload Course Material"));
		inputPanel.add(new JLabel("Course Name:"));
		courseNameField = new JTextField();
		inputPanel.add(courseNameField);
		
		inputPanel.add(new JLabel("Topic:"));
		topicField = new JTextField();
		inputPanel.add(topicField);
		
		addMaterialButton = new JButton("Add Material");
		inputPanel.add(addMaterialButton);
		
		add(inputPanel, BorderLayout.NORTH);
		
		materials = new ArrayList<>();
		materialListModel = new DefaultListModel<>();
		materialList = new JList<>(materialListModel);
		materialList.setBorder(BorderFactory.createTitledBorder("Materials"));
		add(new JScrollPane(materialList), BorderLayout.CENTER);
		
		uploadButton = new JButton("Upload Materials");
		add(uploadButton, BorderLayout.SOUTH);
		
		addMaterialButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser fileChooser = new JFileChooser();
				int result = fileChooser.showOpenDialog(LecturerCourseMaterialPanel.this);
				if (result == JFileChooser.APPROVE_OPTION) {
					File selectedFile = fileChooser.getSelectedFile();
					materials.add(selectedFile);
					materialListModel.addElement(selectedFile.getName());
				}
			}
		});
		uploadButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String courseName = courseNameField.getText();
				String topic = topicField.getText();
				if (courseName.isEmpty() || topic.isEmpty() || materials.isEmpty()) {
					JOptionPane.showMessageDialog(LecturerCourseMaterialPanel.this, "Please fill in all fields and add at least one material.");
					return;
				}
				JOptionPane.showMessageDialog(LecturerCourseMaterialPanel.this, "Material uploaded successfully!");
				StudentCourseMaterialPanel.addCourseMaterial(courseName, topic, new ArrayList<>(materials));
				courseNameField.setText("");
				topicField.setText("");
				materials.clear();
				materialListModel.clear();
			}
		});
	}
}
