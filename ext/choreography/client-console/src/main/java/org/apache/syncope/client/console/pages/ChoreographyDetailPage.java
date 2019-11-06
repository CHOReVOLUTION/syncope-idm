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

import de.agilecoders.wicket.core.markup.html.bootstrap.tabs.AjaxBootstrapTabbedPanel;
import eu.chorevolution.idm.common.to.ChoreographyTO;
import java.util.ArrayList;
import java.util.List;
import org.apache.syncope.client.console.BookmarkablePageLinkBuilder;
import org.apache.syncope.client.console.panels.ChoreographyOverviewPanel;
import org.apache.syncope.client.console.panels.ChoreographyServiceDirectoryPanel;
import org.apache.syncope.client.console.panels.ChoreographyEEResourcesPanel;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.syncope.client.console.panels.InstancesOverviewPanel;
import org.apache.syncope.client.console.panels.LabelPanel;
import org.apache.syncope.client.console.rest.ChoreographyRestClient;
import org.apache.syncope.common.lib.to.AnyObjectTO;
import org.apache.wicket.extensions.markup.html.tabs.AbstractTab;
import org.apache.wicket.extensions.markup.html.tabs.ITab;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

public class ChoreographyDetailPage extends BaseExtPage {

    private static final long serialVersionUID = 2371823932245590233L;

    public static final String PREF_INSTANCE_PAGINATOR_ROWS = "instances.paginator.rows";

    public static final String PREF_SERVICE_PAGINATOR_ROWS = "services.paginator.rows";

    public static final String PREF_VM_PAGINATOR_ROWS = "virtualMachines.paginator.rows";

    private final ChoreographyRestClient restClient;

    private final String choreographyKey;

    private final ChoreographyTO choreography;

    public ChoreographyDetailPage(final PageParameters parameters) {
        super(parameters);

        choreographyKey = parameters.get("chor").toString();

        restClient = new ChoreographyRestClient();

        choreography = restClient.getChoreography(choreographyKey);

        body.add(new Label("header", choreography.getName()));

        body.add(BookmarkablePageLinkBuilder.build("dashboard", "dashboardBr", Dashboard.class));
        body.add(BookmarkablePageLinkBuilder.build("choreographyBr", ChoreographyPage.class));
        body.add(new Label("choreographyTitle", choreography.getName()));

        WebMarkupContainer content = new WebMarkupContainer("content");
        content.setOutputMarkupId(true);

        content.add(new AjaxBootstrapTabbedPanel<>("tabbedPanel", buildTabList()));

        // Re-enable when entilements for this service will be defined
        //MetaDataRoleAuthorizationStrategy.authorize(content, ENABLE, CamelEntitlement.ROUTE_LIST);
        body.add(content);
    }

    private List<ITab> buildTabList() {
        final List<ITab> tabs = new ArrayList<>();

        tabs.add(new AbstractTab(new ResourceModel("overview")) {
            private static final long serialVersionUID = -5861786415855103549L;
            @Override
            public WebMarkupContainer getPanel(final String panelId) {
                return new ChoreographyOverviewPanel(panelId, choreography);
            }
        });

        try {
            AnyObjectTO ee = restClient.getChoreographyEE(choreography.getKey());
            tabs.add(new AbstractTab(new ResourceModel("eeResources")) {
                private static final long serialVersionUID = -5861786415855103549L;
                @Override
                public WebMarkupContainer getPanel(final String panelId) {
                    return new ChoreographyEEResourcesPanel(panelId, getPageReference(), choreography, ee);
                }
            });
        } catch (Exception e) {
            tabs.add(new AbstractTab(new ResourceModel("eeResources")) {
                private static final long serialVersionUID = -5861786415855103549L;
                @Override
                public WebMarkupContainer getPanel(final String panelId) {
                    return new LabelPanel(panelId, new Label(
                            "label", "Enactment Engine informations not available for this choreography"));
                }
            });
        }

        tabs.add(new AbstractTab(new ResourceModel("instances")) {
            private static final long serialVersionUID = -5861786415855103549L;
            @Override
            public WebMarkupContainer getPanel(final String panelId) {
                return new InstancesOverviewPanel(panelId, getPageReference(), choreography);
            }
        });

        tabs.add(new AbstractTab(new ResourceModel("services")) {
            private static final long serialVersionUID = -5861786415855103549L;
            @Override
            public WebMarkupContainer getPanel(final String panelId) {
                return new ChoreographyServiceDirectoryPanel(panelId, getPageReference(), choreographyKey);
            }
        });

        return tabs;
    }

}
