package de.dev4Ag.gpsinfoconverter.ui;

import de.dev4Ag.gpsinfoconverter.CSVLockedException;
import de.dev4Ag.gpsinfoconverter.GPSNotFoundException;
import de.dev4Ag.gpsinfoconverter.KMLLockedException;
import de.dev4Ag.gpsinfoconverter.cli.GPSInfoConverter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class GPSInfoConverterUI {
  private JCheckBox createKMLFileCheckBox;
  private JCheckBox createCSVFileCheckBox;
  private JButton fileSelectStringButton;
  private JTextField filePathStringField;
  private JCheckBox cleanDataCheckBox;
  private JCheckBox sortDataByTimeCheckBox;
  private JButton convertButton;
  private JPanel windowPane;
  private JCheckBox csvRawDataCheckBox;
  private JTextField seperatorTextField;

  private String inputFileName = System.getProperty("user.home");


  public JPanel getWindowPane() {
    System.out.println("WindowPane is" + this.windowPane);
    return this.windowPane;
  }

  public GPSInfoConverterUI() {
    fileSelectStringButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        JFileChooser jFileChooser = new JFileChooser();
        jFileChooser.setDialogTitle("Select Output DataSets Folder");
        jFileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        jFileChooser.setCurrentDirectory(new File(inputFileName));
        jFileChooser.setSelectedFile(new File(inputFileName));
        if (jFileChooser.showDialog(windowPane.getComponent(1), "Open") == JFileChooser.APPROVE_OPTION) {
          File file = jFileChooser.getSelectedFile();
          inputFileName = file.getAbsolutePath();
          filePathStringField.setText(inputFileName);
        }

      }
    });

    convertButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        inputFileName = filePathStringField.getText();
        boolean exportKML = createKMLFileCheckBox.isSelected();
        boolean exportCSV = createCSVFileCheckBox.isSelected();
        boolean exportRawCSV = csvRawDataCheckBox.isSelected();
        boolean cleanData = cleanDataCheckBox.isSelected();
        boolean sortData = sortDataByTimeCheckBox.isSelected();
        GPSInfoConverter.setSplitter(seperatorTextField.getText());
        try {
          GPSInfoConverter.convertData(inputFileName, exportCSV, exportRawCSV, exportKML, sortData, cleanData);
          JOptionPane.showMessageDialog(null, "Data successfully converted", "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (GPSNotFoundException gpsNotFoundException) {
          JOptionPane.showMessageDialog(null, "Could not load GPS Data", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (KMLLockedException kmlLockedException) {
          JOptionPane.showMessageDialog(null, "Could not write KML File. Is it locked?", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (CSVLockedException csvLockedException) {
          JOptionPane.showMessageDialog(null, "Could not write CSV File. Is it locked (e.g. opened in Excel)?", "Error", JOptionPane.ERROR_MESSAGE);
        }
      }
    });

  }

  {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
    $$$setupUI$$$();
  }

  /**
   * Method generated by IntelliJ IDEA GUI Designer
   * >>> IMPORTANT!! <<<
   * DO NOT edit this method OR call it in your code!
   *
   * @noinspection ALL
   */
  private void $$$setupUI$$$() {
    windowPane = new JPanel();
    windowPane.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(6, 4, new Insets(0, 0, 0, 0), -1, -1));
    fileSelectStringButton = new JButton();
    fileSelectStringButton.setText("...");
    windowPane.add(fileSelectStringButton, new com.intellij.uiDesigner.core.GridConstraints(0, 3, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    createKMLFileCheckBox = new JCheckBox();
    createKMLFileCheckBox.setText("Create KML File");
    windowPane.add(createKMLFileCheckBox, new com.intellij.uiDesigner.core.GridConstraints(2, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    filePathStringField = new JTextField();
    windowPane.add(filePathStringField, new com.intellij.uiDesigner.core.GridConstraints(0, 1, 1, 2, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
    final JLabel label1 = new JLabel();
    label1.setText("Select Path");
    windowPane.add(label1, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    cleanDataCheckBox = new JCheckBox();
    cleanDataCheckBox.setText("Clean Data");
    windowPane.add(cleanDataCheckBox, new com.intellij.uiDesigner.core.GridConstraints(3, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    sortDataByTimeCheckBox = new JCheckBox();
    sortDataByTimeCheckBox.setText("sortData by Time");
    windowPane.add(sortDataByTimeCheckBox, new com.intellij.uiDesigner.core.GridConstraints(4, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    convertButton = new JButton();
    convertButton.setText("Convert");
    windowPane.add(convertButton, new com.intellij.uiDesigner.core.GridConstraints(5, 0, 1, 4, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    createCSVFileCheckBox = new JCheckBox();
    createCSVFileCheckBox.setText("Create CSV File");
    windowPane.add(createCSVFileCheckBox, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    csvRawDataCheckBox = new JCheckBox();
    csvRawDataCheckBox.setText("Raw Data");
    windowPane.add(csvRawDataCheckBox, new com.intellij.uiDesigner.core.GridConstraints(1, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    final JLabel label2 = new JLabel();
    label2.setHorizontalAlignment(4);
    label2.setText("Seperator");
    windowPane.add(label2, new com.intellij.uiDesigner.core.GridConstraints(1, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_EAST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(299, 16), null, 0, false));
    seperatorTextField = new JTextField();
    seperatorTextField.setText(",");
    windowPane.add(seperatorTextField, new com.intellij.uiDesigner.core.GridConstraints(1, 3, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(40, -1), null, 1, false));
  }

  /**
   * @noinspection ALL
   */
  public JComponent $$$getRootComponent$$$() {
    return windowPane;
  }

}
