package de.dev4Ag.com.gps2kml.ui;

import javax.swing.*;

public class GPSInfoUI {
  public static void main(String[] args) {
    try {
      UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    } catch (InstantiationException e) {
      e.printStackTrace();
    } catch (IllegalAccessException e) {
      e.printStackTrace();
    } catch (UnsupportedLookAndFeelException e) {
      e.printStackTrace();
    }
    JFrame jFrame = new JFrame("GPSInfo converter");
    jFrame.setContentPane(new GPSInfoConverterUI().getWindowPane());
    jFrame.pack();
    jFrame.setVisible(true);
    jFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
  }
}
