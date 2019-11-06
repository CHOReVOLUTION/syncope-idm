/*
 * Copyright 2017 The CHOReVOLUTION project
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

import eu.chorevolution.idm.common.to.SynthesisProcessorTO;
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
public class SynthesisProcessorLogic extends AbstractLogic<AbstractBaseBean> {

    private static final String SYNTHESIS_PROCESSOR_TYPE = "SYNTHESIS PROCESSOR";

    private static final String SYNTHESIS_PROCESSOR_BASE_URL_SCHEMA = "synthesisProcessorBaseURL";

    private static final String SYNTHESIS_PROCESSOR_USERNAME_SCHEMA = "synthesisProcessorUsername";

    private static final String SYNTHESIS_PROCESSOR_PASSWORD_SCHEMA = "synthesisProcessorPassword";

    @Autowired
    private AnyObjectLogic anyObjectLogic;

    public String create(final SynthesisProcessorTO synthesisProcessorTO) {
        AnyObjectTO sp = new AnyObjectTO();
        sp.setName(synthesisProcessorTO.getName());
        sp.setType(SYNTHESIS_PROCESSOR_TYPE);
        sp.setRealm(SyncopeConstants.ROOT_REALM);

        sp.getPlainAttrs().add(new AttrTO.Builder()
                .schema(SYNTHESIS_PROCESSOR_BASE_URL_SCHEMA).value(synthesisProcessorTO.getBaseUrl()).build());

        sp.getPlainAttrs().add(new AttrTO.Builder()
                .schema(SYNTHESIS_PROCESSOR_USERNAME_SCHEMA).value(synthesisProcessorTO.getUsername()).build());

        sp.getPlainAttrs().add(new AttrTO.Builder()
                .schema(SYNTHESIS_PROCESSOR_PASSWORD_SCHEMA).value(synthesisProcessorTO.getPassword()).build());

        return anyObjectLogic.create(sp, false).getEntity().getKey();
    }

    public void update(final SynthesisProcessorTO updatedSPData) {
        AnyObjectTO enactmentEngine = anyObjectLogic.read(updatedSPData.getKey());
        AnyObjectPatch spPatch = new AnyObjectPatch();
        spPatch.setKey(updatedSPData.getKey());

        if (!enactmentEngine.getName().equals(updatedSPData.getName())) {
            spPatch.setName(new StringReplacePatchItem.Builder().value(updatedSPData.getName()).build());   
        }

        if (updatedSPData.getBaseUrl() != null && !updatedSPData.getBaseUrl().equals(enactmentEngine.getPlainAttrMap()
                        .get(SYNTHESIS_PROCESSOR_BASE_URL_SCHEMA).getValues().get(0))) {
            spPatch.getPlainAttrs().add(new AttrPatch.Builder().attrTO(new AttrTO.Builder()
                    .schema(SYNTHESIS_PROCESSOR_BASE_URL_SCHEMA).value(updatedSPData.getBaseUrl()).build()).build());
        }

        if (updatedSPData.getUsername() != null && !updatedSPData.getUsername().equals(enactmentEngine.getPlainAttrMap()
                        .get(SYNTHESIS_PROCESSOR_USERNAME_SCHEMA).getValues().get(0))) {
            spPatch.getPlainAttrs().add(new AttrPatch.Builder().attrTO(new AttrTO.Builder()
                    .schema(SYNTHESIS_PROCESSOR_USERNAME_SCHEMA).value(updatedSPData.getUsername()).build()).build());
        }

        if (updatedSPData.getPassword() != null && !updatedSPData.getPassword().equals(enactmentEngine.getPlainAttrMap()
                        .get(SYNTHESIS_PROCESSOR_PASSWORD_SCHEMA).getValues().get(0))) {
            spPatch.getPlainAttrs().add(new AttrPatch.Builder().attrTO(new AttrTO.Builder()
                    .schema(SYNTHESIS_PROCESSOR_PASSWORD_SCHEMA).value(updatedSPData.getPassword()).build()).build());
        }

        anyObjectLogic.update(spPatch, false);
    }

    public void delete(final String synthesisProcessorKey) {
        anyObjectLogic.delete(synthesisProcessorKey, true);
    }

    @Override
    protected AbstractBaseBean resolveReference(final Method method, final Object... args)
            throws UnresolvedReferenceException {

        throw new UnresolvedReferenceException();
    }

}
