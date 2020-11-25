package de.dev4Ag.com.gps2kml.ui;

import de.dev4Ag.com.gps2kml.CSVLockedException;
import de.dev4Ag.com.gps2kml.GPSNotFoundException;
import de.dev4Ag.com.gps2kml.KMLLockedException;
import de.dev4Ag.com.gps2kml.cli.GPSInfoConverter;

import javax.swing.*;
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

  private String inputFileName = System.getProperty("user.home");


  public JPanel getWindowPane(){
    return this.windowPane;
  }

  public GPSInfoConverterUI() {
    convertButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        boolean exportKML = createKMLFileCheckBox.isSelected();
        boolean exportCSV = createCSVFileCheckBox.isSelected();
        boolean exportRawCSV = csvRawDataCheckBox.isSelected();
        boolean cleanData = cleanDataCheckBox.isSelected();
        boolean sortData = sortDataByTimeCheckBox.isSelected();
        try {
          GPSInfoConverter.convertData(inputFileName, exportCSV, exportRawCSV, exportKML, sortData, cleanData);
          JOptionPane.showMessageDialog(null,"Data successfully converted", "Success",JOptionPane.INFORMATION_MESSAGE);
        }  catch (GPSNotFoundException gpsNotFoundException) {
          JOptionPane.showMessageDialog(null, "Could not load GPS Data", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (KMLLockedException kmlLockedException) {
          JOptionPane.showMessageDialog(null, "Could not write KML File. Is it locked?", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (CSVLockedException csvLockedException) {
          JOptionPane.showMessageDialog(null,"Could not write CSV File. Is it locked (e.g. opened in Excel)?", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    });

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
  }

}
