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
package org.apache.syncope.client.console.panels;

import eu.chorevolution.idm.common.to.ChoreographyTO;
import org.apache.syncope.client.console.pages.AVGInstanceStatsPage;
import org.apache.syncope.client.console.rest.MonitorRestClient;
import org.apache.wicket.PageReference;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.ajax.markup.html.IndicatingAjaxLink;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

public class InstancesOverviewPanel extends Panel {

    private static final long serialVersionUID = -5092782537953795789L;

    public InstancesOverviewPanel(final String id,
            final PageReference pageReference,
            final ChoreographyTO choreography) {
        super(id);

        MonitorRestClient restClient = new MonitorRestClient();

        Boolean availableInstances = false;
        if (choreography.getChoreographyId() != null) {
            availableInstances = !restClient.instanceList(choreography.getChoreographyId()).isEmpty();
        }

        if (availableInstances) {
            WebMarkupContainer container = new WebMarkupContainer("container");
            add(container);

            container.add(new InstanceDirectoryPanel("instances", pageReference, choreography.getChoreographyId()));
            Double averageInstanceExecutionTime
                    = restClient.getAverageInstanceExecutionTime(choreography.getChoreographyId());
            container.add(new Label(
                    "avg_instances", getString("average") + ": " + averageInstanceExecutionTime + " ms"));

            ChoreographyActionsPanel averageDetailsLink = new ChoreographyActionsPanel("avg_details");
            averageDetailsLink.addAction(new IndicatingAjaxLink<Void>("link") {

                private static final long serialVersionUID = -7978723352517770644L;

                @Override
                public void onClick(final AjaxRequestTarget request) {
                    PageParameters parameters = new PageParameters();
                    parameters.add("chorId", choreography.getChoreographyId());
                    setResponsePage(AVGInstanceStatsPage.class, parameters);
                }
            }, ChoreographyActionsPanel.ChoreographyActionType.SHOW);
            container.add(averageDetailsLink);

            add(new WebMarkupContainer("message") {

                private static final long serialVersionUID = 9169935715713553356L;

                @Override
                public boolean isVisible() {
                    return false;
                } 
            }); 

        } else {
            add(new WebMarkupContainer("container") {

                private static final long serialVersionUID = 9169935715713553356L;

                @Override
                public boolean isVisible() {
                    return false;
                }
            });
            add(new WebMarkupContainer("message"));
        }
    }

}
