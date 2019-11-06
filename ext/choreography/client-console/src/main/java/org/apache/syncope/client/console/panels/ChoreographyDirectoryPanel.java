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
package org.apache.syncope.client.console.panels;

import de.agilecoders.wicket.core.markup.html.bootstrap.dialog.Modal;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.apache.syncope.client.console.SyncopeConsoleSession;
import org.apache.syncope.client.console.commons.AnyDataProvider;
import org.apache.syncope.client.console.commons.ChoreographyDataProvider;
import org.apache.syncope.client.console.commons.Constants;
import org.apache.syncope.client.console.pages.BasePage;
import org.apache.syncope.client.console.pages.ChoreographyPage;
import org.apache.syncope.client.console.pages.ChoreographyDetailPage;
import org.apache.syncope.client.console.rest.AnyTypeClassRestClient;
import org.apache.syncope.client.console.rest.AnyTypeRestClient;
import org.apache.syncope.client.console.rest.ChoreographyRestClient;
import org.apache.syncope.client.console.wicket.extensions.markup.html.repeater.data.table.AttrColumn;
import org.apache.syncope.common.lib.SyncopeClientException;
import org.apache.syncope.common.lib.to.GroupTO;
import org.apache.syncope.client.console.wizards.WizardMgtPanel;
import org.apache.syncope.client.console.wizards.any.AnyWrapper;
import org.apache.syncope.common.lib.types.AnyTypeKind;
import org.apache.syncope.common.lib.types.SchemaType;
import org.apache.wicket.PageReference;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.ajax.markup.html.IndicatingAjaxLink;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.extensions.markup.html.repeater.data.table.AbstractColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.springframework.util.ReflectionUtils;

public class ChoreographyDirectoryPanel extends GroupDirectoryPanel {

    private static final long serialVersionUID = 3727444742501082182L;

    private final ChoreographyRestClient choreographyRestClient; 

    public ChoreographyDirectoryPanel(final String id, final PageReference pageRef) {
        super(id, new Builder(AnyTypeKind.GROUP.name(), pageRef), false);

        choreographyRestClient = new ChoreographyRestClient();

        utilityModal.addSubmitButton();
        utilityModal.size(Modal.Size.Medium);

        utilityModal.setWindowClosedCallback(new ModalWindow.WindowClosedCallback() {

            private static final long serialVersionUID = 8804221891699487139L;

            @Override
            public void onClose(final AjaxRequestTarget target) {
                target.add(container);
                utilityModal.show(false);
            }
        });
    }

    @Override
    protected AnyDataProvider<GroupTO> dataProvider() {
        final ChoreographyDataProvider dp = new ChoreographyDataProvider(restClient, rows, filtered, realm, type);
        return dp.setFIQL(this.fiql);
    }

    @Override
    protected String paginatorRowsKey() {
        return ChoreographyPage.PREF_CHOREOGRAPHY_PAGINATOR_ROWS;
    }

