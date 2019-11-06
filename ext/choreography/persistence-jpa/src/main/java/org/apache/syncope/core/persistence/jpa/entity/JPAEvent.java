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
package org.apache.syncope.core.persistence.jpa.entity;

import eu.chorevolution.idm.common.types.ArtifactType;
import eu.chorevolution.idm.common.types.EventType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import org.apache.syncope.core.persistence.api.entity.Event;
import org.apache.syncope.core.persistence.api.entity.ChoreographyInstance;

@Entity
@Table(name = JPAEvent.TABLE)
public class JPAEvent extends AbstractGeneratedKeyEntity implements Event {

    private static final long serialVersionUID = 7402736206016134723L;

    public static final String TABLE = "Events";

    @Column(name = "eventType", length = 50)
    private EventType eventType;

    @Column(name = "artifactType", length = 50)
    private ArtifactType artifactType;

    @Column(name = "artifactRole", length = 50)
    private String artifactRole;

    @Column(name = "artifactName", length = 255)
    private String artifactName;

    @Column(name = "targetArtifactName", length = 255, nullable = true)
    private String targetArtifactName;

    @Column(name = "sourceState", length = 50)
    private String sourceState;

    @Column(name = "targetState", length = 50)
    private String targetState;

    @Column(name = "operationName", length = 255)
    private String operationName;

    @Column(name = "eventTimestamp")
    private Long eventTimestamp;

    @ManyToOne
    @JoinColumn(name = "choreographyInstance_id", nullable = false)
    private JPAChoreographyInstance choreographyInstance;

    @Override
    public String getId() {
        return getKey();
    }

    @Override
    public EventType getEventType() {
        return this.eventType;
    }

    @Override
    public void setEventType(final EventType eventType) {
        this.eventType = eventType;
    }

    @Override
    public ArtifactType getArtifactType() {
        return this.artifactType;
    }

    @Override
    public void setArtifactType(final ArtifactType artifactType) {
        this.artifactType = artifactType;
    }

    @Override
    public String getArtifactRole() {
        return this.artifactRole;
    }

    @Override
    public void setArtifactRole(final String artifactRole) {
        this.artifactRole = artifactRole;
    }

    @Override
    public String getArtifactName() {
        return this.artifactName;
    }

    @Override
    public void setArtifactName(final String artifactName) {
        this.artifactName = artifactName;
    }

    @Override
    public String getSourceState() {
        return this.sourceState;
    }

    @Override
    public void setSourceState(final String sourceState) {
        this.sourceState = sourceState;
    }

    @Override
    public String getTargetState() {
        return this.targetState;
    }

    @Override
    public void setTargetArtifactName(final String targetArtifactName) {
        this.targetArtifactName = targetArtifactName;
    }

    @Override
    public String getTargetArtifactName() {
        return this.targetArtifactName;
    }

    @Override
    public void setTargetState(final String targetState) {
        this.targetState = targetState;
    }

    @Override
    public String getOperation() {
        return this.operationName;
    }

    @Override
    public void setOperation(final String operation) {
        this.operationName = operation;
    }

    @Override
    public Long getEventTimestamp() {
        return this.eventTimestamp;
    }

    @Override
    public void setEventTimestamp(final Long eventTimestamp) {
        this.eventTimestamp = eventTimestamp;
    }

    @Override
    public ChoreographyInstance getChoreographyInstance() {
        return this.choreographyInstance;
    }

    @Override
    public void setChoreographyInstance(final ChoreographyInstance choreographyInstance) {
        checkType(choreographyInstance, JPAChoreographyInstance.class);
        this.choreographyInstance = (JPAChoreographyInstance) choreographyInstance;
    }

}
