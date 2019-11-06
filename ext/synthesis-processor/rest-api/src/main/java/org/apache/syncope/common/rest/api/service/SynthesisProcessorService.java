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
package org.apache.syncope.common.rest.api.service;

import eu.chorevolution.idm.common.to.SynthesisProcessorTO;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * REST operations for managing synthesis processor.
 */
@Path("synthesisProcessor")
public interface SynthesisProcessorService extends JAXRSService {

    /**
     * Create a new Synthesis Processor
     * 
     * @param synthesisProcessorTO Synthesis Processor to be created
     * @return the id of the created Synthesis Processor
     */
    @POST
    @Path("/create")
    @Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    Response create(SynthesisProcessorTO synthesisProcessorTO);

    /**
     * Update a Synthesis Processor
     * 
     * @param synthesisProcessorTO Synthesis Processor to be updated
     */
    @PUT
    @Path("/update")
    @Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    void update(SynthesisProcessorTO synthesisProcessorTO);

    /**
     * Delete a Synthesis Processor
     *
     * @param key Synthesis Processor key
     */
    @DELETE
    @Path("{key}")
    @Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    void delete(@NotNull @PathParam("key") String key);
}
