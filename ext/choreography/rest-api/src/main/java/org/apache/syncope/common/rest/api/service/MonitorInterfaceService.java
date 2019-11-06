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
package org.apache.syncope.common.rest.api.service;

import eu.chorevolution.idm.common.to.AVGCoordinationDelegateTO;
import eu.chorevolution.idm.common.to.AVGOperationDataTO;
import eu.chorevolution.idm.common.to.AVGServiceTO;
import eu.chorevolution.idm.common.to.ChoreographyInstanceTO;
import eu.chorevolution.idm.common.to.ChoreographyTO;
import eu.chorevolution.idm.common.to.CoordinationDelegateTO;
import eu.chorevolution.idm.common.to.EnactmentEngineStatusTO;
import eu.chorevolution.idm.common.to.OperationDataTO;
import eu.chorevolution.idm.common.to.ServiceTO;
import eu.chorevolution.idm.common.to.VirtualMachineInfoTO;
import java.util.List;
import javax.validation.constraints.NotNull;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

/**
 * REST consoleinterface services.
 */
@Path("consoleinterface")
public interface MonitorInterfaceService extends JAXRSService {

    @Path("{choreographyId}")
    @GET
    ChoreographyTO getChoreography(@NotNull @PathParam("choreographyId") String choreographyId);

    /**
     * This is a temporary method.
     *
     * @return all defined choreographies
     */
    @Path("choreographies")
    @GET
    List<ChoreographyTO> choreographyList();

    /**
     * This is a temporary method.
     *
     * @param choreographyId
     * @return all instances of selected choreography
     */
    @Path("{choreographyId}/instances")
    @GET
    List<ChoreographyInstanceTO> instanceList(@PathParam("choreographyId") String choreographyId);

    /**
     * This is a temporary method.
     *
     * @param choreographyInstancePK
     * @return all CDs of selected choreography instance
     */
    @Path("instance/{choreographyInstancePK}/cds")
    @GET
    List<CoordinationDelegateTO> cdList(@PathParam("choreographyInstancePK") String choreographyInstancePK);

    /**
     * Retrieves service instance list.
     *
     * @param choreographyInstancePK choreography instance
     * @return service instance list
     */
    @Path("instance/{choreographyInstancePK}/serviceList")
    @GET
    List<ServiceTO> instanceServiceList(@PathParam("choreographyInstancePK") String choreographyInstancePK);

    /**
     * Retrieves choreography service list
     *
     * @param choreographyId choreography id
     * @return service list
     */
    @Path("{choreographyId}/serviceList")
    @GET
    List<ServiceTO> choreographyServiceList(@PathParam("choreographyId") String choreographyId);

    /**
     * Retrieves CD operations
     *
     * @param choreographyInstancePK
     * @param cdName
     * @return all operations performed by selected CD
     */
    @Path("instance/{choreographyInstancePK}/{cdname}/operations")
    @GET
    List<OperationDataTO> operationList(
            @PathParam("choreographyInstancePK") String choreographyInstancePK,
            @PathParam("cdname") String cdName
    );

    /**
     * Retrieves choreography instance
     *
     * @param choreographyInstancePK
     * @return choreography instance
     */
    @Path("/instance/{choreographyInstancePK}")
    @GET
    ChoreographyInstanceTO getChoreographyInstance(@PathParam("choreographyInstancePK") String choreographyInstancePK);

    /**
     * Retrieved Coordination Delegate
     *
     * @param choreographyInstancePK choreography instance
     * @param cdName coordination delegate name
     * @return coordination delegate
     */
    @Path("/cd/{cdName}/{choreographyInstancePK}")
    @GET
    CoordinationDelegateTO getCd(
            @PathParam("choreographyInstancePK") String choreographyInstancePK,
            @PathParam("cdName") String cdName
    );

    /**
     * Retrieves services
     *
     * @param choreographyInstancePK choreography instance
     * @param serviceName service name
     * @return service
     */
    @Path("/service/{serviceName}/{choreographyInstancePK}")
    @GET
    ServiceTO getService(
            @PathParam("choreographyInstancePK") String choreographyInstancePK,
            @PathParam("serviceName") String serviceName
    );

    /**
     * Retrieves average execution time
     *
     * @param choreographyId choreography id
     * @return average execution time
     */
    @Path("{choreographyId}/instancesAverage")
    @GET
    Double getAverageInstanceExecutionTime(@PathParam("choreographyId") String choreographyId);

    /**
     * Retrieves average CD execution time
     *
     * @param choreographyId choreography id
     * @return average execution time
     */
    @Path("{choreographyId}/averageCds")
    @GET
    List<AVGCoordinationDelegateTO> averageCdList(@PathParam("choreographyId") String choreographyId);

    /**
     * Retrieves average service execution time
     *
     * @param choreographyId choreography id
     * @return average execution time
     */
    @Path("{choreographyId}/averageServices")
    @GET
    List<AVGServiceTO> averageServiceList(@PathParam("choreographyId") String choreographyId);

    /**
     * Retrieves average CD execution time
     *
     * @param choreographyId choreograpy id
     * @param cdName coordination delegate
     * @return average execution time
     */
    @Path("{choreographyId}/{cdName}/averageOperations")
    @GET
    List<AVGOperationDataTO> averageOperationList(
            @PathParam("choreographyId") String choreographyId,
            @PathParam("cdName") String cdName);

    /**
     * Deltes instance
     *
     * @param choreographyInstancePK choreography instance
     */
    @Path("instance/{choreographyInstancePK}")
    @DELETE
    void deleteInstance(@PathParam("choreographyInstancePK") String choreographyInstancePK);

    /**
     * Returns the enactment engine associated to the choreography
     * 
     * @param enactmentEngineKey enactment engine key
     * @return status of enactment engine
     */
    @GET
    @Path("enactmentEngine/{enactmentEngineKey}/status")
    EnactmentEngineStatusTO getEnactmentEngineStatus(
            @NotNull @PathParam("enactmentEngineKey") String enactmentEngineKey);

    /**
     * Returns the enactment engine associated to the choreography
     * 
     * @param enactmentEngineKey enactment engine key
     * @return list of virtual machines for the enactment engine
     */
    @GET
    @Path("enactmentEngine/{enactmentEngineKey}/virtualMachines")
    List<VirtualMachineInfoTO> getEEVmList(@NotNull @PathParam("enactmentEngineKey") String enactmentEngineKey);

    /**
     * Returns the enactment engine associated to the choreography
     *
     * @param enactmentEngineKey enactment engine key
     * @param choreographyId choreography id
     * @return status of enactment engine for the choreography
     */
    @GET
    @Path("{enactmentEngineKey}/{choreographyId}/eeStatus")
    EnactmentEngineStatusTO getChoreographyEEStatus(
            @NotNull @PathParam("enactmentEngineKey") String enactmentEngineKey,
            @NotNull @PathParam("choreographyId") String choreographyId);

    /**
     * Returns the enactment engine associated to the choreography
     *
     * @param enactmentEngineKey enactment engine key
     * @param choreographyId choreography id
     * @return list of virtual machines for the enactment engine
     */
    @GET
    @Path("enactmentEngine/{enactmentEngineKey}/{choreographyId}/virtualMachines")
    List<VirtualMachineInfoTO> getChoreographyVmList(
            @NotNull @PathParam("enactmentEngineKey") String enactmentEngineKey,
            @NotNull @PathParam("choreographyId") String choreographyId);
}
