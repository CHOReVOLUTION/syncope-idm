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
package org.apache.syncope.core.persistence.api.entity;

import eu.chorevolution.idm.common.types.ArtifactType;
import eu.chorevolution.idm.common.types.EventType;

public interface Event extends Entity {

    String getId();

    EventType getEventType();

    void setEventType(EventType eventType);

    ArtifactType getArtifactType();

    void setArtifactType(ArtifactType artifactType);

    String getArtifactRole();

    void setArtifactRole(String artifactRole);

    String getArtifactName();

    void setArtifactName(String artifactName);

    String getSourceState();

    void setSourceState(String sourceState);

    String getTargetState();

    void setTargetArtifactName(String targetArtifactName);

    String getTargetArtifactName();

    void setTargetState(String targetState);

    String getOperation();

    void setOperation(String operation);

    Long getEventTimestamp();

    void setEventTimestamp(Long eventTimestamp);

    ChoreographyInstance getChoreographyInstance();

    void setChoreographyInstance(ChoreographyInstance choreographyInstance);
}
