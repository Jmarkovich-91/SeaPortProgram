/*
* File Name: SeaPortProgram.java
* Date: March 14, 2017
* Author: Joshua Markovich
* Purpose: To simulate the aspects of a Sea Port, and to create a GUI.
 */
package seaportprogram;

import java.awt.*;
import javax.swing.*;
import java.util.*;
import java.io.*;

public class SeaPortProgram extends JFrame {

    private World world;
    private Scanner sc;

    //Declare GUI components
    private JTextArea textArea = new JTextArea();
    private JTree tree = new JTree();
    private JScrollPane scrollPane = new JScrollPane(textArea);
    private JButton readButton = new JButton("Read");
    private JButton searchButton = new JButton("Search");
    private JButton displayButton = new JButton("Display");
    private JButton sortButton = new JButton("Sort By");
    private JLabel searchLabel = new JLabel("Search Target");
    private JTextField textField = new JTextField(10);
    private String[] comboList = {"Index", "Skill", "Name", "Ship Length Greater Than"};
    private JComboBox comboBox = new JComboBox(comboList);
    private String[] comboList2 = {"Weight", "Length", "Width", "Draft", "Name"};
    private JComboBox comboBox2 = new JComboBox(comboList2);
    private JFileChooser jfc = new JFileChooser(".");
    private JScrollPane treePane = new JScrollPane ();
    private JScrollPane jobPane = new JScrollPane ();
    private JPanel jobPanel = new JPanel ();
    private JSplitPane splitPane;
    private JTabbedPane tabPane = new JTabbedPane ();
    private JScrollPane resourcePane = new JScrollPane ();
    private JPanel resourcePanel = new JPanel ();

    //Private method to set settings for GUI
    private void setFrame(int width, int height) {
        setSize(width, height);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public SeaPortProgram(String title, int width, int height) {

        super(title);
        setFrame(width, height);
        setLayout(new BorderLayout());
        setVisible(true);
        textArea.setEditable (false);
        
        jobPanel.setLayout (new GridLayout (0, 5));
        jobPane.getViewport().add(jobPanel);
        resourcePanel.setLayout (new GridLayout (0, 3));
        resourcePane.getViewport().add(resourcePanel);
        tabPane.addTab("World", scrollPane);
        tabPane.addTab("Tree", treePane);
        tabPane.addTab("Resources", resourcePane);
        splitPane = new JSplitPane (JSplitPane.HORIZONTAL_SPLIT, tabPane, jobPane);
        splitPane.setDividerLocation (600);
        add(splitPane, BorderLayout.CENTER);
        
        JPanel panel = new JPanel();
        panel.add(readButton);
        panel.add(displayButton);
        panel.add(sortButton);
        panel.add(comboBox2);
        panel.add(searchLabel);
        panel.add(textField);
        panel.add(comboBox);
        panel.add(searchButton);
        add(panel, BorderLayout.PAGE_START);

        readButton.addActionListener(e -> readFile());
        displayButton.addActionListener(e -> display());
        searchButton.addActionListener(e -> search((String) comboBox.getSelectedItem(), textField.getText()));
        sortButton.addActionListener(e -> sort((String) comboBox2.getSelectedItem()));
        
        validate();
    }

    //User selects file, structure is built
    public void readFile() {
        jobPanel.removeAll();
        HashMap<Integer, SeaPort> hmp = new HashMap<Integer, SeaPort> ();
        HashMap<Integer, Dock> hmd = new HashMap<Integer, Dock> ();
        HashMap<Integer, Ship> hms = new HashMap<Integer, Ship> ();
        try {
            if (jfc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                world = new World(sc = new Scanner(jfc.getSelectedFile()), hmp, hmd, hms, jobPanel, resourcePanel);
                for (int i = 0; i < world.ports.size(); i++) {
                    world.ports.get(i).displayResources();
                }
            } else {
                JOptionPane.showMessageDialog(null, "Please Select a File");
            }
        } catch (IOException i) {
            JOptionPane.showMessageDialog(null, "Error Opening the File");
        }
        this.repaint();
        this.revalidate();
    }

    //Method to display structure
    public void display() {
        tree = new JTree(world.createNode("The World"));
        treePane.getViewport().add(tree);
        textArea.setText(null);
        this.repaint();
        this.revalidate();
        if (world == null) {
            JOptionPane.showMessageDialog(null, "Please Select a File First");
        } else {
            textArea.append("" + world.toString());
        }
    }

    //Searches based on inputs
    public void search(String type, String target) {
        textArea.setText(null);
        if (world == null) {
            JOptionPane.showMessageDialog(null, "Please Select a File First");
        } else {
            textArea.append("Search Results:\n" + world.search(type, target));
        }
    }
    
    public void sort (String type) {
        textArea.setText(null);
        if (world == null) {
            JOptionPane.showMessageDialog(null, "Please Select a File First");
        } else {
            textArea.append(world.sort(type));
        }
    }

    public static void main(String[] args) {

        SeaPortProgram sp = new SeaPortProgram("Sea Port Program", 1200, 500);

    }

}
