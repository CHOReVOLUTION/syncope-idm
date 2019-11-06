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
package org.apache.syncope.client.console.pages;

import org.apache.syncope.client.console.BookmarkablePageLinkBuilder;
import org.apache.syncope.client.console.rest.AnyObjectRestClient;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.syncope.common.lib.to.AnyObjectTO;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.request.mapper.parameter.PageParameters;

public class SynthesisProcessorDetailPage extends BaseExtPage {

    private static final long serialVersionUID = 1092769178017851238L;

    private final AnyObjectRestClient restClient;

    private final String synthesisProcessorKey;

    private final AnyObjectTO synthesisProcessor;

    public SynthesisProcessorDetailPage(final PageParameters parameters) {
        super(parameters);

        restClient = new AnyObjectRestClient();
        synthesisProcessorKey = parameters.get("sp").toString();

        synthesisProcessor = restClient.read(synthesisProcessorKey);

        //SynthesisProcessorRestClient synthesisProcessorRestClient = new SynthesisProcessorRestClient();

        // Set page title
        body.add(new Label("header", getString("sp") + " " + synthesisProcessor.getName()));

        body.add(BookmarkablePageLinkBuilder.build("dashboard", "dashboardBr", Dashboard.class));
        body.add(BookmarkablePageLinkBuilder.build("synthesisProcessorBr", SynthesisProcessorPage.class));
        body.add(new Label("synthesisProcessorName", synthesisProcessor.getName()));

        WebMarkupContainer content = new WebMarkupContainer("content");
        content.setOutputMarkupId(true);

        //CONTENT!

        // Re-enable when entilements for this service will be defined
        //MetaDataRoleAuthorizationStrategy.authorize(content, ENABLE, CamelEntitlement.ROUTE_LIST);
        body.add(content);
    }
}
