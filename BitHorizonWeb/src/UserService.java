import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

import com.ibm.json.java.JSONObject;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.WriteResult;

import helpers.DBHelper;
import helpers.FBHelper;

@Path("/user")
// @Produces(MediaType.APPLICATION_JSON)
public class UserService {

	@GET
	@Path("/test")
	public Response test() {
		return Response.ok("Test message").build();
	}

	@POST
	@Path("/login")
	public Response login(MultivaluedMap<String, String> params) {
		System.out.println(params.toString());
		System.out.println(params.get("accessToken"));
		String token = params.get("accessToken").get(0);
		String userJson = FBHelper.user(token);
		
		Object o = com.mongodb.util.JSON.parse(userJson);
		DBObject dbObj = (DBObject) o;
		DBCollection users = DBHelper.getCollection("users");
		WriteResult result = users.insert(dbObj);
		
		JSONObject response = new JSONObject();
		response.put("success", result.getN());
		return Response.ok(response.toString()).header("Access-Control-Allow-Origin", "*")
				.header("Access-Control-Allow-Headers", "origin, content-type, accept, authorization")
				.header("Access-Control-Allow-Credentials", "true")
				.header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD")
				.header("Access-Control-Max-Age", "1209600").build();
	}

	@POST
	@Path("/register")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response register(MultivaluedMap<String, String> params) {
		BasicDBObject query = new BasicDBObject();
		query.put("name", params.get("name").get(0));
		query.put("password", params.get("password").get(0));

		DBCollection users = DBHelper.getCollection("users");
		DBCursor docs = users.find(query);

		return Response.ok(docs.toArray().toString()).header("Access-Control-Allow-Origin", "*")
				.header("Access-Control-Allow-Headers", "origin, content-type, accept, authorization")
				.header("Access-Control-Allow-Credentials", "true")
				.header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD")
				.header("Access-Control-Max-Age", "1209600").build();
	}
}
