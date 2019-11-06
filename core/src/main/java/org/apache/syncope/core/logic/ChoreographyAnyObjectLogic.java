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
package org.apache.syncope.core.logic;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.syncope.common.lib.patch.AnyObjectPatch;
import org.apache.syncope.common.lib.to.AnyObjectTO;
import org.apache.syncope.common.lib.to.PropagationStatus;
import org.apache.syncope.common.lib.to.ProvisioningResult;
import org.apache.syncope.common.lib.to.UserTO;
import org.apache.syncope.common.lib.types.AnyEntitlement;
import org.apache.syncope.common.lib.types.AnyTypeKind;
import org.apache.syncope.core.provisioning.api.LogicActions;
import org.apache.syncope.core.provisioning.api.data.UserDataBinder;
import org.apache.syncope.core.spring.security.AuthContextUtils;
import org.apache.syncope.core.spring.security.DelegatedAdministrationException;
import org.springframework.beans.factory.annotation.Autowired;

public class ChoreographyAnyObjectLogic extends AnyObjectLogic {

    private static final String SERVICE_ANYTYPE = "SERVICE";

    private static final String SERVICE_ROLE_ANYTYPE_CLASS = "SERVICE ROLE";

    private static final String SERVICE_PROVIDER_ROLE = "Service provider";

    @Resource(name = "adminUser")
    private String adminUser;

    @Autowired
    private RoleLogic roleLogic;

    @Autowired
    protected UserDataBinder userDataBinder;

    /**
     * Special security checks for services and services roles.
     *
     * @param anyObjectTO any object to check permissions against
     */
    private void chorevolutionSecurityChecks(final AnyObjectTO anyObjectTO) {
        if (adminUser.equals(AuthContextUtils.getUsername())
                || (!SERVICE_ANYTYPE.equals(anyObjectTO.getType())
                && !SERVICE_ROLE_ANYTYPE_CLASS.equals(anyObjectTO.getType()))) {

            return;
        }

        UserTO requestor = userDataBinder.getAuthenticatedUserTO();
        Set<String> roles = new HashSet<>(requestor.getRoles());
        roles.addAll(requestor.getDynRoles());
        roles.remove(SERVICE_PROVIDER_ROLE);

        Set<String> entitlements = new HashSet<>();
        roles.forEach((role) -> {
            entitlements.addAll(roleLogic.read(role).getEntitlements());
        });

        if (!entitlements.contains(AnyEntitlement.UPDATE.getFor(anyObjectTO.getType()))
                && !anyObjectTO.getCreator().equals(requestor.getUsername())) {

            throw new DelegatedAdministrationException(AnyTypeKind.ANY_OBJECT, anyObjectTO.getKey());
        }
    }

    @Override
    public ProvisioningResult<AnyObjectTO> update(
            final AnyObjectPatch anyObjectPatch, final boolean nullPriorityAsync) {

        AnyObjectTO anyObjectTO = binder.getAnyObjectTO(anyObjectPatch.getKey());
        Pair<AnyObjectPatch, List<LogicActions>> before = beforeUpdate(anyObjectPatch, anyObjectTO.getRealm());

        String realm =
                before.getLeft().getRealm() != null && StringUtils.isNotBlank(before.getLeft().getRealm().getValue())
                ? before.getLeft().getRealm().getValue()
                : anyObjectTO.getRealm();
        Set<String> effectiveRealms = getEffectiveRealms(
                AuthContextUtils.getAuthorizations().get(AnyEntitlement.UPDATE.getFor(anyObjectTO.getType())),
                realm);

        // CHOReVOLUTION: CRV-188
        chorevolutionSecurityChecks(anyObjectTO);

        securityChecks(effectiveRealms, realm, before.getLeft().getKey());

        Pair<String, List<PropagationStatus>> updated = provisioningManager.update(anyObjectPatch, nullPriorityAsync);

        return after(binder.getAnyObjectTO(updated.getKey()), updated.getRight(), before.getRight());
    }

}
