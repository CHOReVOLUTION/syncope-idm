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
package eu.chorevolution.idm.choremocks.fs;

import eu.chorevolution.securitytokenservice.federationserver.api.EndUser;
import eu.chorevolution.securitytokenservice.federationserver.api.FederationServerEndUserService;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ws.rs.Path;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.springframework.stereotype.Service;

@Service
@Path("/domains/{domain}/endusers")
public class FederationServerEndUserServiceImpl implements FederationServerEndUserService {

    private static final Map<String, Map<String, EndUser>> DB = Collections.synchronizedMap(new HashMap<>());

    @Override
    public Response list(final String domain) {
        return Response.ok(new GenericEntity<List<EndUser>>(
                DB.containsKey(domain)
                ? new ArrayList<>(DB.get(domain).values())
                : Collections.emptyList()) {
        }).build();
    }

    @Override
    public Response create(final String domain, final EndUser enduser) {
        Map<String, EndUser> users = DB.get(domain);
        if (users == null) {
            users = new HashMap<>();
            DB.put(domain, users);
        }
        if (users.containsKey(enduser.getUsername())) {
            return Response.status(Response.Status.CONFLICT).
                    entity(enduser.getUsername()).
                    type(MediaType.TEXT_PLAIN).build();
        }

        users.put(enduser.getUsername(), enduser);
        return Response.status(Response.Status.CREATED).build();
    }

    @Override
    public Response read(final String domain, final String username) {
        if (DB.containsKey(domain)) {
            EndUser user = DB.get(domain).get(username);
            if (user != null) {
                return Response.ok(user).build();
            }
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    @Override
    public Response update(final String domain, final String username, final EndUser enduser) {
        EndUser user = null;
        if (DB.containsKey(domain)) {
            user = DB.get(domain).get(username);
        }
        if (user == null) {
            return Response.status(Response.Status.NO_CONTENT).build();
        }

        DB.get(domain).put(enduser.getUsername(), enduser);

        return Response.noContent().build();
    }

    @Override
    public Response delete(final String domain, final String username) {
        EndUser user = null;
        if (DB.containsKey(domain)) {
            user = DB.get(domain).get(username);
        }
        if (user == null) {
            return Response.status(Response.Status.NO_CONTENT).build();
        }

        DB.get(domain).remove(username);
        return Response.noContent().build();
    }

}
