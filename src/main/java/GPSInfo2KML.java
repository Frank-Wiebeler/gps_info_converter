import agrirouter.technicalmessagetype.Gps;
import org.geotools.geojson.GeoJSON;
import org.geotools.geojson.geom.GeometryJSON;
import org.locationtech.jts.geom.LineString;

import java.io.*;
import java.sql.Timestamp;
import java.util.List;

public class GPSInfo2KML {

  public class GPSListEntry {
    public double latitude;
    public double longitude;
    public Timestamp timeStamp;
  }

  public static Gps.GPSList readGPSInfoProtobufFile(String name) throws IOException {
    InputStream inputStream = new FileInputStream(name);
    System.out.println("Could load file");
    Gps.GPSList gpsList = Gps.GPSList.parseFrom(inputStream);

    return gpsList;
  }

  public static void toKML(Gps.GPSList gpsList, String fileName) throws IOException {
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

      for(Gps.GPSList.GPSEntry entry: gpsList.getGpsEntriesList()){
          if(
            (entry.getPositionStatus() != Gps.GPSList.GPSEntry.PositionStatus.D_ERROR) &&
            (entry.getPositionStatus() != Gps.GPSList.GPSEntry.PositionStatus.D_NO_GPS) &&
            (entry.getPositionStatus() != Gps.GPSList.GPSEntry.PositionStatus.D_NOT_AVAILABLE)
          ) {
            fileContent += "" +
            entry.getPositionEast() + "," +
              entry.getPositionNorth() + "," +
              entry.getPositionUp() / 1000.0 + " \n";
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

  public static void main(String[] args) {
    String fileName = args[0];
    try {
      Gps.GPSList gpsList = readGPSInfoProtobufFile(fileName);
      for ( Gps.GPSList.GPSEntry entry: gpsList.getGpsEntriesList()) {
        System.out.println("Time" +  entry.getGpsUtcTimestamp().toString() + "Lat: "+ entry.getPositionNorth()+"  Long" +  entry.getPositionEast());
      }

      sortGPSList( gpsList);
      toKML(gpsList, fileName.replace(".bin",".kml"));

    } catch (IOException exception){
      System.out.println("IO-Exception");
    }
  }

  private static void sortGPSList(Gps.GPSList gpsList) {

  }
}
