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
package org.apache.syncope.core.logic;

import eu.chorevolution.idm.common.BusinessException;
import eu.chorevolution.idm.common.to.ChoreographyInstanceTO;
import eu.chorevolution.idm.common.to.EventDataTO;
import java.lang.reflect.Method;
import java.util.List;
import org.apache.syncope.common.lib.AbstractBaseBean;
import org.apache.syncope.core.persistence.api.dao.ChoreographyDAO;
import org.apache.syncope.core.persistence.api.dao.EventDAO;
import org.apache.syncope.core.persistence.api.entity.Event;
import org.apache.syncope.core.persistence.jpa.entity.JPAEvent;
import org.apache.syncope.core.persistence.jpa.entity.JPAChoreographyInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.apache.syncope.core.persistence.api.entity.ChoreographyInstance;
import org.apache.syncope.core.persistence.api.dao.ChoreographyInstanceDAO;
import org.apache.syncope.core.persistence.api.entity.group.GPlainAttr;
import org.apache.syncope.core.persistence.api.entity.group.Group;

@Component
public class CollectorLogic extends AbstractTransactionalLogic<AbstractBaseBean> {

    @Override
    protected AbstractBaseBean resolveReference(final Method method, final Object... os)
            throws UnresolvedReferenceException {
        throw new UnresolvedReferenceException();
    }

    @Autowired
    private EventDAO eventDAO;

    @Autowired
    private ChoreographyInstanceDAO choreographyInstanceDAO;

    @Autowired
    private ChoreographyDAO choreographyDAO;

    public void saveLogEvent(final EventDataTO data) throws BusinessException {

        Group choreography = choreographyDAO.findByName(data.getChoreographyName());
        ChoreographyInstance choreographyInstance = null;
        if (choreography != null) {
            GPlainAttr choreographyId = choreography.getPlainAttr("id");
            if (choreographyId != null && choreographyId.getUniqueValue() != null) {
                choreographyInstance =
                        choreographyInstanceDAO.findByChoreographyInstanceId(
                                choreographyId.getUniqueValue().getStringValue(),
                                data.getChoreographyInstanceId());
            }
        }

        if (choreographyInstance == null) {
            throw new BusinessException("Cannot find choreography instance");
        }

        //Log event
        Event event = new JPAEvent();
        event.setArtifactName(data.getArtifactName());
        event.setArtifactRole(data.getArtifactRole());
        event.setArtifactType(data.getArtifactType());
        event.setOperation(data.getOperation());
        event.setSourceState(data.getSourceState());
        event.setTargetState(data.getTargetState());
        event.setEventTimestamp(data.getEventTimestamp());
        event.setEventType(data.getEventType());

        choreographyInstance.add(event);

        eventDAO.save(event);

    }

    public void addInstance(final ChoreographyInstanceTO data) throws BusinessException {
        Group choreography = choreographyDAO.findByName(data.getChoreographyName());
        if (choreography == null) {
            throw new BusinessException("Cannot find choreography with name " + data.getChoreographyName());
        }

        ChoreographyInstance choreographyInstance = new JPAChoreographyInstance();
        choreographyInstance.setChoreographyInstanceId(data.getChoreographyInstanceId());
        choreographyInstance.setDescription(data.getInstanceDescription());
        GPlainAttr choreographyId = choreography.getPlainAttr("id");
        if (choreographyId != null && choreographyId.getUniqueValue() != null) {
            choreographyInstance.setChoreographyId(choreographyId.getUniqueValue().getStringValue());
        }

        choreographyInstanceDAO.save(choreographyInstance);
    }

    //TODO remove this method
    public String getDatabase() {
        List<Group> chors = choreographyDAO.findAll();
        List<ChoreographyInstance> ins = choreographyInstanceDAO.findAll();
        List<Event> es = eventDAO.findAll();

        String r = "CHOREOGRAPHIES:\n";
        for (Group current : chors) {
            r = r + "-> ID: " + current.getKey() + "\t" + current.getName() + "\n";
        }

        r = r + "\nINSTANCES:\n";
        for (ChoreographyInstance current : ins) {
            r = r + "-> ID: " + current.getKey() + "\tChoreography ID: " + current.getChoreographyId()
                    + "\tInstance: " + current.getChoreographyInstanceId()
                    + "\tDescription: " + current.getDescription() + "\n";
        }

        r = r + "\nEVENTS:\n";
        for (Event current : es) {
            r = r + "-> ID: " + current.getKey() + "\tInstance ID: " + current.getChoreographyInstance().getKey()
                    + "\t" + current.getArtifactName() + "\t" + current.getOperation()
                    + "\t" + current.getEventType() + "\t" + current.getEventTimestamp() + "\n";
        }

        return r;
    }
}
