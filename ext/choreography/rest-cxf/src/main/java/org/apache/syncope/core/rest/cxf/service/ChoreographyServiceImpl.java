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

import eu.chorevolution.idm.common.to.ChoreographyTO;
import eu.chorevolution.idm.common.types.ChoreographyAction;
import eu.chorevolution.idm.common.types.ChoreographyOperation;
import eu.chorevolution.idm.common.types.SecurityFilterInfo;
import eu.chorevolution.idm.common.types.ServiceAction;
import org.apache.syncope.core.logic.ChoreographyLogic;
import java.io.InputStream;
import java.net.URL;
import java.util.List;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.Response;
import org.apache.syncope.common.lib.to.AnyObjectTO;
import org.apache.syncope.common.rest.api.RESTHeaders;
import org.apache.syncope.common.rest.api.service.ChoreographyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ChoreographyServiceImpl extends AbstractServiceImpl implements ChoreographyService {

    @Autowired
    private ChoreographyLogic logic;

    @Override
    public Response create(final ChoreographyTO choreographyTO) {
        String key = logic.create(choreographyTO);
        return Response.
                created(uriInfo.getAbsolutePathBuilder().path(key).build()).
                header(RESTHeaders.RESOURCE_KEY, key).
                build();
    }

    @Override
    public Response read(final String choreographyKey) {
        return Response.ok(logic.getChoreography(choreographyKey)).build();
    }

    @Override
    public Response list() {
        return Response.ok(new GenericEntity<List<ChoreographyTO>>(logic.getChoreographyList()) { }).build();
    }

    @Override
    public void update(final ChoreographyTO choreographyTO) {
        logic.update(choreographyTO);
    }

    @Override
    public void delete(final String key) {
        logic.delete(key);
    }

    @Override
    public void enact(final String key, final String enactmentEngineKey) {
        logic.enact(key, enactmentEngineKey);
    }

    @Override
    public void notifyCompletion(
            final String id,
            final String name,
            final ChoreographyOperation operation,
            final String message,
            final InputStream enactedChorSpec) {

        logic.notifyCompletion(id, name, operation, message, enactedChorSpec);
    }

    @Override
    public void onChoreography(
            final String key,
            final ChoreographyAction action,
            final Integer newSize) {

        logic.onChoreography(key, action, newSize);
    }

    @Override
    public void onChoreographyService(
            final String key,
            final String serviceId,
            final ServiceAction action,
            final String newServiceURL) {

        logic.onChoreographyService(key, serviceId, action, newServiceURL);
    }

    @Override
    public SecurityFilterInfo readSecurityFilterInfo(final String choreographyKey, final String serviceId) {
        return logic.readSecurityFilterInfo(choreographyKey, serviceId);
    }

    @Override
    public void configureSecurityFilter(
            final String key,
            final String serviceId,
            final URL federationServerURL) {
        logic.configureSecurityFilter(key, serviceId, federationServerURL);
    }

    @Override
    public AnyObjectTO getEnactmentEngine(final String choreographyKey) {
        return logic.getChoreographyEnactmentEngine(choreographyKey);
    }

    @Override
    public AnyObjectTO getSynthesisProcessor(final String choreographyKey) {
        return logic.getChoreographySynthesisProcessor(choreographyKey);
    }

}
