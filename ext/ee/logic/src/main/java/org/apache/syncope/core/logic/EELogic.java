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
package org.apache.syncope.core.logic;

import eu.chorevolution.idm.common.to.EnactmentEngineTO;
import java.lang.reflect.Method;
import org.apache.syncope.common.lib.AbstractBaseBean;
import org.apache.syncope.common.lib.SyncopeConstants;
import org.apache.syncope.common.lib.patch.AnyObjectPatch;
import org.apache.syncope.common.lib.patch.AttrPatch;
import org.apache.syncope.common.lib.patch.StringReplacePatchItem;
import org.apache.syncope.common.lib.to.AnyObjectTO;
import org.apache.syncope.common.lib.to.AttrTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EELogic extends AbstractTransactionalLogic<AbstractBaseBean> {

    private static final String ENACTMENT_ENGINE_TYPE = "ENACTMENT ENGINE";

    private static final String ENACTMENT_ENGINE_BASE_URL_SCHEMA = "enactmentEngineBaseURL";

    private static final String ENACTMENT_ENGINE_USERNAME_SCHEMA = "enactmentEngineUsername";

    private static final String ENACTMENT_ENGINE_PASSWORD_SCHEMA = "enactmentEnginePassword";

    @Autowired
    private AnyObjectLogic anyObjectLogic;

    public String create(final EnactmentEngineTO enactmentEngineTO) {
        AnyObjectTO ee = new AnyObjectTO();
        ee.setName(enactmentEngineTO.getName());
        ee.setType(ENACTMENT_ENGINE_TYPE);
        ee.setRealm(SyncopeConstants.ROOT_REALM);

        ee.getPlainAttrs().add(new AttrTO.Builder()
                .schema(ENACTMENT_ENGINE_BASE_URL_SCHEMA).value(enactmentEngineTO.getBaseUrl()).build());

        ee.getPlainAttrs().add(new AttrTO.Builder()
                .schema(ENACTMENT_ENGINE_USERNAME_SCHEMA).value(enactmentEngineTO.getUsername()).build());

        ee.getPlainAttrs().add(new AttrTO.Builder()
                .schema(ENACTMENT_ENGINE_PASSWORD_SCHEMA).value(enactmentEngineTO.getPassword()).build());

        return anyObjectLogic.create(ee, false).getEntity().getKey();
    }

    public void update(final EnactmentEngineTO updatedEEData) {
        AnyObjectTO enactmentEngine = anyObjectLogic.read(updatedEEData.getKey());
        AnyObjectPatch eePatch = new AnyObjectPatch();
        eePatch.setKey(updatedEEData.getKey());

        if (!enactmentEngine.getName().equals(updatedEEData.getName())) {
            eePatch.setName(new StringReplacePatchItem.Builder().value(updatedEEData.getName()).build());   
        }

        if (updatedEEData.getUsername() != null && !updatedEEData.getUsername()
                .equals(enactmentEngine.getPlainAttrMap().get(ENACTMENT_ENGINE_USERNAME_SCHEMA).getValues().get(0))) {
            eePatch.getPlainAttrs().add(new AttrPatch.Builder().attrTO(new AttrTO.Builder()
                    .schema(ENACTMENT_ENGINE_USERNAME_SCHEMA).value(updatedEEData.getUsername()).build()).build());
        }

        if (updatedEEData.getBaseUrl() != null && !updatedEEData.getBaseUrl()
                .equals(enactmentEngine.getPlainAttrMap().get(ENACTMENT_ENGINE_BASE_URL_SCHEMA).getValues().get(0))) {
            eePatch.getPlainAttrs().add(new AttrPatch.Builder().attrTO(new AttrTO.Builder()
                    .schema(ENACTMENT_ENGINE_BASE_URL_SCHEMA).value(updatedEEData.getBaseUrl()).build()).build());
        }

        if (updatedEEData.getPassword() != null && !updatedEEData.getPassword()
                .equals(enactmentEngine.getPlainAttrMap().get(ENACTMENT_ENGINE_PASSWORD_SCHEMA).getValues().get(0))) {
            eePatch.getPlainAttrs().add(new AttrPatch.Builder().attrTO(new AttrTO.Builder()
                    .schema(ENACTMENT_ENGINE_PASSWORD_SCHEMA).value(updatedEEData.getPassword()).build()).build());
        }

        anyObjectLogic.update(eePatch, false);
    }

    public void delete(final String enactmentEngineKey) {
        anyObjectLogic.delete(enactmentEngineKey, true);
    }

    @Override
    protected AbstractBaseBean resolveReference(final Method method, final Object... os)
            throws UnresolvedReferenceException {
        throw new UnresolvedReferenceException();
    }

}
