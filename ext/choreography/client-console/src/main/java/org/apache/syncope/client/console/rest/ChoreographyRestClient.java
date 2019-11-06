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
package org.apache.syncope.client.console.rest;

import eu.chorevolution.idm.common.to.ChoreographyTO;
import eu.chorevolution.idm.common.types.ChoreographyAction;
import static org.apache.syncope.client.console.rest.BaseRestClient.getService;
import org.apache.syncope.common.lib.to.AnyObjectTO;
import org.apache.syncope.common.rest.api.service.ChoreographyService;

public class ChoreographyRestClient extends BaseRestClient {

    private static final long serialVersionUID = -2018208424159468912L;
        
    public ChoreographyTO getChoreography(final String choreographyKey) {
        return getService(ChoreographyService.class).read(choreographyKey).readEntity(ChoreographyTO.class);
    }

    public void updateChoreograhpy(final ChoreographyTO choreographyTO) {
        getService(ChoreographyService.class).update(choreographyTO);
    }

    public void deleteChoreography(final String choreographyKey) {
        getService(ChoreographyService.class).delete(choreographyKey);
    }

    public void enactChoreography(final String choregographyKey, final String enactmentEngineKey) {
        getService(ChoreographyService.class).enact(choregographyKey, enactmentEngineKey);
    }

    public void startChoreography(final String choreographyKey) {
        getService(ChoreographyService.class)
                .onChoreography(choreographyKey, ChoreographyAction.START, null);
    }

    public void stopChoreography(final String choreographyKey) {
        getService(ChoreographyService.class)
                .onChoreography(choreographyKey, ChoreographyAction.STOP, null);
    }

    public void freezeChoreography(final String choreographyKey) {
        getService(ChoreographyService.class)
                .onChoreography(choreographyKey, ChoreographyAction.FREEZE, null);
    }

    public void unfreezeChoreography(final String choreographyKey) {
        getService(ChoreographyService.class)
                .onChoreography(choreographyKey, ChoreographyAction.UNFREEZE, null);
    }

    public void resizeChoreography(final String choreographyKey, final Integer size) {
        getService(ChoreographyService.class)
                .onChoreography(choreographyKey, ChoreographyAction.RESIZE, size);
    }

    public AnyObjectTO getChoreographyEE(final String choreographyKey) {
        return getService(ChoreographyService.class).getEnactmentEngine(choreographyKey);
    }

    public AnyObjectTO getChoreographySynthesisProcessor(final String choreographyKey) {
        return getService(ChoreographyService.class).getSynthesisProcessor(choreographyKey);
    }
}
