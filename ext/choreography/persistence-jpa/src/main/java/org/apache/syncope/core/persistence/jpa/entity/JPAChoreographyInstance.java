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

import java.util.LinkedHashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import org.apache.syncope.core.persistence.api.entity.Event;
import org.apache.syncope.core.persistence.api.entity.ChoreographyInstance;

@Entity
@Table(name = JPAChoreographyInstance.TABLE)
public class JPAChoreographyInstance extends AbstractGeneratedKeyEntity implements ChoreographyInstance {

    private static final long serialVersionUID = 4730175715632012842L;

    public static final String TABLE = "ChoreographyInstances";

    @Column(nullable = false)
    private String choreographyInstanceId;

    @Column(nullable = true, length = 255)
    private String description;

    @Column(nullable = false)
    private String choreographyId;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true,
            fetch = FetchType.LAZY, mappedBy = "choreographyInstance")
    private Set<JPAEvent> events = new LinkedHashSet<>();

    @Override
    public String getChoreographyInstanceId() {
        return this.choreographyInstanceId;
    }

    @Override
    public void setChoreographyInstanceId(final String choreographyInstanceId) {
        this.choreographyInstanceId = choreographyInstanceId;
    }

    @Override
    public String getDescription() {
        return this.description;
    }

    @Override
    public void setDescription(final String description) {
        this.description = description;
    }

    @Override
    public String getChoreographyId() {
        return choreographyId;
    }

    @Override
    public void setChoreographyId(final String choreographyKey) {
        this.choreographyId = choreographyKey;
    }

    @Override
    public void add(final Event event) {
        checkType(event, JPAEvent.class);
        event.setChoreographyInstance(this);
        events.add((JPAEvent) event);
    }

    @Override
    public Set<? extends Event> getEvents() {
        return this.events;
    }

}