    @Override
    protected List<IColumn<GroupTO, String>> getColumns() {
        final List<IColumn<GroupTO, String>> columns = new ArrayList<>();

        addPropertyColumn("key", ReflectionUtils.findField(GroupTO.class, "key"), columns);
        addPropertyColumn("name", ReflectionUtils.findField(GroupTO.class, "name"), columns);

        columns.add(new AttrColumn<>("description", SchemaType.PLAIN));

        columns.add(new AbstractColumn<GroupTO, String>(new ResourceModel("", "Enactment Engine")) {

            private static final long serialVersionUID = 2054811145491901166L;

            @Override
            public void populateItem(
                    final Item<ICellPopulator<GroupTO>> item, final String componentId, final IModel<GroupTO> model) {

                String eeName = StringUtils.EMPTY;
                try {
                    eeName = choreographyRestClient.getChoreographyEE(model.getObject().getKey()).getName();
                } catch (Exception e) {
                    eeName = "Not assigned";
                } finally {
                    Label label = new Label(componentId, eeName);
                    item.add(label);
                }
            }
        });
        
        columns.add(new AbstractColumn<GroupTO, String>(new ResourceModel("", "Synthesis Processor")) {

            private static final long serialVersionUID = 2054811145491901166L;

            @Override
            public void populateItem(
                    final Item<ICellPopulator<GroupTO>> item, final String componentId, final IModel<GroupTO> model) {

                String spName = StringUtils.EMPTY;
                try {
                    spName = choreographyRestClient.getChoreographySynthesisProcessor(
                            model.getObject().getKey()).getName();
                } catch (Exception e) {
                    spName = "Not assigned";
                } finally {
                    Label label = new Label(componentId, spName);
                    item.add(label);
                }
            }
        });

        columns.add(new AttrColumn<>("status", SchemaType.PLAIN));

        columns.add(new AbstractColumn<GroupTO, String>(new ResourceModel("actions", "")) {

            private static final long serialVersionUID = -3503023501954863131L;

            @Override
            public String getCssClass() {
                return "action";
            }

            @Override
            public void populateItem(
                    final Item<ICellPopulator<GroupTO>> item, final String componentId, final IModel<GroupTO> model) {

                String chorStatus = model.getObject().getPlainAttrMap().get("status").getValues().iterator().next();
                ChoreographyActionsPanel choreographyActionsPanel = new ChoreographyActionsPanel(componentId);

                if (chorStatus.equals("CREATED")) {
                    choreographyActionsPanel.addAction(new IndicatingAjaxLink<Void>("link") {

                        private static final long serialVersionUID = -7978723352517770644L;

                        @Override
                        public void onClick(final AjaxRequestTarget target) {
                            try {
                                utilityModal.setContent(new SelectEEModalPanel(
                                        utilityModal,
                                        model.getObject(),
                                        pageRef));
                                utilityModal.header(Model.of("Select Enactment Engine"));
                                utilityModal.show(true);
                                target.add(utilityModal);
                            } catch (SyncopeClientException e) {
                                LOG.error("While enacting choreography {}", model.getObject().getName(), e);
                                SyncopeConsoleSession.get().error(StringUtils.isBlank(e.getMessage())
                                        ? e.getClass().getName() : e.getMessage());
                            }
                            ((BasePage) pageRef.getPage()).getNotificationPanel().refresh(target);
                        }
                    }, ChoreographyActionsPanel.ChoreographyActionType.ENACT);
                }

                if (!chorStatus.equals("CREATED")
                        && !chorStatus.equals("PENDING UPDATE")
                        && !chorStatus.equals("PENDING ENACTMENT")) {
                    choreographyActionsPanel.addAction(new IndicatingAjaxLink<Void>("link") {

                        private static final long serialVersionUID = -7978723352517770644L;

                        @Override
                        public void onClick(final AjaxRequestTarget target) {
                            utilityModal.setContent(new ResizeModalPanel(
                                    utilityModal,
                                    model.getObject().getKey(),
                                    pageRef)
                            );
                            utilityModal.show(true);
                            target.add(utilityModal);
                        }
                    }, ChoreographyActionsPanel.ChoreographyActionType.RESIZE);
                }

                if (chorStatus.equals("STARTED")) {
                    choreographyActionsPanel.addAction(new IndicatingAjaxLink<Void>("link") {

                        private static final long serialVersionUID = 3104631231085231035L;

                        @Override
                        public void onClick(final AjaxRequestTarget target) {
                            try {
                                choreographyRestClient.stopChoreography(
                                        model.getObject().getKey());
                                SyncopeConsoleSession.get().info(getString(Constants.OPERATION_SUCCEEDED));
                                target.add(container);
                            } catch (SyncopeClientException e) {
                                LOG.error("While stopping choreography {}", model.getObject().getName(), e);
                                SyncopeConsoleSession.get().error(StringUtils.isBlank(e.getMessage())
                                        ? e.getClass().getName() : e.getMessage());
                            }
                            ((BasePage) pageRef.getPage()).getNotificationPanel().refresh(target);
                        }
                    }, ChoreographyActionsPanel.ChoreographyActionType.STOP);
                    choreographyActionsPanel.addAction(new IndicatingAjaxLink<Void>("link") {

                        private static final long serialVersionUID = 3104631231085231035L;

                        @Override
                        public void onClick(final AjaxRequestTarget target) {
                            try {
                                choreographyRestClient.freezeChoreography(
                                        model.getObject().getKey());
                                SyncopeConsoleSession.get().info(getString(Constants.OPERATION_SUCCEEDED));
                                target.add(container);
                            } catch (SyncopeClientException e) {
                                LOG.error("While freezing choreography {}", model.getObject().getName(), e);
                                SyncopeConsoleSession.get().error(StringUtils.isBlank(e.getMessage())
                                        ? e.getClass().getName() : e.getMessage());
                            }
                            ((BasePage) pageRef.getPage()).getNotificationPanel().refresh(target);
                        }
                    }, ChoreographyActionsPanel.ChoreographyActionType.FREEZE);
                }

                if (chorStatus.equals("STOPPED")) {
                    choreographyActionsPanel.addAction(new IndicatingAjaxLink<Void>("link") {

                        private static final long serialVersionUID = 3104631231085231035L;

                        @Override
                        public void onClick(final AjaxRequestTarget target) {
                            try {
                                choreographyRestClient.startChoreography(
                                        model.getObject().getKey());
                                SyncopeConsoleSession.get().info(getString(Constants.OPERATION_SUCCEEDED));
                                target.add(container);
                            } catch (SyncopeClientException e) {
                                LOG.error("While starting choreography {}", model.getObject().getName(), e);
                                SyncopeConsoleSession.get().error(StringUtils.isBlank(e.getMessage())
                                        ? e.getClass().getName() : e.getMessage());
                            }
                            ((BasePage) pageRef.getPage()).getNotificationPanel().refresh(target);
                        }
                    }, ChoreographyActionsPanel.ChoreographyActionType.START);
                }

                if (chorStatus.equals("FROZEN")) {
                    choreographyActionsPanel.addAction(new IndicatingAjaxLink<Void>("link") {

                        private static final long serialVersionUID = 3104631231085231035L;

                        @Override
                        public void onClick(final AjaxRequestTarget target) {
                            try {
                                choreographyRestClient.unfreezeChoreography(
                                        model.getObject().getKey());
                                SyncopeConsoleSession.get().info(getString(Constants.OPERATION_SUCCEEDED));
                                target.add(container);
                            } catch (SyncopeClientException e) {
                                LOG.error("While unfreezing choreography {}", model.getObject().getName(), e);
                                SyncopeConsoleSession.get().error(StringUtils.isBlank(e.getMessage())
                                        ? e.getClass().getName() : e.getMessage());
                            }
                            ((BasePage) pageRef.getPage()).getNotificationPanel().refresh(target);
                        }
                    }, ChoreographyActionsPanel.ChoreographyActionType.UNFREEZE);
                }

                if (!chorStatus.equals("PENDING DELETE")) {
                    choreographyActionsPanel.addAction(new IndicatingAjaxLink<Void>("link") {

                    private static final long serialVersionUID = 3104631231085231035L;

                        @Override
                        public void onClick(final AjaxRequestTarget target) {
                            PageParameters param = new PageParameters();
                            param.add("chor", model.getObject().getKey());
                            setResponsePage(ChoreographyDetailPage.class, param);
                        }
                    }, ChoreographyActionsPanel.ChoreographyActionType.SHOW);

                    choreographyActionsPanel.addAction(new IndicatingAjaxLink<Void>("link") {
                        private static final long serialVersionUID = -7978723352517770644L;
                        @Override
                        public void onClick(final AjaxRequestTarget target) {
                            utilityModal.setContent(
                                    new EditChoreographyModalPanel(utilityModal, pageRef, model.getObject()));
                            utilityModal.header(Model.of("Edit Choreography Informations"));
                            utilityModal.show(true);
                            target.add(utilityModal);
                        }
                    }, ChoreographyActionsPanel.ChoreographyActionType.EDIT);

                    choreographyActionsPanel.addAction(new IndicatingAjaxLink<Void>("link") {

                        private static final long serialVersionUID = -7978723352517770644L;

                        @Override
                        public void onClick(final AjaxRequestTarget target) {
                            try {
                                choreographyRestClient.deleteChoreography(
                                        model.getObject().getKey());
                                SyncopeConsoleSession.get().info(getString(Constants.OPERATION_SUCCEEDED));
                                target.add(container);
                            } catch (SyncopeClientException e) {
                                LOG.error("While deleting object {}", model.getObject().getName(), e);
                                SyncopeConsoleSession.get().error(StringUtils.isBlank(e.getMessage())
                                        ? e.getClass().getName() : e.getMessage());
                            }
                            ((BasePage) pageRef.getPage()).getNotificationPanel().refresh(target);
                        }
                    }, ChoreographyActionsPanel.ChoreographyActionType.DELETE);
                }
                item.add(choreographyActionsPanel);
            }
        });

        return columns;
    }

    public static class Builder extends GroupDirectoryPanel.Builder {

        private static final long serialVersionUID = 8769126634538601689L;

        private final AnyTypeRestClient anyTypeRestClient = new AnyTypeRestClient();

        private final AnyTypeClassRestClient anyTypeClassRestClient = new AnyTypeClassRestClient();

        public Builder(final String type, final PageReference pageRef) {
            super(new ArrayList<>(), type, pageRef);

            getAnyTypeClassTOs().addAll(
                    anyTypeClassRestClient.list(anyTypeRestClient.read(AnyTypeKind.GROUP.name()).getClasses()));

            setRealm("/");
            setFiltered(true);

            setShowResultPage(true);
        }

        @Override
        protected WizardMgtPanel<AnyWrapper<GroupTO>> newInstance(final String id, final boolean wizardInModal) {
            return new ChoreographyDirectoryPanel(id, pageRef);
        }
    }
}
