

import java.util.List;

import org.restlet.resource.Get;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;
import org.restlet.security.Role;
import org.restlet.security.User;

public class TestResource extends ServerResource {

    /** Current user. */
    User user;

    /** Its role(s). */
    List<Role> roles;

    @Override
    protected void doInit() throws ResourceException {
        this.user = getClientInfo().getUser();
        this.roles = getClientInfo().getRoles();
    }

    @Get
    public String toText() {
        // List the role(s) of the current user.
        StringBuilder sb = new StringBuilder();
        sb.append("hello ").append(user.getIdentifier()).append(".");
        if (roles != null && !roles.isEmpty()) {
            if (roles.size() == 1) {
                sb.append(" Here is your role => ");
            } else {
                sb.append(" Here are your roles => ");
            }

            for (Role role : roles) {
                sb.append(role.getName());
                sb.append(" ");
            }
        }

        return sb.toString();
    }

}
