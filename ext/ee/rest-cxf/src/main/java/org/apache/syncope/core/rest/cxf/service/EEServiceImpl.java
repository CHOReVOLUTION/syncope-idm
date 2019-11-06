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
package org.apache.syncope.core.rest.cxf.service;

import eu.chorevolution.idm.common.to.EnactmentEngineTO;
import javax.ws.rs.core.Response;

import org.apache.syncope.common.rest.api.service.EEService;
import org.apache.syncope.core.logic.EELogic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EEServiceImpl extends AbstractServiceImpl implements EEService {

    @Autowired
    private EELogic logic;

    @Override
    public Response create(final EnactmentEngineTO enactmentEngineTO) {
        return Response.status(Response.Status.CREATED).
                header("X-CHOReVOLUTION-EnactmentEngineId", logic.create(enactmentEngineTO)).
                build();
    }

    @Override
    public void update(final EnactmentEngineTO enactmentEngineTO) {
        logic.update(enactmentEngineTO);
    }

    @Override
    public void delete(final String enactmentEngineKey) {
        logic.delete(enactmentEngineKey);
    }
}
