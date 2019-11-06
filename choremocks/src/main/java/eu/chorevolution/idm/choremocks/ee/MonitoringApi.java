/*
 * Copyright 2016 The CHOReVOLUTION project.
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

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;


@Path("/v1/monitoring")
@Api("Monitoring")
public interface MonitoringApi {

	@GET
	@Path("/ee")
	@Produces(MediaType.APPLICATION_JSON)
	Response getEngineStatus();


	@GET
	@Path("/ee/vm")
	@Produces(MediaType.APPLICATION_JSON)
	Response getEngineVirtualMachines();


	@GET
	@Path("/chor/{choreographyId}")
	@Produces(MediaType.APPLICATION_JSON)
	Response getChorStatus(
            @ApiParam(name = "choreographyId", value = "The ID of the choregraphy to monitor", required = true)
            @PathParam("choreographyId") String choreographyId
	);


	@GET
	@Path("/chor/{choreographyId}/vm")
	@Produces(MediaType.APPLICATION_JSON)
	Response getChorVirtualMachines(
            @ApiParam(name = "choreographyId", value = "The ID of the choregraphy to monitor", required = true)
            @PathParam("choreographyId") String choreographyId
	);

}