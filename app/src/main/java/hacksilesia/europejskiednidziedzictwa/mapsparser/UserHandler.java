package hacksilesia.europejskiednidziedzictwa.mapsparser;

import android.util.Log;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;

public class UserHandler extends DefaultHandler {

    String folderType = "";

    String folderTag = "close";
    String nameTag = "close";
    String placeMarkerTag = "close";
    String coordinatesTag = "close";

    public ArrayList<MapLocation> mapLocations = new ArrayList<MapLocation>();
    public ArrayList<MapPath> mapPaths = new ArrayList<MapPath>();
    public ArrayList<MapRiddle> mapRiddles = new ArrayList<MapRiddle>();

    int locationIterator = 0;
    int pathsIterator = 0;
    int riddleIterator = 0;

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        Log.d("PATHEEE", "start");
        if (qName.equalsIgnoreCase("folder")) {
            folderTag = "open";
        }
        if(qName.equalsIgnoreCase("name")){
            nameTag = "open";
        }
        if(qName.equalsIgnoreCase("placeMark")){
            placeMarkerTag = "open";
        }
        if(qName.equalsIgnoreCase("coordinates")){
            coordinatesTag = "open";
        }
    }
    @Override
    public void characters(char ch[], int start, int length) throws SAXException {
        if (nameTag.equalsIgnoreCase("open") && folderTag == "open"){
            if(placeMarkerTag == "close"){
                folderType = new String(ch, start, length);
                Log.d("PATHEEE", "FolderName = "+ folderType + "\n");
            } else {
                String nodeName = new String(ch, start, length);
                if(folderType.equalsIgnoreCase("points")){
                    Log.d("PATHEEE", "New Point with name: " + nodeName + "\n");
                } else if(folderType.equalsIgnoreCase("path")){
                    Log.d("PATHEEE", "New Path segment: " + nodeName + "\n");

                    MapPath p = new MapPath(pathsIterator);
                    mapPaths.add(p);
                    pathsIterator++;
                }
            }
        }
        if(coordinatesTag == "open"){
            Log.d("PATHEEE", "Coords: " + new String(ch, start, length) + "\n");


            if(folderType.equalsIgnoreCase("points")){
                String[] loc = new String(ch, start, length).split(",");

                MapLocation l = new MapLocation(Float.parseFloat(loc[0]), Float.parseFloat(loc[1]), locationIterator);
                mapLocations.add(l);
                locationIterator++;
            }
            if(folderType.equalsIgnoreCase("path")){
                String[] ps = new String(ch, start, length).split(" ");

                for(int i=0; i<ps.length; i++) {
                    String[] q = ps[i].split(",");
                    MapPoint p = new MapPoint(Float.parseFloat(q[0]), Float.parseFloat(q[1]));
                    mapPaths.get(mapPaths.size() - 1).coordinates.add(p);
                }
            }
            if(folderType.equalsIgnoreCase("riddle")){
                String[] loc = new String(ch, start, length).split(",");

                MapRiddle r = new MapRiddle(Float.parseFloat(loc[0]), Float.parseFloat(loc[1]), riddleIterator);
                riddleIterator++;
                mapRiddles.add(r);
            }
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if (qName.equalsIgnoreCase("folder")) {
            folderTag = "close";
            folderType = "";
        }
        if(qName.equalsIgnoreCase("name")){
            nameTag = "close";
        }
        if(qName.equalsIgnoreCase("placeMark")){
            placeMarkerTag = "close";
        }
        if(qName.equalsIgnoreCase("coordinates")){
            coordinatesTag = "close";
        }
    }
}