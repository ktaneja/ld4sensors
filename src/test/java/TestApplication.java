

import org.restlet.Application;
import org.restlet.Component;
import org.restlet.Restlet;
import org.restlet.data.ChallengeResponse;
import org.restlet.data.ChallengeScheme;
import org.restlet.data.Protocol;
import org.restlet.resource.ClientResource;
import org.restlet.routing.Router;
import org.restlet.routing.Template;
import org.restlet.security.ChallengeAuthenticator;
import org.restlet.security.RoleAuthorizer;


public class TestApplication extends Application {
    public static void main(String[] args) throws Exception {
        Component c = new Component();
        c.getServers().add(Protocol.HTTP, 8182);

        c.getDefaultHost().attach("/myapp", new TestApplication());
        c.start();

        ClientResource pubResource = new ClientResource(
                "http://localhost:8182/myapp/publish/test");
        ChallengeResponse cr = new ChallengeResponse(
                ChallengeScheme.HTTP_BASIC, "scotts", "tiger");
        pubResource.setChallengeResponse(cr);
        pubResource.get().write(System.out);

        ClientResource adminResource = new ClientResource(
                "http://localhost:8182/myapp/admin/test");
        cr = new ChallengeResponse(ChallengeScheme.HTTP_BASIC, "admin", "admin");
        adminResource.setChallengeResponse(cr);
        adminResource.get().write(System.out);

        c.stop();
    }

    public TestApplication() {
        // Complete the list of the application's known roles.
        // Used by ServerResource#isInRole(String), at the resource level
        getRoles().add(MyEnroler.ADMINISTRATOR);
        getRoles().add(MyEnroler.PUBLISHER);
    }

    @Override
    public Restlet createInboundRoot() {
        Router rootRouter = new Router(getContext());
        rootRouter.setDefaultMatchingMode(Template.MODE_STARTS_WITH);

        // "Publish" URIs
        Router pubRouter = new Router(getContext());
        pubRouter.attach("/test", TestResource.class);

        // Available only for publishers
        RoleAuthorizer ra = new RoleAuthorizer();
        ra.getAuthorizedRoles().add(MyEnroler.PUBLISHER);
        ra.setNext(pubRouter);

        rootRouter.attach("/publish", ra);

        // "Administration" URIs
        Router adminRouter = new Router(getContext());
        adminRouter.attach("/test", TestResource.class);

        // Available only for admins
        ra = new RoleAuthorizer();
        ra.getAuthorizedRoles().add(MyEnroler.ADMINISTRATOR);
        ra.setNext(adminRouter);

        rootRouter.attach("/admin", ra);

        // Authenticate the whole hierarchy of URIs
        ChallengeAuthenticator guard = new ChallengeAuthenticator(getContext(),
                ChallengeScheme.HTTP_BASIC, "my realm");
        guard.setVerifier(new MyVerifier());
        guard.setEnroler(new MyEnroler());

        guard.setNext(rootRouter);

        return guard;
    }
}
