import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;

import twitter4j.*;
import twitter4j.conf.ConfigurationBuilder;

import opennlp.tools.namefind.NameFinderME;
import opennlp.tools.namefind.TokenNameFinderModel;
import opennlp.tools.tokenize.Tokenizer;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;
import opennlp.tools.util.Span;

public class Retriv {

    /**
     * @param args
     */
    private static ConfigurationBuilder cb;

    public static void main(String[] args) {
        try {


            // TODO Auto-generated method stub
            System.out.println("Twitter Streaming");
            cb = new ConfigurationBuilder();
            cb.setDebugEnabled(true);
            cb.setOAuthConsumerKey("ne4FoT4YBa8MlQVs6PQ1bYTn2");
            cb.setOAuthConsumerSecret("XTeQAr9pXYFRQXxNomYsSJGbCCyO0p8yGLpdXWcw92W4S957iq");
            cb.setOAuthAccessToken("187084317-ebk6kR3QXbl41PVZASgRBTp4Wlawsyyj3j9R0LqP");
            cb.setOAuthAccessTokenSecret("4uWMOQpnujnL5zYb6a2Qc8VbTC5oloyU31GaDBNXzj9yp");

            TwitterStream t_new = new TwitterStreamFactory(cb.build()).getInstance();

            InputStream modelInToken = null;
            InputStream modelIn = null;
            modelInToken = new FileInputStream("/home/kalluri/Downloads/REAL-TIME BIGDATA/HackathonMusic/src/main/resources/en-token.bin");
            TokenizerModel modelToken = new TokenizerModel(modelInToken);
           final Tokenizer tokenizer = new TokenizerME(modelToken);
            //2. find names
            modelIn = new FileInputStream("/home/kalluri/Downloads/REAL-TIME BIGDATA/HackathonMusic/src/main/resources/en-ner-person.bin");
            TokenNameFinderModel model = new TokenNameFinderModel(modelIn);
           final NameFinderME nameFinder = new NameFinderME(model);

            StatusListener s_new = new StatusListener() {


                @Override
                public void onException(Exception ex) {
                    // TODO Auto-generated method stub
                    ex.printStackTrace();
                }

                @Override
                public void onDeletionNotice(StatusDeletionNotice sdn) {
                    // TODO Auto-generated method stub
                    //System.out.println("Got a status deletion notice id:" + sdn.getStatusId());
                }

                public void onTrackLimitationNotice(int i) {

                }

                @Override
                public void onScrubGeo(long userId, long upToStatusId) {
                    // TODO Auto-generated method stub
                    //System.out.println("Got scrub_geo event userId:" + userId + " upToStatusId:" + upToStatusId);
                }

                @Override
                public void onStallWarning(StallWarning arg0) {
                    // TODO Auto-generated method stub

                }

                @Override
                public void onStatus(Status status) {
                    String song=null;
                    String artist= null;
                    String a=null;
                    String b = null;
                    //System.out.println(status.getLang());
                    if (status.getLang().equalsIgnoreCase("en")) {

                        // TODO Auto-generated method stub
                        System.out.println("@" + status.getUser().getScreenName() + " - " + status.getText());


                        String s = "id: @" + status.getId() + "retweets: @" + status.getRetweetCount() + "likes: @" + status.getFavoriteCount() + " User: @" + status.getUser().getScreenName() + " Tweet: " + status.getText() + " Language: " + status.getLang() + "\n";
                        System.out.println(s);
                        SentimentAnalyzer sa = new SentimentAnalyzer();
                        String senti = sa.findSentiment(status.getText());
                        String user = status.getUser().getScreenName();
                        String tweet = status.getText();
                        String half = tweet.substring(tweet.indexOf("#NowPlaying ") + 12);
                        if(half.contains("by"))
                        {
                            String[] sechalf = half.split("by");
                             a = sechalf[0];
                            b=sechalf[1];
                             String[] split = a.split(" ");
                             int c = split.length;
                            song = split[c-2]+" "+split[c-1];
                            System.out.println(song);
                            String[] splits = b.split(" ");
                            //int e = splits.length;
                            artist = splits[1]+" "+splits[2];
                            System.out.println(artist);

                        }
                        else if (half.contains("-"))
                        {
                            String[] thirdhalf = half.split("-");
                            a = thirdhalf[0];
                            b=thirdhalf[1];
                            String[] split = a.split(" ");
                            int c = split.length;
                            artist = split[c-1]+" "+split[c];
                            System.out.println(artist);
                            String[] splits = b.split(" ");
                            //int e = splits.length;
                            song = splits[1]+" "+splits[2];
                            System.out.println(song);
                        }

                        else{

                        }

                        int likes = status.getFavoriteCount();
                        int retweets = status.getRetweetCount();
                        long id = status.getId();
                        String longid = Long.toString(id);
                        System.out.println(senti);

                        String tokens[] = tokenizer.tokenize(tweet);
                        Span nameSpans[] = nameFinder.find(tokens);
                        System.out.println("nameSpans");
                        String names = Arrays.toString(Span.spansToStrings(nameSpans, tokens));
                        System.out.println(names);

                        /*for (int i = 0; i < nameSpans.length; i++) {
                             names= nameSpans[i].toString();
                            System.out.println(names);
                        }*/


                        insertIntoMongoDB(artist,song,names,longid, user, tweet,  senti,""+ likes,""+ retweets);


                    }


                }
            };
            FilterQuery fq = new FilterQuery();
            String keywords[] = {"#NowPlaying"};
            fq.track(keywords);
            t_new.addListener(s_new);
            t_new.filter(fq);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    public static void insertIntoMongoDB(String artist,String song,String names, String longid, String user, String tweet, String sentiment, String likes, String retweets) {
        try {


            URL url = new URL("https://api.mongolab.com/api/1/databases/cs5543/collections/TwitterSentiment?apiKey=jwTtZxj0UFvt_JDGFsAcPUfPT_jBY_JA");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");


            String input = "{\"artist\":\""+artist+"\",\"song\":\""+song+"\",\"names\":\"" + names + "\",\"retweets\":\"" + retweets + "\",\"longid\":\"" + longid + "\",\"likes\":\"" + likes + "\",\"user\":\"" + user + "\",\"tweet\":\"" + tweet + "\",\"sentiment\":\"" + sentiment + "\",\"time\":\"" + System.currentTimeMillis() + "\"}";
            System.out.println(input);
            OutputStream os = conn.getOutputStream();
            os.write(input.getBytes());
            os.flush();

            if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
                System.out.println("The code is " + conn.getResponseMessage());
                throw new RuntimeException("Failed : HTTP error code : "
                        + conn.getResponseCode());
            }

            BufferedReader br = new BufferedReader(new InputStreamReader(
                    (conn.getInputStream())));

            String output;
            //System.out.println("Output from Server .... \n");
            while ((output = br.readLine()) != null) {
                //System.out.println(output);
            }

            conn.disconnect();
        } catch (Exception e) {

            e.printStackTrace();

        }

    }

}

	
		
		
		
	