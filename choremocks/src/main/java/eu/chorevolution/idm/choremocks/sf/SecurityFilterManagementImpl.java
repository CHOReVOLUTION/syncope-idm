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
package eu.chorevolution.idm.choremocks.sf;

import eu.chorevolution.securityfilter.api.RuntimeInfo;
import eu.chorevolution.securityfilter.api.SecurityFilterManagement;
import eu.chorevolution.securityfilter.api.Status;
import eu.chorevolution.securityfilter.api.SecurityContext;
import eu.chorevolution.securityfilter.api.SecurityFilterConfiguration;
import javax.ws.rs.Path;
import org.springframework.stereotype.Service;

@Service
@Path(value = "/management")
public class SecurityFilterManagementImpl implements SecurityFilterManagement {

    private static final RuntimeInfo SHARED_INSTANCE;

    static {
        SHARED_INSTANCE = new RuntimeInfo();
        SHARED_INSTANCE.setStatus(Status.ENABLED);
        SHARED_INSTANCE.setSecurityContext(null);
    }

    @Override
    public RuntimeInfo info() {
        return SHARED_INSTANCE;
    }

    @Override
    public void status(final Status status) {
        SHARED_INSTANCE.setStatus(status);
    }

    @Override
    public void securityContext(final SecurityContext securityContext) {
        SHARED_INSTANCE.setSecurityContext(securityContext);
    }

    @Override
    public void federationServerURL(final SecurityFilterConfiguration config) {
    }

}
