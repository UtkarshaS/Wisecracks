//package data;
//
//import org.xmlpull.v1.XmlPullParser;
//import org.xmlpull.v1.XmlPullParserException;
//import org.xmlpull.v1.XmlPullParserFactory;
//
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.InputStreamReader;
//import java.io.UnsupportedEncodingException;
//import java.net.HttpURLConnection;
//import java.net.MalformedURLException;
//import java.net.URL;
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.Comparator;
//import java.util.HashSet;
//import java.util.List;
//
////fwJHrZnVjPq2P2Mw
//
//public class EventfulApiIntegration {
//
//    private InputStream input = null;
//    private String apiKey = "JwLLMcQtwnSz3Nmg";
//
//    public EventfulApiIntegration() {
//    }
//
//    public static void main(String... args) {
//        EventfulApiIntegration he = new EventfulApiIntegration();
//        // for (EventfulEventDto e : he.searchCity("New York")) {
//        // System.out.println(e.getTitle());
//        // System.out.println(e.getStartTime());
//        // }
//
//        // New york Lat-Long
//        for (EventfulEventDto e : he.searchNearbyEvents(40.7127, -74.0059)) {
//            System.out.println(e.getTitle());
//            System.out.println(e.getCity());
//            System.out.println(e.getStartTime());
//        }
//    }
//
//    public List<EventfulEventDto> searchCity(String city) {
//        ArrayList<EventfulEventDto> resultEvents = new ArrayList<EventfulEventDto>();
//        String correctString = replaceChars(city, " ", "+");
//        try {
//            URL url = new URL("http://api.eventful.com/rest/events/search?app_key=" + apiKey + "&location="
//                    + correctString + "&date=Future" + "&page_size=50");
//            resultEvents = getEvents(url);
//        } catch (MalformedURLException e) {
//            e.printStackTrace();
//        }
//        return sortEventsByAscendingDate(resultEvents);
//    }
//
//    public List<EventfulEventDto> searchCityCategory(String city, String category) {
//        ArrayList<EventfulEventDto> resultEvents = new ArrayList<EventfulEventDto>();
//        String correctString = replaceChars(city, " ", "+");
//        try {
//            URL url = new URL("http://api.eventful.com/rest/events/search?app_key=" + apiKey + "&location="
//                    + correctString + "&date=Future" + "&category=" + category + "&page_size=50");
//            resultEvents = getEvents(url);
//        } catch (MalformedURLException e) {
//            e.printStackTrace();
//        }
//        return sortEventsByAscendingDate(resultEvents);
//    }
//
//    public List<EventfulEventDto> searchNearbyEvents(double latitude, double longitude) {
//        ArrayList<EventfulEventDto> resultEvents = new ArrayList<EventfulEventDto>();
//        try {
//            URL url = new URL("http://api.eventful.com/rest/events/search?app_key=" + apiKey + "&location=" + latitude
//                    + "," + longitude + "&date=Future" + "&within=20&units=km&page_size=50");
//            resultEvents = getEvents(url);
//            System.out.println(resultEvents.size());
//        } catch (MalformedURLException e) {
//            e.printStackTrace();
//        }
//        return sortEventsByAscendingDate(resultEvents);
//    }
//
//    private List<EventfulEventDto> sortEventsByAscendingDate(List<EventfulEventDto> events) {
//        Collections.sort(events, new Comparator<EventfulEventDto>() {
//            @Override
//            public int compare(EventfulEventDto o1, EventfulEventDto o2) {
//                return o1.getStartTime().compareTo(o2.getStartTime());
//            }
//        });
//        return events;
//
//    }
//
//    private String replaceChars(String source, String from, String to) {
//        StringBuilder dest = new StringBuilder(source);
//        for (int i = 0; i < source.length(); i++) {
//            int foundAt = from.indexOf(source.charAt(i));
//            if (foundAt >= 0)
//                dest.setCharAt(i, to.charAt(foundAt));
//        }
//        return "" + dest;
//    }
//
//    private ArrayList<EventfulEventDto> readTransactionsEvents(XmlPullParser parser) throws IOException,
//            XmlPullParserException {
//        HashSet<String> hSet = new HashSet<String>();
//        ArrayList<EventfulEventDto> list = new ArrayList<EventfulEventDto>();
//        String name = "", title = "", description = "", startTime = "", venueName = "", venueAddress = "", city = "", country = "", latitude = "", longitude;
//        int eventType = parser.getEventType();
//        while (eventType != XmlPullParser.END_DOCUMENT) {
//            if (eventType == XmlPullParser.START_TAG) {
//                name = parser.getName();
//            } else if (eventType == XmlPullParser.END_TAG) {
//                name = "";
//            } else if (eventType == XmlPullParser.TEXT) {
//                if (name.equals("title")) {
//                    title = parser.getText();
//                } else if (name.equals("description")) {
//                    description = parser.getText();
//                } else if (name.equals("start_time")) {
//                    startTime = parser.getText();
//                } else if (name.equals("venue_name")) {
//                    venueName = parser.getText();
//                } else if (name.equals("venue_address")) {
//                    venueAddress = parser.getText();
//                } else if (name.equals("city_name")) {
//                    city = parser.getText();
//                } else if (name.equals("country_name")) {
//                    country = parser.getText();
//                } else if (name.equals("latitude")) {
//                    latitude = parser.getText();
//                } else if (name.equals("longitude")) {
//                    longitude = parser.getText();
//                    if (!hSet.contains(title)) {
//                        list.add(new EventfulEventDto(title, description, startTime, venueName, venueAddress, city,
//                                country, latitude, longitude));
//                        hSet.add(title);
//                    }
//                }
//            }
//
//            eventType = parser.next();
//        }
//        input.close();
//        return list;
//    }
//
//    protected ArrayList<EventfulEventDto> getEvents(URL url) {
//        ArrayList<EventfulEventDto> resultEvents = new ArrayList<EventfulEventDto>();
//        HttpURLConnection connection;
//        try {
//            connection = (HttpURLConnection) url.openConnection();
//            input = connection.getInputStream();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        XmlPullParserFactory factory;
//        try {
//            factory = XmlPullParserFactory.newInstance();
//            factory.setNamespaceAware(true);
//            XmlPullParser parser = factory.newPullParser();
//            parser.setInput(new InputStreamReader(input, "UTF-8"));
//            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
//            resultEvents = readTransactionsEvents(parser);
//
//        } catch (XmlPullParserException e1) {
//            // TODO Auto-generated catch block
//            e1.printStackTrace();
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return resultEvents;
//    }
//}
