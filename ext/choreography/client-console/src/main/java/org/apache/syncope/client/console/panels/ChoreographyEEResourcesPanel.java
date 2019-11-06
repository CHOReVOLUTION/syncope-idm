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

import de.agilecoders.wicket.core.markup.html.bootstrap.dialog.Modal;
import eu.chorevolution.idm.common.to.ChoreographyTO;
import eu.chorevolution.idm.common.to.EnactmentEngineStatusTO;
import java.io.Serializable;
import org.apache.commons.lang3.StringUtils;
import org.apache.syncope.client.console.SyncopeConsoleSession;
import org.apache.syncope.client.console.commons.Constants;
import org.apache.syncope.client.console.pages.BasePage;
import static org.apache.syncope.client.console.panels.DirectoryPanel.LOG;
import org.apache.syncope.client.console.rest.ChoreographyRestClient;
import org.apache.syncope.client.console.rest.MonitorRestClient;
import org.apache.syncope.client.console.wicket.markup.html.bootstrap.dialog.BaseModal;
import org.apache.syncope.client.console.widgets.ResourceWidget;
import org.apache.syncope.common.lib.SyncopeClientException;
import org.apache.syncope.common.lib.to.AnyObjectTO;
import org.apache.wicket.PageReference;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.ajax.markup.html.IndicatingAjaxLink;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.model.Model;

public class ChoreographyEEResourcesPanel extends Panel {

    private static final long serialVersionUID = 8745189061396851234L;

    private final ChoreographyRestClient choreographyRestClient;

    private final MonitorRestClient monitorRestClient;

    private final AnyObjectTO enactmentEngine;

    private final ChoreographyTO choreography;

    private final PageReference pageRef;

    private WebMarkupContainer content;

    private final BaseModal<Serializable> utilityModal = new BaseModal<>("resizeEEModal");

    public ChoreographyEEResourcesPanel(
            final String id,
            final PageReference pageRef,
            final ChoreographyTO choreography,
            final AnyObjectTO enactmentEngine) {
        super(id);

        this.choreography = choreography;
        this.enactmentEngine = enactmentEngine;
        this.choreographyRestClient = new ChoreographyRestClient();
        this.monitorRestClient = new MonitorRestClient();
        this.pageRef = pageRef;

        this.content = new WebMarkupContainer("eeDetails");
        WebMarkupContainer message;

        try {
            content.add(buildResorucesOverviewPanel());
            content.add(new ChoreographyVMDirectoryPanel(
                    "virtualMachinesList", pageRef, enactmentEngine.getKey(), choreography.getChoreographyId()));    
            content.add(buildActionsPanel());
            content.setOutputMarkupId(true);

            message = new WebMarkupContainer("message") {

                private static final long serialVersionUID = 9169935715713553356L;

                @Override
                public boolean isVisible() {
                    return false;
                } 
            };
        } catch (RuntimeException e) {
            this.content = new WebMarkupContainer("eeDetails") {

                private static final long serialVersionUID = 9169935715713553356L;

                @Override
                public boolean isVisible() {
                    return false;
                }
            };
            this.content.setVisible(true);
            message = new WebMarkupContainer("message");
        }

        add(new Label("eeName", enactmentEngine.getName()));
        add(content);
        add(message);
        add(utilityModal);
        setOutputMarkupId(true);
    }

