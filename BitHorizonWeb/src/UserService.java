import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

@Path("/user")
@Produces(MediaType.APPLICATION_JSON)
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
		
		return Response.ok("Hello").build();
	}
}
