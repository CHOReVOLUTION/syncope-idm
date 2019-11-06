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
package org.apache.syncope.core.persistence.jpa.dao;

import java.util.List;
import javax.persistence.TypedQuery;
import org.apache.syncope.core.persistence.jpa.entity.JPAChoreographyInstance;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.apache.syncope.core.persistence.api.entity.ChoreographyInstance;
import org.apache.syncope.core.persistence.api.dao.ChoreographyInstanceDAO;

@Repository
public class JPAChoreographyInstanceDAO extends AbstractDAO<ChoreographyInstance> implements ChoreographyInstanceDAO {

    @Transactional(readOnly = true)
    @Override
    public List<ChoreographyInstance> findAll() {
        TypedQuery<ChoreographyInstance> query = entityManager().createQuery(
                "SELECT e FROM " + JPAChoreographyInstance.class.getSimpleName() + " e ",
                ChoreographyInstance.class);
        return query.getResultList();
    }

    @Transactional(readOnly = true)
    @Override
    public List<ChoreographyInstance> findByChoreographyId(final String choreographyId) {
        TypedQuery<ChoreographyInstance> query = entityManager().createQuery(
                "SELECT e FROM " + JPAChoreographyInstance.class.getSimpleName()
                + " e WHERE e.choreographyId = :choreographyId",
                ChoreographyInstance.class);
        query.setParameter("choreographyId", choreographyId);
        return query.getResultList();
    }

    @Transactional(readOnly = true)
    @Override
    public ChoreographyInstance findById(final String id) {
        TypedQuery<ChoreographyInstance> query = entityManager().createQuery(
                "SELECT e FROM " + JPAChoreographyInstance.class.getSimpleName()
                + " e WHERE e.id = :id",
                ChoreographyInstance.class);
        query.setParameter("id", id);
        List<ChoreographyInstance> results = query.getResultList();

        if (results.isEmpty()) {
            return null;
        } else {
            return results.get(0);
        }
    }

    @Transactional(readOnly = true)
    @Override
    public ChoreographyInstance findByChoreographyInstanceId(
            final String choreographyId,
            final String choreographyInstanceId) {

        TypedQuery<ChoreographyInstance> query = entityManager().createQuery(
                "SELECT e FROM " + JPAChoreographyInstance.class.getSimpleName() + " e"
                + " WHERE e.choreographyId = :choreographyId"
                + " AND e.choreographyInstanceId = :choreographyInstanceId",
                ChoreographyInstance.class);
        query.setParameter("choreographyId", choreographyId);
        query.setParameter("choreographyInstanceId", choreographyInstanceId);

        List<ChoreographyInstance> results = query.getResultList();

        if (results.isEmpty()) {
            return null;
        } else {
            return results.get(0);
        }
    }

    @Transactional
    @Override
    public void save(final ChoreographyInstance choreographyInstance) {
        entityManager().persist(choreographyInstance);
    }

    @Transactional
    @Override
    public void delete(final ChoreographyInstance choreographyInstance) {
        entityManager().remove(choreographyInstance);
    }

    @Transactional
    @Override
    public void deleteByChoreographyId(final String choreographyId) {
        findByChoreographyId(choreographyId).forEach((choreographyInstance) -> {
            delete(choreographyInstance);
        });
    }

}
