
import org.restlet.data.ClientInfo;
import org.restlet.security.Enroler;
import org.restlet.security.Role;

public class MyEnroler implements Enroler {

    public final static Role PUBLISHER = new Role("publisher", "publisher");

    public final static Role ADMINISTRATOR = new Role("admin", "administrator");

    public void enrole(ClientInfo clientInfo) {
        // Gives the role according to a user.
        if ("scott".equals(clientInfo.getUser().getIdentifier())) {
            clientInfo.getRoles().add(PUBLISHER);
        } else if ("admin".equals(clientInfo.getUser().getIdentifier())) {
            clientInfo.getRoles().add(ADMINISTRATOR);
        }

    }

}
