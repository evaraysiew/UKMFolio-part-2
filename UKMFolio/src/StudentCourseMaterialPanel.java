import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import java.util.List;

public class StudentCourseMaterialPanel extends JPanel{
	private static Map<String, Map<String, List<File>>> courseMaterials = new HashMap<>();
	private JComboBox<String> courseComboBox;
	private JComboBox<String> topicComboBox;
	private JList<String> materialList;
	private DefaultListModel<String> materialListModel;
	
	public StudentCourseMaterialPanel() {
		setLayout(new BorderLayout(10, 10));
		
		JPanel selectionPanel = new JPanel(new GridBagLayout());
		selectionPanel.setBorder(BorderFactory.createTitledBorder("Select Course and Topic"));
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(5, 5, 5, 5);
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridx = 0;
		gbc.gridy = 0;
		selectionPanel.add(new JLabel("Course:"));
		
		courseComboBox = new JComboBox<>();
		gbc.gridx = 1;
		gbc.gridy = 0;
		selectionPanel.add(courseComboBox, gbc);
		
		gbc.gridx = 0;
		gbc.gridy = 1;
		selectionPanel.add(new JLabel("Topic:"), gbc);
		
		topicComboBox = new JComboBox<>();
		gbc.gridx = 1;
		gbc.gridy = 1;
		selectionPanel.add(topicComboBox, gbc);

		add(selectionPanel, BorderLayout.NORTH);
		
		materialListModel = new DefaultListModel<>();
		materialList = new JList<>(materialListModel);
		materialList.setBorder(BorderFactory.createTitledBorder("Materials"));
		add(new JScrollPane(materialList), BorderLayout.CENTER);
		
		JButton viewButton = new JButton("View Material");
		JPanel buttonPanel = new JPanel();
		buttonPanel.add(viewButton);
		add(viewButton, BorderLayout.SOUTH);
		
		courseComboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String selectedCourse = (String) courseComboBox.getSelectedItem();
				if (selectedCourse != null) {
					updateTopics(selectedCourse);
				}
			}
		});
		topicComboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String selectedCourse = (String) courseComboBox.getSelectedItem();
				String selectedTopic = (String) topicComboBox.getSelectedItem();
				if (selectedCourse != null && selectedTopic != null) {
					updateMaterials(selectedCourse, selectedTopic);
				}
			}
		});
		viewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String selectedMaterial = materialList.getSelectedValue();
				if (selectedMaterial != null) {
					showMaterialPopup(selectedMaterial);
				}
			}
		});
		updateCourses();
	}
	public static void addCourseMaterial(String courseName, String topic, List<File> materials) {
		courseMaterials.putIfAbsent(courseName, new HashMap<>());
		courseMaterials.get(courseName).put(topic, materials);
	}
	private void updateCourses() {
		courseComboBox.removeAllItems();
		for (String course : courseMaterials.keySet()) {
			courseComboBox.addItem(course);
		}
	}
	private void updateTopics(String courseName) {
		topicComboBox.removeAllItems();
		Map<String, List<File>> topics = courseMaterials.get(courseName);
		if (topics != null) {
			for (String topic : topics.keySet()) {
				topicComboBox.addItem(topic);
			}
		}
	}
	private void updateMaterials(String courseName, String topic) {
		materialListModel.clear();
		List<File> materials = courseMaterials.get(courseName).get(topic);
		if (materials != null) {
			for (File material : materials) {
				materialListModel.addElement(material.getName());
			}
		}
	}
	private void showMaterialPopup(String materialName) {
		JDialog dialog = new JDialog((Frame) null, "Material: " + materialName, true);
		dialog.setLayout(new BorderLayout(10, 10));
		
		JTextArea textArea = new JTextArea("Placeholder for the content of the material: " + materialName);
		textArea.setEditable(false);
		dialog.add(new JScrollPane(textArea), BorderLayout.CENTER);
		
		JButton downloadButton = new JButton("Download");
		downloadButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(dialog, "Material " + materialName + " has been downloaded.");
			}
		});
		dialog.add(downloadButton, BorderLayout.SOUTH);
		
		dialog.setSize(400, 300);
		dialog.setLocationRelativeTo(this);
		dialog.setVisible(true);
	}
}
