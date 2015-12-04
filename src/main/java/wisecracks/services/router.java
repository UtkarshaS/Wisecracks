import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/LoginService")
public class LoginService {

    @POST
    @Path("/users")
    @Produces(MediaType.APPLICATION_JSON)
    boolean userExists = //RDS Search query;
    if(!userExists)
    {
        //Create user entry in DB
        String firstName =; // retrive from Google - in Http request
        String lastName =; // retrive from Google - in Http request

        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        //get current date time with Date()
        Date date = new Date();
        Date current = dateFormat.format(date);

        //Create a new user
        User(userId, firstName, lastName, current);
    }
}