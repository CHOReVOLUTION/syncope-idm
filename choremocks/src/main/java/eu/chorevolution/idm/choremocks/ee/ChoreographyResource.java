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
package eu.chorevolution.idm.choremocks.ee;

import eu.chorevolution.idm.common.types.ChoreographyOperation;
import java.io.InputStream;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import org.apache.brooklyn.rest.domain.TaskSummary;
import org.apache.cxf.interceptor.LoggingOutInterceptor;
import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.syncope.client.lib.SyncopeClient;
import org.apache.syncope.client.lib.SyncopeClientFactoryBean;
import org.apache.syncope.common.rest.api.service.ChoreographyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class ChoreographyResource implements ChoreographyApi {

    private static final Logger LOG = LoggerFactory.getLogger(ChoreographyResource.class);

    private static final Map<String, String> DEPLOYED = Collections.synchronizedMap(new HashMap<>());

    private static final ScheduledExecutorService EXECUTOR = Executors.newScheduledThreadPool(5);

    static {
        DEPLOYED.put("xx45671", "WP7");
    }

    @Context
    private UriInfo uriInfo;

    @Override
    public Response deploy(final String choreographyName, final InputStream chorSpec) {
        String choreographyId = UUID.randomUUID().toString();
        DEPLOYED.put(choreographyId, choreographyName);

        EXECUTOR.schedule(new CompletionNotifier(
                choreographyId,
                choreographyName,
                ChoreographyOperation.CREATE,
                "All good",
                getClass().getResourceAsStream("/" + choreographyName + "_create.xml")),
                3, TimeUnit.SECONDS);

        return Response.created(uriInfo.getAbsolutePathBuilder().path(choreographyId).build()).
                entity(new TaskSummary(
                        UUID.randomUUID().toString(),
                        "Create Task for " + choreographyId,
                        null,
                        choreographyId,
                        choreographyName,
                        null,
                        new Date().getTime(),
                        new Date().getTime(),
                        new Date().getTime(),
                        "SUBMITTED",
                        null,
                        false,
                        false,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null)).
                build();
    }

    @Override
    public Response update(final String choreographyId, final String choreographyName, final InputStream chorSpec) {
        if (!DEPLOYED.containsKey(choreographyId)) {
            throw new NotFoundException("Choreography " + choreographyId);
        }
        DEPLOYED.put(choreographyId, choreographyName);

        EXECUTOR.schedule(new CompletionNotifier(
                choreographyId,
                choreographyName,
                ChoreographyOperation.UPDATE,
                "All good",
                getClass().getResourceAsStream("/" + choreographyName + "_update.xml")),
                3, TimeUnit.SECONDS);

        return Response.accepted().
                entity(new TaskSummary(
                        UUID.randomUUID().toString(),
                        "Update Task for " + choreographyId,
                        null,
                        choreographyId,
                        choreographyName,
                        null,
                        new Date().getTime(),
                        new Date().getTime(),
                        new Date().getTime(),
                        "SUBMITTED",
                        null,
                        false,
                        false,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null)).
                build();
    }

    @Override
    public Response undeploy(final String choreographyId) {
        if (!DEPLOYED.containsKey(choreographyId)) {
            throw new NotFoundException("Choreography " + choreographyId);
        }

        String choreographyName = DEPLOYED.remove(choreographyId);

        EXECUTOR.schedule(new CompletionNotifier(
                choreographyId,
                choreographyName,
                ChoreographyOperation.DELETE,
                "All good",
                null),
                3, TimeUnit.SECONDS);

        return Response.accepted().
                entity(new TaskSummary(
                        UUID.randomUUID().toString(),
                        "Delete Task for " + choreographyId,
                        null,
                        choreographyId,
                        choreographyName,
                        null,
                        new Date().getTime(),
                        new Date().getTime(),
                        new Date().getTime(),
                        "SUBMITTED",
                        null,
                        false,
                        false,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null)).
                build();
    }

    @Override
    public Response checkStatus(final String choreographyId) {
        return null;
    }

    @Override
    public Response start(final String choreographyId) {
        String choreographyName = DEPLOYED.get(choreographyId);
        return Response.accepted().
                entity(new TaskSummary(
                        UUID.randomUUID().toString(),
                        "Start Task for " + choreographyId,
                        null,
                        choreographyId,
                        choreographyName,
                        null,
                        new Date().getTime(),
                        new Date().getTime(),
                        new Date().getTime(),
                        "SUBMITTED",
                        null,
                        false,
                        false,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null)).
                build();
    }

    @Override
    public Response stop(final String choreographyId) {
        String choreographyName = DEPLOYED.get(choreographyId);
        return Response.accepted().
                entity(new TaskSummary(
                        UUID.randomUUID().toString(),
                        "Stop Task for " + choreographyId,
                        null,
                        choreographyId,
                        choreographyName,
                        null,
                        new Date().getTime(),
                        new Date().getTime(),
                        new Date().getTime(),
                        "SUBMITTED",
                        null,
                        false,
                        false,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null)).
                build();
    }

    @Override
    public Response pause(final String choreographyId) {
        String choreographyName = DEPLOYED.get(choreographyId);
        return Response.accepted().
                entity(new TaskSummary(
                        UUID.randomUUID().toString(),
                        "Pause Task for " + choreographyId,
                        null,
                        choreographyId,
                        choreographyName,
                        null,
                        new Date().getTime(),
                        new Date().getTime(),
                        new Date().getTime(),
                        "SUBMITTED",
                        null,
                        false,
                        false,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null)).
                build();
    }

    @Override
    public Response resize(final String choreographyId, final Integer newSize) {
        String choreographyName = DEPLOYED.get(choreographyId);
        return Response.accepted().
                entity(new TaskSummary(
                        UUID.randomUUID().toString(),
                        "Resize Task for " + choreographyId,
                        null,
                        choreographyId,
                        choreographyName,
                        null,
                        new Date().getTime(),
                        new Date().getTime(),
                        new Date().getTime(),
                        "SUBMITTED",
                        null,
                        false,
                        false,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null)).
                build();
    }

    @Override
    public Response replaceService(
            final String choreographyId,
            final String serviceRole,
            final String serviceName,
            final String serviceEndpoint) {

        return null;
    }

    private static class CompletionNotifier implements Runnable {

        private final String id;

        private final String name;

        private final ChoreographyOperation operation;

        private final String message;

        private final InputStream enactedChorSpec;

        private final SyncopeClient client;

        public CompletionNotifier(
                final String id,
                final String name,
                final ChoreographyOperation operation,
                final String message,
                final InputStream enactedChorSpec) {

            this.id = id;
            this.name = name;
            this.operation = operation;
            this.message = message;
            this.enactedChorSpec = enactedChorSpec;

            client = new SyncopeClientFactoryBean().
                    setAddress("http://localhost:9080/syncope/rest").
                    create("admin", "password");
        }

        @Override
        public void run() {
            LOG.debug("Notifying {} {}", id, operation);
            try {
                ChoreographyService service = client.getService(ChoreographyService.class);
                WebClient.getConfig(service).getOutInterceptors().add(new LoggingOutInterceptor());
                WebClient.client(service).type(MediaType.APPLICATION_XML_TYPE);
                service.notifyCompletion(id, name, operation, message, enactedChorSpec);
                LOG.debug("Notified {} {}", id, operation);
            } catch (Exception e) {
                LOG.error("While notifying", e);
            }
        }

    }
}
