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
package org.apache.syncope.client.console.rest;

import eu.chorevolution.idm.common.to.EnactmentEngineStatusTO;
import eu.chorevolution.idm.common.to.EnactmentEngineTO;
import eu.chorevolution.idm.common.to.VirtualMachineInfoTO;
import java.util.List;
import static org.apache.syncope.client.console.rest.BaseRestClient.getService;
import org.apache.syncope.common.rest.api.service.EEService;
import org.apache.syncope.common.rest.api.service.MonitorInterfaceService;

public class EnactmentEngineRestClient extends BaseRestClient {

    private static final long serialVersionUID = -1037423582023749173L;

    public EnactmentEngineStatusTO getEEStatus(final String enactmentEngineKey) {
        return getService(MonitorInterfaceService.class).getEnactmentEngineStatus(enactmentEngineKey);
    }

    public List<VirtualMachineInfoTO> getEEVmList(final String enactmentEngineKey) {
        return getService(MonitorInterfaceService.class).getEEVmList(enactmentEngineKey);
    }

    public void create(final EnactmentEngineTO enactmentEngineTO) {
        getService(EEService.class).create(enactmentEngineTO);
    }

    public void update(final EnactmentEngineTO enactmentEngineTO) {
        getService(EEService.class).update(enactmentEngineTO);
    }

    public void delete(final String enactmentEngineKey) {
        getService(EEService.class).delete(enactmentEngineKey);
    }
}
