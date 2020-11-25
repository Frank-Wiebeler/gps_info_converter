package de.dev4Ag.com.gps2kml.cli;

import agrirouter.technicalmessagetype.Gps;
import com.google.protobuf.Timestamp;
import de.dev4Ag.com.gps2kml.CSVLockedException;
import de.dev4Ag.com.gps2kml.GPSNotFoundException;
import de.dev4Ag.com.gps2kml.KMLLockedException;

import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class GPSInfoConverter {
  private static final double ALTITUDE_FACTOR = (1.0/1000);
  private static final double WGS84_POSITION_FACTOR = (1.0/10000000);
  private static final int MILLISECONDS_TO_SECONDS = 1000;

  private static boolean exportKML;
  private static boolean rawCSV;
  private static boolean exportCSV;
  private static boolean sortData;
  private static boolean cleanData;
  private static String importfileName;
  private static DateFormat df =  new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");

  public static class GPSListEntry implements  Comparable<GPSListEntry>{
    public double latitude;
    public double longitude;
    public long altitude;
    public Gps.GPSList.GPSEntry.PositionStatus status;
    public Timestamp timeStamp;
    public int numberOfSatellites;
    public double pdop;
    public double hdop;

    public int compareTo(GPSListEntry compare) {
      return (int)(compare.timeStamp.getSeconds() - this.timeStamp.getSeconds());
    }
  }

  public static Gps.GPSList readGPSInfoProtobufFile(String name) throws IOException {
    InputStream inputStream = new FileInputStream(name);
    System.out.println("Could load file");
    Gps.GPSList gpsList = Gps.GPSList.parseFrom(inputStream);
    System.out.println("Loaded File; Size of list: " + gpsList.getGpsEntriesList().size());
    return gpsList;
  }


  private static List<GPSListEntry> convertProtobufListToOwnList(Gps.GPSList gpsList) {
    List<GPSListEntry> sortedGPSList = new ArrayList<GPSListEntry>();
    for ( Gps.GPSList.GPSEntry entry: gpsList.getGpsEntriesList()
    ) {
      GPSListEntry gpsListEntry = new GPSListEntry();
      gpsListEntry.altitude = entry.getPositionUp();
      gpsListEntry.longitude = entry.getPositionEast();
      gpsListEntry.latitude = entry.getPositionNorth();
      gpsListEntry.status = entry.getPositionStatus();
      gpsListEntry.timeStamp = entry.getGpsUtcTimestamp();
      gpsListEntry.numberOfSatellites = entry.getNumberOfSatellites();
      gpsListEntry.pdop = entry.getPdop();
      gpsListEntry.hdop = entry.getHdop();
      sortedGPSList.add(gpsListEntry);
    }

    return sortedGPSList;
  }


  private static List<GPSListEntry> sortGPSData(List<GPSListEntry> sortedGPSList) {

    Collections.sort(sortedGPSList);

    return sortedGPSList;
  }

  private static void cleanGPSData(List<GPSListEntry> sortedGPSList) {
    List<GPSListEntry> entriesToRemove = new ArrayList<GPSListEntry>();
    for(GPSListEntry gpsListEntry : sortedGPSList){
      if(
        gpsListEntry.timeStamp.getSeconds() == 0 ||
        gpsListEntry.status == Gps.GPSList.GPSEntry.PositionStatus.D_ERROR ||
        gpsListEntry.status == Gps.GPSList.GPSEntry.PositionStatus.D_NO_GPS ||
        gpsListEntry.status == Gps.GPSList.GPSEntry.PositionStatus.UNRECOGNIZED){

          entriesToRemove.add(gpsListEntry);
      }
    }
    sortedGPSList.removeAll(entriesToRemove);
  }



  public static void toKML(List<GPSListEntry> gpsList, String fileName) throws IOException {
    String fileContent="<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
      "<kml xmlns=\"http://www.opengis.net/kml/2.2\">\n" +
      "  <Document>\n" +
      "    <name>Paths</name>\n" +
      "    <description>Driving Route exported from gps:info files.</description>\n" +
      "    <Style id=\"yellowLineGreenPoly\">\n" +
      "      <LineStyle>\n" +
      "        <color>7f00ffff</color>\n" +
      "        <width>4</width>\n" +
      "      </LineStyle>\n" +
      "      <PolyStyle>\n" +
      "        <color>7f00ff00</color>\n" +
      "      </PolyStyle>\n" +
      "    </Style>\n" +
      "    <Placemark>\n" +
      "      <name>Absolute Extruded</name>\n" +
      "      <description>Transparent green wall with yellow outlines</description>\n" +
      "      <styleUrl>#yellowLineGreenPoly</styleUrl>\n" +
      "      <LineString>\n" +
      "        <extrude>1</extrude>\n" +
      "        <tessellate>1</tessellate>\n" +
      "        <coordinates> ";

      for(GPSListEntry entry: gpsList){
          if(
            (entry.status != Gps.GPSList.GPSEntry.PositionStatus.D_ERROR) &&
            (entry.status != Gps.GPSList.GPSEntry.PositionStatus.D_NO_GPS) &&
            (entry.status != Gps.GPSList.GPSEntry.PositionStatus.D_NOT_AVAILABLE)
          ) {
            fileContent = fileContent +
              entry.longitude + "," +
              entry.latitude + "," +
              entry.altitude * ALTITUDE_FACTOR + " \n";
          }
      }

      fileContent = fileContent + "        </coordinates>\n" +
      "      </LineString>\n" +
      "    </Placemark>\n" +
      "  </Document>\n" +
      "</kml>";

    BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
    writer.write(fileContent);

    writer.close();
  }

  public static String getCSVLine( GPSListEntry gpsListEntry){
    Date timeStampDate = new Date(gpsListEntry.timeStamp.getSeconds()*MILLISECONDS_TO_SECONDS);
    return df.format(timeStampDate) + ";" +
      gpsListEntry.latitude  +";"+
      gpsListEntry.longitude + ";" +
      gpsListEntry.altitude * ALTITUDE_FACTOR + ";" +
      gpsListEntry.status.name() + ";" +
      gpsListEntry.numberOfSatellites + ";" +
      gpsListEntry.pdop + ";" +
      gpsListEntry.hdop + ";\n";
  }

  public static String getRawCSVLine( GPSListEntry gpsListEntry){
    return gpsListEntry.timeStamp.getSeconds() + ";" +
            gpsListEntry.latitude +";"+
            gpsListEntry.longitude + ";" +
            gpsListEntry.altitude + ";" +
            gpsListEntry.status.name() + ";" +
            gpsListEntry.numberOfSatellites + ";" +
            gpsListEntry.pdop + ";" +
            gpsListEntry.hdop + ";\n";
  }
  public static void toCSV(List<GPSListEntry> gpsListEntryList, String fileName, boolean rawCSVData) throws IOException {

    String fileContent = "timeStamp;latitude;longitude;altitude;status;numberOfSatellites;pdop;hdop\n";
    for (GPSListEntry gpsListEntry: gpsListEntryList) {
      if( rawCSVData) {
        fileContent = fileContent + getRawCSVLine(gpsListEntry);
      } else {
        fileContent = fileContent + getCSVLine(gpsListEntry);
      }
    }


    BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));

    writer.write(fileContent);
    writer.close();
  }


  public static void convertData(
    String importfileName,
    boolean createCSV, boolean rawCSVData, boolean createKML, boolean sortData, boolean cleanData
  ) throws GPSNotFoundException, KMLLockedException, CSVLockedException {
    Gps.GPSList gpsList;
    try {
      gpsList = readGPSInfoProtobufFile(importfileName);
    } catch (FileNotFoundException e) {
      throw new GPSNotFoundException();
    } catch (IOException e) {
      throw new GPSNotFoundException();
    }

    /*
    for ( Gps.GPSList.GPSEntry entry: gpsList.getGpsEntriesList()) {
        System.out.println("Time" +  entry.getGpsUtcTimestamp().toString() + "Lat: "+ entry.getPositionNorth()+"  Long" +  entry.getPositionEast());
    }*/

      List<GPSListEntry> sortedGPSList= convertProtobufListToOwnList( gpsList);
      if( sortData) {
        sortGPSData(sortedGPSList);
      }

      if( cleanData){
        cleanGPSData(sortedGPSList);
      }

      if( createKML) {
        try {
          toKML(sortedGPSList, importfileName.replace(".bin", ".kml"));
        } catch (IOException e) {
          throw new KMLLockedException();
        }
      }

      if( createCSV) {
        try {
          toCSV(sortedGPSList, importfileName.replace(".bin", ".csv"), rawCSVData);
        } catch (IOException e) {
          throw new CSVLockedException();
        }
      }
  }

  public static void main(String[] args) {
    importfileName = args[0];

    for( String argument : args){
      if(argument.equals("-kml") ){
        exportKML = true;
      } else if ( argument.equals("-csv")){
        exportCSV = true;
      } else if ( argument.equals("-sort")){
        sortData = true;
      } else if ( argument.equals("-clean")){
        cleanData = true;
      } else if ( argument.equals( "-raw")){
        rawCSV = true;
      }
    }
    try {
      convertData(importfileName, exportCSV, rawCSV, exportKML, sortData, cleanData);
    } catch (GPSNotFoundException e) {
      System.out.println("Could not load GPS Data");
    } catch (KMLLockedException e) {
      System.out.println("Could not write KML File. Is it locked?");
    } catch (CSVLockedException e) {
      System.out.println("Could not write CSV File. Is it locked (e.g. opened in Excel)?");
    }

  }

}