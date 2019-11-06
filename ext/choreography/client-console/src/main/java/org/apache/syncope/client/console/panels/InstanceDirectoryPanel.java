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
import eu.chorevolution.idm.common.to.ChoreographyInstanceTO;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Comparator;
import org.apache.commons.lang3.StringUtils;
import org.apache.syncope.client.console.SyncopeConsoleSession;
import org.apache.syncope.client.console.commons.Constants;
import org.apache.syncope.client.console.commons.DirectoryDataProvider;
import org.apache.syncope.client.console.pages.BasePage;
import org.apache.syncope.client.console.pages.ChoreographyDetailPage;
import org.apache.syncope.client.console.pages.InstanceStatsPage;
import org.apache.syncope.client.console.panels.InstanceDirectoryPanel.InstanceProvider;
import org.apache.syncope.client.console.rest.MonitorRestClient;
import org.apache.syncope.client.console.wicket.markup.html.form.ActionLink;
import org.apache.syncope.client.console.wizards.WizardMgtPanel;
import org.apache.syncope.common.lib.SyncopeClientException;
import org.apache.wicket.PageReference;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.ajax.markup.html.IndicatingAjaxLink;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.extensions.markup.html.repeater.data.table.AbstractColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

public class InstanceDirectoryPanel extends DirectoryPanel<
        ChoreographyInstanceTO, ChoreographyInstanceTO, InstanceProvider, MonitorRestClient> {

    private static final long serialVersionUID = 3727444742501082182L;

    private String choreographyId;

    public InstanceDirectoryPanel(final String id, final PageReference pageRef, final String choreographyId) {
        super(id, new Builder<ChoreographyInstanceTO, ChoreographyInstanceTO, MonitorRestClient>(
                new MonitorRestClient(), pageRef) {

            private static final long serialVersionUID = 8769126634538601689L;

            @Override
            protected WizardMgtPanel<ChoreographyInstanceTO> newInstance(final String id, final boolean wizardInModal) {
                return new InstanceDirectoryPanel(id, this);
            }
        }.disableCheckBoxes());

        this.choreographyId = choreographyId;

        setFooterVisibility(false);
        modal.addSubmitButton();
        modal.size(Modal.Size.Large);
        initResultTable();
    }

    private InstanceDirectoryPanel(
            final String id,
            final Builder<ChoreographyInstanceTO, ChoreographyInstanceTO, MonitorRestClient> builder) {

        super(id, builder);
    }

    @Override
    protected InstanceProvider dataProvider() {
        return new InstanceProvider(rows);
    }

    @Override
    protected String paginatorRowsKey() {
        return ChoreographyDetailPage.PREF_INSTANCE_PAGINATOR_ROWS;
    }

    @Override
    protected Collection<ActionLink.ActionType> getBulkActions() {
        return Collections.<ActionLink.ActionType>emptyList();
    }

    @Override
    protected List<IColumn<ChoreographyInstanceTO, String>> getColumns() {
        final List<IColumn<ChoreographyInstanceTO, String>> columns = new ArrayList<>();
        columns.add(new PropertyColumn<>(new ResourceModel("choreographyInstanceId", "Id"),
                "Id", "choreographyInstanceId"));
        columns.add(new PropertyColumn<>(new ResourceModel("instanceDescription", "Description"),
                "instanceDescription"));
        columns.add(new PropertyColumn<>(new ResourceModel("status", "Status"), "Status", "status"));
        columns.add(new PropertyColumn<>(new ResourceModel("executionTime", "Execution Time (ms)"),
                "Execution Time (ms)", "executionTime"));
        columns.add(new AbstractColumn<ChoreographyInstanceTO, String>(new ResourceModel("actions", "")) {

            private static final long serialVersionUID = -3503023501954863131L;

            @Override
            public String getCssClass() {
                return "action";
            }

            @Override
            public void populateItem(final Item<ICellPopulator<ChoreographyInstanceTO>> item, final String componentId,
                    final IModel<ChoreographyInstanceTO> model) {

                ChoreographyActionsPanel choreographyActionsPanel = new ChoreographyActionsPanel(componentId);

                choreographyActionsPanel.addAction(new IndicatingAjaxLink<Void>("link") {

                    private static final long serialVersionUID = 3104631231085231035L;

                    @Override
                    public void onClick(final AjaxRequestTarget target) {
                        PageParameters param = new PageParameters();
                        param.add("instance", model.getObject().getKey());
                        setResponsePage(InstanceStatsPage.class, param);
                    }
                }, ChoreographyActionsPanel.ChoreographyActionType.SHOW);
                choreographyActionsPanel.addAction(new IndicatingAjaxLink<Void>("link") {

                    private static final long serialVersionUID = -4012471301824517424L;

                    @Override
                    public void onClick(final AjaxRequestTarget target) {
                        try {
                            restClient.deleteInstance(model.getObject().getKey());
                            SyncopeConsoleSession.get().info(getString(Constants.OPERATION_SUCCEEDED));
                            target.add(container);
                        } catch (SyncopeClientException e) {
                            LOG.error("While deleting instance {}", model.getObject().getChoreographyInstanceId(), e);
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

    protected final class InstanceComparator implements Comparator<ChoreographyInstanceTO>, Serializable {

        private static final long serialVersionUID = -1959177332431794755L;

        @Override
        public int compare(final ChoreographyInstanceTO o1, final ChoreographyInstanceTO o2) {
            return o1.getChoreographyInstanceId().compareTo(o2.getChoreographyInstanceId());
        }
    }

    protected final class InstanceProvider extends DirectoryDataProvider<ChoreographyInstanceTO> {

        private static final long serialVersionUID = -185944053385660794L;

        private final Comparator<ChoreographyInstanceTO> comparator = new InstanceComparator();

        private InstanceProvider(final int paginatorRows) {
            super(paginatorRows);
        }

        @Override
        public Iterator<ChoreographyInstanceTO> iterator(final long first, final long count) {
            List<ChoreographyInstanceTO> list = restClient.instanceList(choreographyId);
            Collections.sort(list, comparator);
            return list.subList((int) first, (int) first + (int) count).iterator();
        }

        @Override
        public long size() {
            return restClient.instanceList(choreographyId).size();
        }

        @Override
        public IModel<ChoreographyInstanceTO> model(final ChoreographyInstanceTO object) {
            return new CompoundPropertyModel<>(object);
        }
    }
}
