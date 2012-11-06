

import org.restlet.security.SecretVerifier;

public class MyVerifier extends SecretVerifier {

    @Override
    public boolean verify(String identifier, char[] secret) {
        // Could check from a database (see in this case LocalVerifier).
        return (("scott".equals(identifier)) && compare("tiger".toCharArray(),
                secret))
                || (("admin".equals(identifier)) && compare("admin"
                        .toCharArray(), secret));
    }

}
