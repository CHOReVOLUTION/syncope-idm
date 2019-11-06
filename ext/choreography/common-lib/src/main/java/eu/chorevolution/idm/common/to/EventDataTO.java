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
package eu.chorevolution.idm.common.to;

import eu.chorevolution.idm.common.types.ArtifactType;
import eu.chorevolution.idm.common.types.EventType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import org.apache.syncope.common.lib.AbstractBaseBean;

@XmlRootElement(name = "event_data")
@XmlType
public class EventDataTO extends AbstractBaseBean {

    private static final long serialVersionUID = 3482017510413664039L;

    private String choreographyName;

    private String choreographyInstanceId;

    private ArtifactType artifactType;

    private String artifactRole;

    private String artifactName;

    private String sourceState;

    private String targetState;

    private String operation;

    private EventType eventType;

    private String targetArtifactName;

    private long eventTimestamp;

    public String getChoreographyName() {
        return choreographyName;
    }

    public void setChoreographyName(final String choreographyName) {
        this.choreographyName = choreographyName;
    }

    public String getChoreographyInstanceId() {
        return choreographyInstanceId;
    }

    public void setChoreographyInstanceId(final String choreographyInstanceId) {
        this.choreographyInstanceId = choreographyInstanceId;
    }

    public ArtifactType getArtifactType() {
        return artifactType;
    }

    public void setArtifactType(final ArtifactType artifactType) {
        this.artifactType = artifactType;
    }

    public String getArtifactRole() {
        return artifactRole;
    }

    public void setArtifactRole(final String artifactRole) {
        this.artifactRole = artifactRole;
    }

    public String getArtifactName() {
        return artifactName;
    }

    public void setArtifactName(final String artifactName) {
        this.artifactName = artifactName;
    }

    public String getSourceState() {
        return sourceState;
    }

    public void setSourceState(final String sourceState) {
        this.sourceState = sourceState;
    }

    public String getTargetState() {
        return targetState;
    }

    public void setTargetState(final String targetState) {
        this.targetState = targetState;
    }

    public String getTargetArtifactName() {
        return targetArtifactName;
    }

    public void setTargetArtifactName(final String targetArtifactName) {
        this.targetArtifactName = targetArtifactName;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(final String operation) {
        this.operation = operation;
    }

    public EventType getEventType() {
        return eventType;
    }

    public void setEventType(final EventType eventType) {
        this.eventType = eventType;
    }

    public long getEventTimestamp() {
        return eventTimestamp;
    }

    public void setEventTimestamp(final long eventTimestamp) {
        this.eventTimestamp = eventTimestamp;
    }

}
