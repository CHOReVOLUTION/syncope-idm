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
package org.apache.syncope.core.workflow.java;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.syncope.common.lib.patch.GroupPatch;
import org.apache.syncope.common.lib.to.GroupTO;
import org.apache.syncope.common.lib.types.AnyTypeKind;
import org.apache.syncope.common.lib.types.ResourceOperation;
import org.apache.syncope.core.persistence.api.dao.PlainSchemaDAO;
import org.apache.syncope.core.persistence.api.entity.AnyUtils;
import org.apache.syncope.core.persistence.api.entity.AnyUtilsFactory;
import org.apache.syncope.core.persistence.api.entity.PlainSchema;
import org.apache.syncope.core.persistence.api.entity.group.GPlainAttr;
import org.apache.syncope.core.persistence.api.entity.group.Group;
import org.apache.syncope.core.provisioning.api.PropagationByResource;
import org.apache.syncope.core.provisioning.api.WorkflowResult;
import org.apache.syncope.core.provisioning.api.utils.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;

public class ChorevolutionGroupWorkflowAdapter extends DefaultGroupWorkflowAdapter {

    @Autowired
    private PlainSchemaDAO plainSchemaDAO;

    @Autowired
    private AnyUtilsFactory anyUtilsFactory;

    @Override
    public WorkflowResult<String> create(final GroupTO groupTO) {

        Group group = entityFactory.newEntity(Group.class);
        if (groupTO.getAuxClasses().contains("Choreography")) {
            AnyUtils anyUtils = anyUtilsFactory.getInstance(AnyTypeKind.GROUP);

            PlainSchema schema = plainSchemaDAO.find("isChoreography");
            if (schema != null) {
                GPlainAttr attr = anyUtils.newPlainAttr();
                attr.setOwner(group);
                attr.setSchema(schema);
                attr.add("true", anyUtils);
                group.add(attr);
            }
        }
        dataBinder.create(group, groupTO);
        group = groupDAO.save(group);

        PropagationByResource propByRes = new PropagationByResource();
        propByRes.set(ResourceOperation.CREATE, group.getResourceKeys());

        return new WorkflowResult<>(group.getKey(), propByRes, "create");
    }

    @Override
    protected WorkflowResult<String> doUpdate(final Group group, final GroupPatch groupPatch) {
        if (CollectionUtils.collect(group.getAuxClasses(), EntityUtils.keyTransformer()).contains("Choreography")) {
            GPlainAttr attr = group.getPlainAttr("isChoreography");
            if (attr == null) {
                PlainSchema schema = plainSchemaDAO.find("isChoreography");
                if (schema != null) {
                    AnyUtils anyUtils = anyUtilsFactory.getInstance(AnyTypeKind.GROUP);

                    attr = anyUtils.newPlainAttr();
                    attr.setOwner(group);
                    attr.setSchema(schema);
                    attr.add("true", anyUtils);
                    group.add(attr);
                }
            }
        }
        return super.doUpdate(group, groupPatch);
    }

}
