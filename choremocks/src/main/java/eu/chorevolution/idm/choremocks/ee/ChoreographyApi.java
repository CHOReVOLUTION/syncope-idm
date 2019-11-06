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

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import java.io.InputStream;
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

@Path("/v1/choreography")
@Api("Choreography")
@Produces(MediaType.APPLICATION_JSON)
public interface ChoreographyApi {

    @POST
    @Path("/{choreographyId}/start")
    @ApiOperation(
            value = "Start a running choreography."
    )
    Response start(
            @ApiParam(name = "choreographyId", value = "The ID of the choregraphy to start", required = true)
            @PathParam("choreographyId") String choreographyId);

    @POST
    @Path("/{choreographyId}/stop")
    @ApiOperation(
            value = "Stop a running choreography."
    )
    Response stop(
            @ApiParam(name = "choreographyId", value = "The ID of the choregraphy to stop", required = true)
            @PathParam("choreographyId") String choreographyId);

    @POST
    @Path("/{choreographyId}/pause")
    @ApiOperation(
            value = "Pause a running choreography."
    )
    Response pause(
            @ApiParam(name = "choreographyId", value = "The ID of the choregraphy to pause", required = true)
            @PathParam("choreographyId") String choreographyId);

    @GET
    @Path("/{choreographyId}/check_status")
    Response checkStatus(
            @ApiParam(name = "choreographyId", value = "The ID of the choregraphy to check", required = true)
            @PathParam("choreographyId") String choreographyId);

    @POST
    @Path("/")
    @Consumes(MediaType.APPLICATION_XML)
    @ApiOperation(
            value = "Deploy a choreography onto a cloud environment.",
            response = org.apache.brooklyn.rest.domain.TaskSummary.class
    )
    Response deploy(
            @ApiParam(name = "choreographyName", value = "Choreography name", required = true)
            @QueryParam("choreographyName") String choreographyName,
            @ApiParam(name = "chorSpec", value = "Choreography specifications", required = true) InputStream chorSpec);

    @PUT
    @Path("/{choreographyId}")
    @Consumes(MediaType.APPLICATION_XML)
    @ApiOperation(
            value = "Update an existing choreography onto a cloud environment.",
            response = org.apache.brooklyn.rest.domain.TaskSummary.class
    )
    Response update(
            @ApiParam(name = "choreographyId", value = "Choreography ID", required = true)
            @PathParam("choreographyId") String choreographyId,
            @ApiParam(name = "choreographyName", value = "Choreography name", required = true)
            @QueryParam("choreographyName") String choreographyName,
            @ApiParam(name = "chorSpec", value = "Choreography specifications", required = true) InputStream chorSpec);

    @DELETE
    @Path("/{choreographyId}")
    @ApiOperation(
            value = "Undeploy a choreography."
    )
    Response undeploy(
            @ApiParam(name = "choreographyId", value = "The ID of the choregraphy to undeploy", required = true)
            @PathParam("choreographyId") String choreographyId);

    @PUT
    @Path("/{choreographyId}/replaceService/{serviceRole}/{serviceName}")
    @ApiOperation(
            value = "Replace the given service endpoint",
            response = org.apache.brooklyn.rest.domain.TaskSummary.class
    )
    Response replaceService(
            @ApiParam(name = "choreographyId", value = "Choreography ID", required = true)
            @PathParam("choreographyId") String choreographyId,
            @ApiParam(name = "serviceRole", value = "Service name", required = true)
            @PathParam("serviceRole") String serviceRole,
            @ApiParam(name = "serviceName", value = "Service name", required = true)
            @PathParam("serviceRole") String serviceName,
            @QueryParam("serviceEndpoint") String serviceEndpoint);

    @POST
    @Path("/{choreographyId}/resize")
    @ApiOperation(
            value = "Resize VM pool running a running choreography.",
            response = org.apache.brooklyn.rest.domain.TaskSummary.class
    )
    Response resize(
            @ApiParam(name = "choreographyId", value = "Choreography ID", required = true)
            @PathParam("choreographyId") String choreographyId,
            @ApiParam(name = "desired_pool_size", value = "Desired size of the VMs pool", required = true)
            @QueryParam("newSize") Integer newSize);
}