    private ChoreographyActionsPanel buildActionsPanel() {
        String currentChoreographyStatus = choreographyRestClient.getChoreography(choreography.getKey()).getStatus();
        ChoreographyActionsPanel actionsPanel = new ChoreographyActionsPanel("actionsPanel");
        actionsPanel.addAction(new IndicatingAjaxLink<Void>("link") {

                    private static final long serialVersionUID = 5283601360187316340L;

                    @Override
                    public void onClick(final AjaxRequestTarget target) {
                        utilityModal.size(Modal.Size.Small);
                        utilityModal.addSubmitButton();
                        utilityModal.header(Model.of("Resize"));
                        utilityModal.setContent(new ResizeModalPanel(
                                utilityModal, choreography.getKey(), pageRef));
                        utilityModal.show(true);
                        target.add(utilityModal);
                    }
            }, ChoreographyActionsPanel.ChoreographyActionType.RESIZE);
        if (currentChoreographyStatus.equals("STARTED")) {
            actionsPanel.addAction(new IndicatingAjaxLink<Void>("link") {

                private static final long serialVersionUID = 3104631231085231035L;

                @Override
                public void onClick(final AjaxRequestTarget target) {
                    try {
                        choreographyRestClient.stopChoreography(choreography.getKey());
                        SyncopeConsoleSession.get().info(getString(Constants.OPERATION_SUCCEEDED));
                    } catch (SyncopeClientException e) {
                        LOG.error("While stopping choreography {}", choreography.getName(), e);
                        SyncopeConsoleSession.get().error(StringUtils.isBlank(e.getMessage())
                                ? e.getClass().getName() : e.getMessage());
                    }
                    ((BasePage) pageRef.getPage()).getNotificationPanel().refresh(target);
                    content.addOrReplace(buildActionsPanel());
                    target.add(content);
                }
            }, ChoreographyActionsPanel.ChoreographyActionType.STOP);
            actionsPanel.addAction(new IndicatingAjaxLink<Void>("link") {

                private static final long serialVersionUID = 3104631231085231035L;

                @Override
                public void onClick(final AjaxRequestTarget target) {
                    try {
                        choreographyRestClient.freezeChoreography(choreography.getKey());
                        SyncopeConsoleSession.get().info(getString(Constants.OPERATION_SUCCEEDED));
                    } catch (SyncopeClientException e) {
                        LOG.error("While freezing choreography {}", choreography.getName(), e);
                        SyncopeConsoleSession.get().error(StringUtils.isBlank(e.getMessage())
                                ? e.getClass().getName() : e.getMessage());
                    }
                    ((BasePage) pageRef.getPage()).getNotificationPanel().refresh(target);
                    content.addOrReplace(buildActionsPanel());
                    target.add(content);
                }
            }, ChoreographyActionsPanel.ChoreographyActionType.FREEZE);
        }

        if (currentChoreographyStatus.equals("STOPPED")) {
            actionsPanel.addAction(new IndicatingAjaxLink<Void>("link") {

                private static final long serialVersionUID = 3104631231085231035L;

                @Override
                public void onClick(final AjaxRequestTarget target) {
                    try {
                        choreographyRestClient.startChoreography(choreography.getKey());
                        SyncopeConsoleSession.get().info(getString(Constants.OPERATION_SUCCEEDED));
                    } catch (SyncopeClientException e) {
                        LOG.error("While starting choreography {}", choreography.getName(), e);
                        SyncopeConsoleSession.get().error(StringUtils.isBlank(e.getMessage())
                                ? e.getClass().getName() : e.getMessage());
                    }
                    ((BasePage) pageRef.getPage()).getNotificationPanel().refresh(target);
                    content.addOrReplace(buildActionsPanel());
                    target.add(content);
                }
            }, ChoreographyActionsPanel.ChoreographyActionType.START);
        }

        if (currentChoreographyStatus.equals("FROZEN")) {
            actionsPanel.addAction(new IndicatingAjaxLink<Void>("link") {

                private static final long serialVersionUID = 3104631231085231035L;

                @Override
                public void onClick(final AjaxRequestTarget target) {
                    try {
                        choreographyRestClient.unfreezeChoreography(choreography.getKey());
                        SyncopeConsoleSession.get().info(getString(Constants.OPERATION_SUCCEEDED));
                    } catch (SyncopeClientException e) {
                        LOG.error("While unfreezing choreography {}", choreography.getName(), e);
                        SyncopeConsoleSession.get().error(StringUtils.isBlank(e.getMessage())
                                ? e.getClass().getName() : e.getMessage());
                    }
                    ((BasePage) pageRef.getPage()).getNotificationPanel().refresh(target);
                    content.addOrReplace(buildActionsPanel());
                    target.add(content);
                }
            }, ChoreographyActionsPanel.ChoreographyActionType.UNFREEZE);
        }
        return actionsPanel;
    }

    private RepeatingView buildResorucesOverviewPanel() {

        EnactmentEngineStatusTO ee = monitorRestClient.getChoreographyEEStatus(
                enactmentEngine.getKey(), choreography.getChoreographyId());

        double ramPercentage = (ee.getRamUsage() / Float.valueOf(ee.getRamTotal())) * 100;
        double storagePercentage = (ee.getStorageUsage() / Float.valueOf(ee.getStorageTotal())) * 100;

        RepeatingView resourcesOverview = new RepeatingView("resourceWidget");
        resourcesOverview.add(new ResourceWidget(
                resourcesOverview.newChildId(),
                "bg-yellow", ee.getVirtualMachinesCount(), "Virtual Machines", "fa fa-cube"));
        resourcesOverview.add(new ResourceWidget(
                resourcesOverview.newChildId(), "bg-red", "CPU utilization", ee.getCpuUsageRatio(), "fa fa-cogs"));
        resourcesOverview.add(new ResourceWidget(
                resourcesOverview.newChildId(), "bg-green", ee.getRamUsage(), ee.getRamTotal(),
                "RAM utilization", ramPercentage, "MB", ""));
        resourcesOverview.add(new ResourceWidget(
                resourcesOverview.newChildId(), "bg-aqua", ee.getStorageUsage(), ee.getStorageTotal(),
                "Storage occupancy", storagePercentage, "GB", "fa fa-database"));

        return resourcesOverview;
    }
}
