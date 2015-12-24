package main.java.wisecracks.data;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import twitter4j.Location;
import twitter4j.ResponseList;
import twitter4j.Trend;
import twitter4j.Trends;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;
//import de.steffbo.twitter.trends.misc.TrendProperties;

public class TweetTrends {
    private Twitter twitter;
    
    private String jSTrend_Place = "https://api.twitter.com/1.1/trends/place.json?";//Trending topics by place
    private String woeid_London = "id=23424975,";
    public TweetTrends() throws TwitterException {
    //Properties props = TrendProperties.getInstance().getProperties();
    
    ConfigurationBuilder cb = new ConfigurationBuilder();
    cb.setDebugEnabled(true).setOAuthConsumerKey("RfyST5fcyRRKwL4jQ6npTEVvH")
            .setOAuthConsumerSecret("MnosqoNJoyAktkHqpvMZ5Fil9pdMzUfYBv1p3jl7yWx3pViuEH")
            .setOAuthAccessToken("290446932-B9l2hxt6eRAiaR8vwOBnSmCxRpvkSFdwgdWuBv5Z")
            .setOAuthAccessTokenSecret("AUATTGFuFeVAAcWoMgUSSjhpoAv4fYzurcoCprAywURCR");

    TwitterFactory tf = new TwitterFactory(cb.build());
    twitter = tf.getInstance();
    
    }
    public Trends getTrends() throws TwitterException {
    return twitter.getPlaceTrends(2459115);
       }
    
    public ResponseList<Location> getLocations() throws TwitterException {
    return twitter.getAvailableTrends();
       }
  
    public static void main(String[] args) throws TwitterException{ 
    
    TweetTrends tt = new TweetTrends();
    System.out.println(tt.getTweetTrends());
      }
      
    
    public ArrayList<String> getTweetTrends() throws TwitterException {
    ArrayList<String> tweetTrends = new ArrayList<String>();
    Trends trends = twitter.getPlaceTrends(23424975);
    for (int i = 0; i < trends.getTrends().length; i++) {
        String tmp = trends.getTrends()[i].getName();
        tweetTrends.add(tmp);
        //System.out.println(tmp);
    }
    return tweetTrends;
    }
}
