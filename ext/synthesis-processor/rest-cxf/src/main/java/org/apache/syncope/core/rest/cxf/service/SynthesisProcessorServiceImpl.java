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
package org.apache.syncope.core.rest.cxf.service;

import eu.chorevolution.idm.common.to.SynthesisProcessorTO;
import javax.ws.rs.core.Response;
import org.apache.syncope.common.rest.api.service.SynthesisProcessorService;
import org.apache.syncope.core.logic.SynthesisProcessorLogic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SynthesisProcessorServiceImpl extends AbstractServiceImpl implements SynthesisProcessorService {

    @Autowired
    private SynthesisProcessorLogic logic;

    @Override
    public Response create(final SynthesisProcessorTO synthesisProcessorTO) {
        return Response.status(Response.Status.CREATED).
                header("X-CHOReVOLUTION-SynthesisProcessorId", logic.create(synthesisProcessorTO)).
                build();
    }

    @Override
    public void update(final SynthesisProcessorTO synthesisProcessorTO) {
        logic.update(synthesisProcessorTO);
    }

    @Override
    public void delete(final String synthesisProcessorKey) {
        logic.delete(synthesisProcessorKey);
    }
}
