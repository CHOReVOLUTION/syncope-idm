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
package org.apache.syncope.client.console.commons;

import org.apache.syncope.client.console.rest.AbstractAnyRestClient;
import org.apache.syncope.common.lib.to.AnyObjectTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EnactmentEngineDataProvider extends AnyDataProvider<AnyObjectTO> {

    private static final long serialVersionUID = 3628773545540989992L;

    protected static final Logger LOG = LoggerFactory.getLogger(EnactmentEngineDataProvider.class);

    public EnactmentEngineDataProvider(
            final AbstractAnyRestClient<AnyObjectTO, ?> restClient,
            final int paginatorRows,
            final boolean filtered,
            final String realm,
            final String type) {
        super(restClient, paginatorRows, filtered, realm, type);
    }

    @Override
    public AnyDataProvider<AnyObjectTO> setFIQL(final String fiql) {
        this.fiql = fiql;
        LOG.info("Set anyObject search query to '{}'", this.fiql);
        return this;
    }
}
