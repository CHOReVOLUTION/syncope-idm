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
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.IterableUtils;
import org.apache.syncope.core.persistence.api.dao.ChoreographyDAO;
import org.apache.syncope.core.persistence.api.dao.GroupDAO;
import org.apache.syncope.core.persistence.api.entity.AnyTypeClass;
import org.apache.syncope.core.persistence.api.entity.PlainAttrValue;
import org.apache.syncope.core.persistence.api.entity.group.Group;
import org.apache.syncope.core.persistence.jpa.entity.group.JPAGPlainAttrUniqueValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly = true)
public class JPAChoreographyDAO extends AbstractDAO<Group> implements ChoreographyDAO {

    @Autowired
    private GroupDAO groupDAO;

    @Override
    public List<Group> findAll() {
        List<Group> all = groupDAO.findAll();
        CollectionUtils.filter(
                all,
                (final Group group) -> IterableUtils.matchesAny(
                        group.getAuxClasses(),
                        (final AnyTypeClass anyTypeClass) -> "Choreography".equals(anyTypeClass.getKey())));
        return all;
    }

    @Override
    public Group findById(final String id) {
        PlainAttrValue value = new JPAGPlainAttrUniqueValue();
        value.setStringValue(id);

        return groupDAO.findByAttrUniqueValue("id", value);
    }

    @Override
    public Group findByName(final String name) {
        return groupDAO.findByName(name);
    }

}
