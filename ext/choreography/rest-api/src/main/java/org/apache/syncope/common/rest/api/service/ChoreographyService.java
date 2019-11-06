/*
 * Copyright 2015 The CHOReVOLUTION project
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
package org.apache.syncope.common.rest.api.service;

import eu.chorevolution.idm.common.to.ChoreographyTO;
import eu.chorevolution.idm.common.types.ChoreographyAction;
import eu.chorevolution.idm.common.types.ChoreographyOperation;
import eu.chorevolution.idm.common.types.SecurityFilterInfo;
import eu.chorevolution.idm.common.types.ServiceAction;
import java.io.InputStream;
import java.net.URL;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.apache.syncope.common.lib.to.AnyObjectTO;

/**
 * REST operations for acting on choreographies and / or their services.
 */
@Path("chors")
public interface ChoreographyService extends JAXRSService {

    /**
     * Retrieves choreography
     *
     * @param choreographyKey choreography key
     * @return choreography
     */
    @Path("{key}")
    @GET
    @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    Response read(@NotNull @PathParam("key") String choreographyKey);

    /**
     * Retrieves choreography list
     *
     * @return list of all choreographies, with only name, key and description
     */
    @GET
    @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    Response list();

    /**
     * Creates a choreography.
     *
     * @param choreographyTO choreography
     * @return Response object featuring Location header of created choreography
     */
    @POST
    @Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    Response create(ChoreographyTO choreographyTO);

    /**
     * Updates an existing choreography, with updated specification.
     *
     * @param choreographyTO choreography
     */
    @PUT
    @Path("{key}")
    @Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    void update(ChoreographyTO choreographyTO);

    /**
     * Deletes the given choreography.
     *
     * @param key choreography key
     */
    @DELETE
    @Path("{key}")
    void delete(@NotNull @PathParam("key") String key);

    /**
     * Request to enact the given choreography, onto the associated Enactment Engine.
     *
     * @param key choreography key
     * @param enactmentEngineKey target enactment engine instance (ignored if it has been already specified)
     */
    @POST
    @Path("{key}/enact")
    void enact(
            @NotNull @PathParam("key") String key,
            @NotNull @QueryParam("enactmentEngineKey") String enactmentEngineKey);

    /**
     * This operation will used to notify that an enactment operation was completed, providing the
     * related concrete ChorSpec.
     *
     * @param id choreography id
     * @param name choreography name
     * @param operation the operation performed by the enactment engine on the given choreography
     * @param message execution message - reporting success or failure (for example)
     * @param enactedChorSpec ChorSpec XML representation, as enacted by the enactment engine
     */
    @POST
    @Path("{id}/{operation}/notifyCompletion")
    @Consumes({ MediaType.APPLICATION_XML })
    void notifyCompletion(
            @NotNull @PathParam("id") String id,
            @NotNull @QueryParam("name") String name,
            @NotNull @PathParam("operation") ChoreographyOperation operation,
            @QueryParam("message") String message,
            InputStream enactedChorSpec);

    /**
     * Performs the given action on the given choreography.
     *
     * @param key choreography key
     * @param action action to be performed
     * @param newSize (normally ignored, required if {@code action} is {@link ChoreographyAction#RESIZE}
     * minimum number of nodes running each service group of the given choreography
     */
    @POST
    @Path("{key}")
    void onChoreography(
            @NotNull @PathParam("key") String key,
            @NotNull @QueryParam("action") ChoreographyAction action,
            @QueryParam("newSize") Integer newSize);

    /**
     * Performs the given action on the given choreography's service.
     *
     * @param key choreography key
     * @param serviceId choreography service id
     * @param action action to be performed
     * @param argument Security context name if {@link ServiceAction#ENFORCE_SECURITY_CONTEXT};
     * new service id if {@link ServiceAction#REPLACE};
     * nothing if {@link ServiceAction#ENABLE_SECURITY_FILTER} or {@link ServiceAction#DISABLE_SECURITY_FILTER}
     * service URL to replace the current set for the given service id
     */
    @POST
    @Path("{key}/{serviceId}")
    void onChoreographyService(
            @NotNull @PathParam("key") String key,
            @NotNull @PathParam("serviceId") String serviceId,
            @NotNull @QueryParam("action") ServiceAction action,
            @QueryParam("argument") String argument);

    /**
     * Returns information about the current status of the security filter instance associated to the given service,
     * in the given choreography (if available).
     *
     * @param key choreography key
     * @param serviceId choreography service id
     * @return information about the current status of the security filter instance associated to the given service,
     * in the given choreography (if available)
     */
    @GET
    @Path("{key}/{serviceId}/securityFilterInfo")
    @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    SecurityFilterInfo readSecurityFilterInfo(
            @NotNull @PathParam("key") String key,
            @NotNull @PathParam("serviceId") String serviceId);

    /**
     * Configure the Federation Server URL into the security filter instance associated to the given service,
     * in the given choreography (if available).
     *
     * @param key choreography key
     * @param serviceId choreography service id
     * @param federationServerURL Federation Server URL to configure
     */
    @PUT
    @Path("{key}/{serviceId}/securityFilter")
    void configureSecurityFilter(
            @NotNull @PathParam("key") String key,
            @NotNull @PathParam("serviceId") String serviceId,
            URL federationServerURL);

    /**
     * Returns the enactment engine associated with the choreography.
     *
     * @param choreographyKey choreography key
     * @return Enactment engine associated to the choreography
     */
    @GET
    @Path("{key}/enactmentEngine")
    @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    AnyObjectTO getEnactmentEngine(@NotNull @PathParam("key") String choreographyKey);

    /**
     * Returns the synthesis processor associated with the choreography.
     *
     * @param choreographyKey choreography key
     * @return Enactment engine associated to the choreography
     */
    @GET
    @Path("{key}/synthesisProcessor")
    @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    AnyObjectTO getSynthesisProcessor(@NotNull @PathParam("key") String choreographyKey);

}
