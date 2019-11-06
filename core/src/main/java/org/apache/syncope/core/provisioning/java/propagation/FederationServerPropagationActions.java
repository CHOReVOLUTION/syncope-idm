/*
 * Copyright 2015 The CHOReVOLUTION project
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
package org.apache.syncope.core.provisioning.java.propagation;

import java.util.HashSet;
import java.util.Set;
import org.apache.syncope.common.lib.types.AnyTypeKind;
import org.apache.syncope.core.persistence.api.dao.UserDAO;
import org.apache.syncope.core.persistence.api.entity.task.PropagationTask;
import org.apache.syncope.core.persistence.api.entity.user.User;
import org.apache.syncope.core.persistence.api.dao.AnyTypeDAO;
import org.apache.syncope.core.persistence.api.entity.group.GPlainAttr;
import org.apache.syncope.core.persistence.api.entity.resource.Provision;
import org.identityconnectors.common.security.GuardedString;
import org.identityconnectors.framework.common.objects.Attribute;
import org.identityconnectors.framework.common.objects.AttributeBuilder;
import org.identityconnectors.framework.common.objects.AttributeUtil;
import org.identityconnectors.framework.common.objects.ConnectorObject;
import org.identityconnectors.framework.common.objects.OperationalAttributes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

public class FederationServerPropagationActions extends DefaultPropagationActions {

    private static final Logger LOG = LoggerFactory.getLogger(FederationServerPropagationActions.class);

    private static final String GROUPS = "__GROUPS__";

    private static final String CHOREOGRAPHIES = "__CHOREOGRAPHIES__";

    @Autowired
    private AnyTypeDAO anyTypeDAO;

    @Autowired
    private UserDAO userDAO;

    @Transactional(readOnly = true)
    @Override
    public void before(final PropagationTask task, final ConnectorObject beforeObj) {
        super.before(task, beforeObj);

        Provision provision = task.getResource().getProvision(anyTypeDAO.findUser());
        if (AnyTypeKind.USER == task.getAnyTypeKind() && provision != null && provision.getMapping() != null) {
            User user = userDAO.find(task.getEntityKey());
            if (user != null) {
                Set<Attribute> attributes = new HashSet<>(task.getAttributes());

                // 1. ensure to send AES-encoded password
                if (user.getPassword() != null) {
                    Attribute pwdAttr = AttributeUtil.find(OperationalAttributes.PASSWORD_NAME, attributes);
                    if (pwdAttr != null) {
                        attributes.remove(pwdAttr);
                    }
                    attributes.add(AttributeBuilder.buildPassword(new GuardedString(user.getPassword().toCharArray())));
                }

                // 2. check security groups and choreographies
                Set<String> securityGroups = new HashSet<>();
                Set<String> choreographyGroupKeys = new HashSet<>();
                Set<String> choreographyGroups = new HashSet<>();
                userDAO.findAllGroups(user).forEach((group) -> {
                    GPlainAttr isChoreography = group.getPlainAttr("isChoreography");
                    if (isChoreography != null
                            && !isChoreography.getValues().isEmpty()
                            && isChoreography.getValues().get(0).getBooleanValue()) {

                        choreographyGroupKeys.add(group.getKey());
                        choreographyGroups.add(group.getName());
                    } else {
                        securityGroups.add(group.getName());
                    }
                });
                LOG.debug("Security groups memberships to propagate: {}", securityGroups);

                // populate security group memberships
                Attribute groups = AttributeUtil.find(GROUPS, attributes);
                if (groups != null) {
                    groups.getValue().forEach((obj) -> {
                        securityGroups.add(obj.toString());
                    });
                }
                attributes.add(AttributeBuilder.build(GROUPS, securityGroups));

                // populate choreography group memberships
                Attribute choreographies = AttributeUtil.find(CHOREOGRAPHIES, attributes);
                if (choreographies != null) {
                    choreographies.getValue().forEach((obj) -> {
                        choreographyGroups.add(obj.toString());
                    });
                }
                attributes.add(AttributeBuilder.build(CHOREOGRAPHIES, choreographyGroups));

                // 3. add per-service username / password attributes
                choreographyGroupKeys.stream().
                        map((choreographyKey) -> user.getMembership(choreographyKey)).
                        filter((membership) -> (membership != null)).
                        forEachOrdered((membership) -> {
                            user.getPlainAttrs(membership).stream().
                                    filter((attr) -> (!attr.getValuesAsStrings().isEmpty())).forEachOrdered((attr) -> {

                                attributes.add(AttributeBuilder.build(
                                        attr.getSchema().getKey(), attr.getValuesAsStrings().get(0)));
                            });
                        });

                task.setAttributes(attributes);
            }
        } else {
            LOG.debug("Not about user, not doing anything");
        }
    }

}
