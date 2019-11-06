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

import org.apache.syncope.core.persistence.api.entity.ChoreographyEntityFactory;
import org.springframework.stereotype.Component;
import org.apache.syncope.core.persistence.api.entity.Event;
import org.apache.syncope.core.persistence.api.entity.ChoreographyInstance;

@Component
public class JPAChoreographyEntityFactory implements ChoreographyEntityFactory {

    @Override
    public Event newEvent() {
        return new JPAEvent();
    }

    @Override
    public ChoreographyInstance newChoreographyInstance() {
        return new JPAChoreographyInstance();
    }

}
