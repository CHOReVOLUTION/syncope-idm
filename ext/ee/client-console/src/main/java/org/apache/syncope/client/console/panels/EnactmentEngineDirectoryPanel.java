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
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.apache.syncope.client.console.SyncopeConsoleSession;
import org.apache.syncope.client.console.commons.AnyDataProvider;
import org.apache.syncope.client.console.commons.Constants;
import org.apache.syncope.client.console.commons.EnactmentEngineDataProvider;
import org.apache.syncope.client.console.pages.BasePage;
import org.apache.syncope.client.console.pages.EnactmentEngineDetailPage;
import org.apache.syncope.client.console.pages.EnactmentEnginePage;
import static org.apache.syncope.client.console.panels.DirectoryPanel.LOG;
import org.apache.syncope.client.console.rest.EnactmentEngineRestClient;
import org.apache.syncope.client.console.wicket.markup.html.bootstrap.dialog.BaseModal;
import org.apache.syncope.common.lib.SyncopeClientException;
import org.apache.syncope.common.lib.search.AnyObjectFiqlSearchConditionBuilder;
import org.apache.syncope.common.lib.to.AnyObjectTO;
import org.apache.syncope.common.lib.types.AnyTypeKind;
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

public class EnactmentEngineDirectoryPanel extends AnyObjectDirectoryPanel {

    private static final long serialVersionUID = 7303610381539502741L;

    private final BaseModal<Serializable> modal = new BaseModal<>("outer");

    private final EnactmentEngineRestClient enactmentEngineRestClient;

    public EnactmentEngineDirectoryPanel(final String id, final PageReference pageRef) {
        super(id, new Builder(AnyTypeKind.ANY_OBJECT.name(), pageRef), false);

        this.enactmentEngineRestClient = new EnactmentEngineRestClient();

        modal.addSubmitButton();
        modal.size(Modal.Size.Medium);
        addOuterObject(modal);

        modal.setWindowClosedCallback(new ModalWindow.WindowClosedCallback() {

            private static final long serialVersionUID = 8804221891699487139L;

            @Override
            public void onClose(final AjaxRequestTarget target) {
                target.add(container);
                modal.show(false);
            }
        });
    }

    @Override
    protected String paginatorRowsKey() {
        return EnactmentEnginePage.PREF_EE_PAGINATOR_ROWS;
    }

    @Override
    protected AnyDataProvider<AnyObjectTO> dataProvider() {
        final EnactmentEngineDataProvider dp = new EnactmentEngineDataProvider(restClient, rows, filtered, realm, type);
        return dp.setFIQL(new AnyObjectFiqlSearchConditionBuilder("ENACTMENT ENGINE").query());
    }

    @Override
    protected List<IColumn<AnyObjectTO, String>> getColumns() {
        final List<IColumn<AnyObjectTO, String>> columns = new ArrayList<>();

        addPropertyColumn("key", ReflectionUtils.findField(AnyObjectTO.class, "key"), columns);
        addPropertyColumn("name", ReflectionUtils.findField(AnyObjectTO.class, "name"), columns);

        columns.add(new AbstractColumn<AnyObjectTO, String>(new ResourceModel("", "Base URL")) {

            private static final long serialVersionUID = 2054811145491901166L;

            @Override
            public void populateItem(
                    final Item<ICellPopulator<AnyObjectTO>> item,
                    final String componentId,
                    final IModel<AnyObjectTO> model) {
                Label label = new Label(
                        componentId, model.getObject().getPlainAttrMap().get("enactmentEngineBaseURL")
                                .getValues().iterator().next());
                item.add(label);
            }
        });

        columns.add(new AbstractColumn<AnyObjectTO, String>(new ResourceModel("actions", "")) {

            private static final long serialVersionUID = -5137819175810948915L;

            @Override
            public String getCssClass() {
                return "action";
            }

            @Override
            public void populateItem(
                    final Item<ICellPopulator<AnyObjectTO>> item,
                    final String componentId,
                    final IModel<AnyObjectTO> model) {

                ChoreographyActionsPanel choreographyActionsPanel = new ChoreographyActionsPanel(componentId);

                choreographyActionsPanel.addAction(new IndicatingAjaxLink<Void>("link") {

                    private static final long serialVersionUID = 9203736181047160675L;

                    @Override
                    public void onClick(final AjaxRequestTarget target) {
                        PageParameters param = new PageParameters();
                        param.add("ee", model.getObject().getKey());
                        setResponsePage(EnactmentEngineDetailPage.class, param);
                    }
                }, ChoreographyActionsPanel.ChoreographyActionType.SHOW);
                choreographyActionsPanel.addAction(new IndicatingAjaxLink<Void>("link") {

                    private static final long serialVersionUID = 9203736181047160675L;

                    @Override
                    public void onClick(final AjaxRequestTarget target) {
                        modal.header(Model.of("Edit Enactment Engine"));
                        modal.setContent(new EditEnactmentEngineModalPanel(
                                modal, pageRef, model.getObject()));
                        modal.show(true);
                        target.add(modal);
                    }
                }, ChoreographyActionsPanel.ChoreographyActionType.EDIT);
                choreographyActionsPanel.addAction(new IndicatingAjaxLink<Void>("link") {

                    private static final long serialVersionUID = 7293649026463920641L;

                    @Override
                    public void onClick(final AjaxRequestTarget target) {
                        try {
                                enactmentEngineRestClient.delete(model.getObject().getKey());
                                SyncopeConsoleSession.get().info(getString(Constants.OPERATION_SUCCEEDED));
                                target.add(container);
                            } catch (SyncopeClientException e) {
                                LOG.error("While deleting object {}", model.getObject().getKey(), e);
                                SyncopeConsoleSession.get().error(StringUtils.isBlank(e.getMessage())
                                        ? e.getClass().getName() : e.getMessage());
                            }
                            ((BasePage) pageRef.getPage()).getNotificationPanel().refresh(target);
                    }
                }, ChoreographyActionsPanel.ChoreographyActionType.DELETE);
                item.add(choreographyActionsPanel);
            }
        });
        return columns;
    }

    public static class Builder extends AnyObjectDirectoryPanel.Builder {

        private static final long serialVersionUID = 1903764789236792302L;

        public Builder(final String type, final PageReference pageRef) {
            super(new ArrayList<>(), type, pageRef);
            setFiltered(true);
            setShowResultPage(true);
        }
    }
}
