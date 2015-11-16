package helpers;

import java.util.List;

import com.ibm.json.java.JSONObject;
import com.restfb.Connection;
import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.json.JsonObject;
import com.restfb.types.Likes;
import com.restfb.types.Page;
import com.restfb.types.Post;
import com.restfb.types.User;

public class FBHelper {

	public static String user(String token) {
		FacebookClient facebookClient = new DefaultFacebookClient(token);
		JsonObject user = facebookClient.fetchObject("me", JsonObject.class);
		System.out.println(user.toString());
		return user.toString();
	}
	
	public static void main(String args[]) {
		FBHelper.user("CAACEdEose0cBAPZC4xd7sjjHkAYyLidBaa17lDcW0DmNi6N31MmYrKNLZCbQ0CopbXsXPuMRIkZAo11yQ3nnpcxP9WeMGi4TRWdB9bfLtRULvmsBKmSDZCRxRqR9QTtCZAEKuMwbZBiToLdgB5YWhQ8uQk6sZAdSNEZB30s1Aw2CWwHatYneTQtPg4Es9tjkQ7oA8IRPhLuN4tVFcPSjyffW");
//		Connection<Page> myFeed = facebookClient.fetchConnection("me/music", Page.class);
//		for (List<Page> myFeedConnectionPage : myFeed) {
//			for (Page post : myFeedConnectionPage) {
//				System.out.println("Post: " + post);	
//			}
//		}
	}
}
