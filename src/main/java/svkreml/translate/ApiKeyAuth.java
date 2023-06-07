package svkreml.translate;

import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientRequestFilter;
import javax.ws.rs.core.HttpHeaders;
import java.io.IOException;

public class ApiKeyAuth implements ClientRequestFilter {
    private final String authHeader;

    public ApiKeyAuth(final String token) {
        authHeader = "Api-Key " + token;
    }

    @Override
    public void filter(ClientRequestContext requestContext) throws IOException {
        requestContext.getHeaders().putSingle(HttpHeaders.AUTHORIZATION, authHeader);
    }
}
