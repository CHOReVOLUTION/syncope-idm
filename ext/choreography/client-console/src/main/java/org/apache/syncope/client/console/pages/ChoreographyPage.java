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

import eu.chorevolution.idm.common.ChorevolutionEntitlement;
import org.apache.syncope.client.console.BookmarkablePageLinkBuilder;
import org.apache.syncope.client.console.annotations.ExtPage;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.syncope.client.console.panels.ChoreographyDirectoryPanel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

@ExtPage(label = "Choreographies", icon = "fa-bar-chart",
        listEntitlement = ChorevolutionEntitlement.CHOREOGRAPHY_LIST, priority = 100)
public class ChoreographyPage extends BaseExtPage {

    private static final long serialVersionUID = 1965360932245590233L;

    public static final String PREF_CHOREOGRAPHY_PAGINATOR_ROWS = "choreography.paginator.rows";

    public ChoreographyPage(final PageParameters parameters) {
        super(parameters);

        body.add(BookmarkablePageLinkBuilder.build("dashboard", "dashboardBr", Dashboard.class));

        WebMarkupContainer content = new WebMarkupContainer("content");
        content.setOutputMarkupId(true);

        content.add(new ChoreographyDirectoryPanel("choreographies", getPageReference()));

        // Re-enable when entilements for this service will be defined
        //MetaDataRoleAuthorizationStrategy.authorize(content, ENABLE, CamelEntitlement.ROUTE_LIST);
        body.add(content);
    }

}
