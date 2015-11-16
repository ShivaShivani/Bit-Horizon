import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

@Path("/music")
public class MusicService {

	@Path("/recommend")
	@GET
	public Response recommend() {
		return null;
	}
	
	
}
