package helpers;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;

public class DBHelper {

	public static DBCollection getCollection(String name) {
		MongoClientURI uri = new MongoClientURI("mongodb://root:password@ds055564.mongolab.com:55564/bithorizon1");
		MongoClient client = new MongoClient(uri);
		
		DB db = client.getDB(uri.getDatabase());
		DBCollection coll = db.getCollection(name);
		return coll;
	}

}
