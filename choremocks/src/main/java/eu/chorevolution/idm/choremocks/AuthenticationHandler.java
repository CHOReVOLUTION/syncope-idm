/*
 * Copyright 2016 The CHOReVOLUTION project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package eu.chorevolution.idm.choremocks;

import java.io.IOException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Response;
import org.apache.cxf.common.util.Base64Exception;
import org.apache.cxf.common.util.Base64Utility;

/**
 * Sample Basic Authentication handling.
 */
public class AuthenticationHandler implements ContainerRequestFilter {

    @Override
    public void filter(final ContainerRequestContext requestContext) throws IOException {
        // Skip authentication for Security Filter
        if (requestContext.getUriInfo().getRequestUri().toASCIIString().contains("/management/")) {
            return;
        }

        String authorization = requestContext.getHeaderString("Authorization");
        if (authorization == null) {
            authorization = "";
        }
        String[] parts = authorization.split(" ");
        if (parts.length != 2 || !"Basic".equals(parts[0])) {
            requestContext.abortWith(createFaultResponse());
            return;
        }

        String decodedValue;
        try {
            decodedValue = new String(Base64Utility.decode(parts[1]));
        } catch (Base64Exception e) {
            requestContext.abortWith(createFaultResponse());
            return;
        }
        String[] namePassword = decodedValue.split(":");
        if (isAuthenticated(namePassword[0], namePassword[1])) {
            // let request to continue
        } else {
            // authentication failed, request the authetication, add the realm name if needed to the value of
            // WWW-Authenticate 
            requestContext.abortWith(Response.status(401).header("WWW-Authenticate", "Basic").build());
        }
    }

    private Response createFaultResponse() {
        return Response.status(401).header("WWW-Authenticate", "Basic realm=\"CHOReVOLUTION IdM Mock-ups\"").build();
    }

    private boolean isAuthenticated(final String username, final String password) {
        return "admin".equals(username) && "admin".equals(password);
    }
}
