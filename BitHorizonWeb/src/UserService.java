import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

@Path("/user")
public class UserService {
	
	@GET
	@Path("/test")
	public Response test() {
		return Response.ok("Test message").build();
	}
}
