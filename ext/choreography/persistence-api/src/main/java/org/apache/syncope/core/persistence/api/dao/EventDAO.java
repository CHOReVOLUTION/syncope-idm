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
package org.apache.syncope.core.persistence.api.dao;

import eu.chorevolution.idm.common.types.ArtifactType;
import java.util.List;
import org.apache.syncope.core.persistence.api.entity.Event;

public interface EventDAO extends DAO<Event> {

    List<Event> findAll();

    List<Event> findByChoreographyInstancePK(String choreographyInstancePK);

    List<Event> findByArtifactName(String choreographyInstancePK, String artifactName);

    List<Event> findByOperation(String choreographyInstancePK, String operationName);

    List<Event> findByOperation(
            String choreographyInstancePK, String operationName, ArtifactType artifactType, String artifactName);

    List<String> findArtifactsByType(String choreographyInstancePK, ArtifactType artifactType);

    List<String> findOperationsByArtifact(String choreographyInstancePK, String artifactName);

    List<String> findCdsByChoreography(String choreographyId);

    List<String> findServicesByChoreography(String choreographyId);

    List<String> findChoreographyOperationsByArtifact(String choreographyId, String artifactName);

    void save(Event event);

    void delete(Event event);

}
