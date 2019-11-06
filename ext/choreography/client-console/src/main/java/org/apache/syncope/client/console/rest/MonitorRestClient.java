/*
 * Copyright 2017 The CHOReVOLUTION project.
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
package org.apache.syncope.client.console.rest;

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
import static org.apache.syncope.client.console.rest.BaseRestClient.getService;
import org.apache.syncope.common.rest.api.service.MonitorInterfaceService;

public class MonitorRestClient extends BaseRestClient {

    private static final long serialVersionUID = -3482684671830574479L;

    public List<ChoreographyInstanceTO> instanceList(final String choreographyId) {
    return getService(MonitorInterfaceService.class).instanceList(choreographyId);
    }

    public List<CoordinationDelegateTO> cdList(final String instanceId) {
        return getService(MonitorInterfaceService.class).cdList(instanceId);
    }

    public List<ServiceTO> instanceServiceList(final String instanceId) {
        return getService(MonitorInterfaceService.class).instanceServiceList(instanceId);
    }

    public List<ServiceTO> choreographyServiceList(final String choreographyId) {
        return getService(MonitorInterfaceService.class).choreographyServiceList(choreographyId);
    }

    public List<OperationDataTO> operationList(
            final String choreographyInstancePK,
            final String cdName) {
        return getService(MonitorInterfaceService.class).operationList(choreographyInstancePK, cdName);
    }

    public ChoreographyTO getChoreography(final String choreographyId) {
        return getService(MonitorInterfaceService.class).getChoreography(choreographyId);
    }

    public ChoreographyInstanceTO getChoreographyInstance(final String choreographyInstancePK) {
        return getService(MonitorInterfaceService.class).getChoreographyInstance(choreographyInstancePK);
    }

    public CoordinationDelegateTO getCD(final String choreographyInstancePK, final String cdName) {
        return getService(MonitorInterfaceService.class).getCd(choreographyInstancePK, cdName);
    }

    public ServiceTO getService(final String choreographyInstancePK, final String serviceName) {
        return getService(MonitorInterfaceService.class).getService(choreographyInstancePK, serviceName);
    }

    public Double getAverageInstanceExecutionTime(final String choreographyId) {
        return getService(MonitorInterfaceService.class).getAverageInstanceExecutionTime(choreographyId);
    }

    public List<AVGCoordinationDelegateTO> averageCdList(final String choreographyId) {
        return getService(MonitorInterfaceService.class).averageCdList(choreographyId);
    }

    public List<AVGServiceTO> averageServiceList(final String choreographyId) {
        return getService(MonitorInterfaceService.class).averageServiceList(choreographyId);
    }

    public List<AVGOperationDataTO> averageOperationList(
            final String choreographyId,
            final String cdName) {
        return getService(MonitorInterfaceService.class).averageOperationList(choreographyId, cdName);
    }

    public void deleteInstance(final String choreographyInstancePK) {
        getService(MonitorInterfaceService.class).deleteInstance(choreographyInstancePK);
    }

    public EnactmentEngineStatusTO getChoreographyEEStatus(
            final String enactmentEngineKey,
            final String choreographyId) {
        return getService(MonitorInterfaceService.class).getChoreographyEEStatus(enactmentEngineKey, choreographyId);
    }

    public List<VirtualMachineInfoTO> getChoreographyVmList(
            final String enactmentEngineKey,
            final String choreographyId) {
        return getService(MonitorInterfaceService.class).getChoreographyVmList(enactmentEngineKey, choreographyId);
    }
    
}
