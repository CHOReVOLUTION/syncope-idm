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
package org.apache.syncope.client.console.rest;

import eu.chorevolution.idm.common.to.SynthesisProcessorTO;
import org.apache.syncope.common.rest.api.service.SynthesisProcessorService;

public class SynthesisProcessorRestClient extends BaseRestClient {

    private static final long serialVersionUID = 2264681003724992654L;

    public void create(final SynthesisProcessorTO synthesisProcessorTO) {
        getService(SynthesisProcessorService.class).create(synthesisProcessorTO);
    }

    public void update(final SynthesisProcessorTO synthesisProcessorTO) {
        getService(SynthesisProcessorService.class).update(synthesisProcessorTO);
    }

    public void delete(final String synthesisProcessorKey) {
        getService(SynthesisProcessorService.class).delete(synthesisProcessorKey);
    }
}
