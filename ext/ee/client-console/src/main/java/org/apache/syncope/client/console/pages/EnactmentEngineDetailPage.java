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

import eu.chorevolution.idm.common.to.EnactmentEngineStatusTO;
import org.apache.syncope.client.console.BookmarkablePageLinkBuilder;
import org.apache.syncope.client.console.panels.EEVMDirectoryPanel;
import org.apache.syncope.client.console.rest.AnyObjectRestClient;
import org.apache.syncope.client.console.rest.EnactmentEngineRestClient;
import org.apache.syncope.client.console.widgets.ResourceWidget;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.syncope.common.lib.to.AnyObjectTO;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.request.mapper.parameter.PageParameters;

public class EnactmentEngineDetailPage extends BaseExtPage {

    private static final long serialVersionUID = 1092769178017851238L;

    private final AnyObjectRestClient restClient;

    private final String enactmentEngineKey;

    private final AnyObjectTO enactmentEngine;

    public EnactmentEngineDetailPage(final PageParameters parameters) {
        super(parameters);

        restClient = new AnyObjectRestClient();
        enactmentEngineKey = parameters.get("ee").toString();

        enactmentEngine = restClient.read(enactmentEngineKey);

        EnactmentEngineRestClient enactmentEngineRestClient = new EnactmentEngineRestClient();

        // Set page title
        body.add(new Label("header", getString("ee") + " " + enactmentEngine.getName()));

        body.add(BookmarkablePageLinkBuilder.build("dashboard", "dashboardBr", Dashboard.class));
        body.add(BookmarkablePageLinkBuilder.build("enactmentEnginesBr", EnactmentEnginePage.class));
        body.add(new Label("enactmentEngineName", enactmentEngine.getName()));

        try {

            EnactmentEngineStatusTO ee = enactmentEngineRestClient.getEEStatus(enactmentEngineKey);

            WebMarkupContainer content = new WebMarkupContainer("content");
            content.add(new Label("enactmentEngineDetailsPageContent", "PAGE CONTENT"));
            content.setOutputMarkupId(true);

            double ramPercentage = (ee.getRamUsage() / Float.valueOf(ee.getRamTotal())) * 100;
            double storagePercentage = (ee.getStorageUsage() / Float.valueOf(ee.getStorageTotal())) * 100;

            RepeatingView resourcesOverview = new RepeatingView("resourceWidget");
            resourcesOverview.add(new ResourceWidget(
                    resourcesOverview.newChildId(),
                    "bg-yellow", ee.getVirtualMachinesCount(), "Virtual Machines", "fa fa-cube"));
            resourcesOverview.add(new ResourceWidget(
                    resourcesOverview.newChildId(), "bg-red", "CPU utilization", ee.getCpuUsageRatio(), "fa fa-cogs"));
            resourcesOverview.add(new ResourceWidget(resourcesOverview.newChildId(), "bg-green",
                    ee.getRamUsage(), ee.getRamTotal(), "RAM utilization", ramPercentage, "MB", ""));
            resourcesOverview.add(new ResourceWidget(
                    resourcesOverview.newChildId(), "bg-aqua", ee.getStorageUsage(), ee.getStorageTotal(),
                    "Storage occupancy", storagePercentage, "GB", "fa fa-database"));
            content.add(resourcesOverview);

            content.add(new EEVMDirectoryPanel("virtualMachinesList", getPageReference(), enactmentEngineKey));

            body.add(content);
            body.add(new WebMarkupContainer("message") {

                private static final long serialVersionUID = 9169935715713553356L;
                @Override
                    public boolean isVisible() {
                        return false;
                }
            });

        } catch (RuntimeException e) {
                body.add(new WebMarkupContainer("message"));
                body.add(new WebMarkupContainer("content") {

                    private static final long serialVersionUID = 9169935715713553356L;

                    @Override
                    public boolean isVisible() {
                        return false;
                    }
                });
        }
        // Re-enable when entilements for this service will be defined
        //MetaDataRoleAuthorizationStrategy.authorize(content, ENABLE, CamelEntitlement.ROUTE_LIST);
    }
}
