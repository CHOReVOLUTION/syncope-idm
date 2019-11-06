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
import eu.chorevolution.idm.common.to.AVGCoordinationDelegateTO;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import org.apache.syncope.client.console.commons.DirectoryDataProvider;
import org.apache.syncope.client.console.pages.AVGCDDetailPage;
import org.apache.syncope.client.console.pages.InstanceStatsPage;
import org.apache.syncope.client.console.panels.AVGCDDirectoryPanel.CDProvider;
import org.apache.syncope.client.console.rest.MonitorRestClient;
import org.apache.syncope.client.console.wicket.markup.html.form.ActionLink;
import org.apache.syncope.client.console.wizards.WizardMgtPanel;
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

public class AVGCDDirectoryPanel extends DirectoryPanel<
        AVGCoordinationDelegateTO, AVGCoordinationDelegateTO, CDProvider, MonitorRestClient> {

    private static final long serialVersionUID = 3727444742501082182L;

    private String choreographyId;

    public AVGCDDirectoryPanel(final String id, final PageReference pageRef, final String choreographyId) {

        super(id,
                new DirectoryPanel.Builder<
                AVGCoordinationDelegateTO, AVGCoordinationDelegateTO, MonitorRestClient>(
                        new MonitorRestClient(), pageRef) {

            private static final long serialVersionUID = 8769126634538601689L;

            @Override
            protected WizardMgtPanel<AVGCoordinationDelegateTO> newInstance(
                    final String id, final boolean wizardInModal) {
                return new AVGCDDirectoryPanel(id, this);
            }
        }.disableCheckBoxes());

        this.choreographyId = choreographyId;

        setFooterVisibility(true);
        modal.addSubmitButton();
        modal.size(Modal.Size.Large);
        initResultTable();
    }

    private AVGCDDirectoryPanel(final String id,
            final DirectoryPanel.Builder<
            AVGCoordinationDelegateTO, AVGCoordinationDelegateTO, MonitorRestClient> builder) {

        super(id, builder);
    }

    @Override
    protected CDProvider dataProvider() {
        return new CDProvider(rows);
    }

    @Override
    protected String paginatorRowsKey() {
        return InstanceStatsPage.PREF_STATS_PAGINATOR_ROWS;
    }

    @Override
    protected Collection<ActionLink.ActionType> getBulkActions() {
        return Collections.<ActionLink.ActionType>emptyList();
    }

    @Override
    protected List<IColumn<AVGCoordinationDelegateTO, String>> getColumns() {
        final List<IColumn<AVGCoordinationDelegateTO, String>> columns = new ArrayList<>();
        columns.add(new PropertyColumn<>(new ResourceModel("name", "Name"), "Name", "name"));
        columns.add(new PropertyColumn<>(new ResourceModel("minExecutedOperations", "# Min Executed Operations"),
                "minExecutedOperations"));
        columns.add(new PropertyColumn<>(new ResourceModel("maxExecutedOperations", "# Max Executed Operations"),
                "maxExecutedOperations"));
        columns.add(new PropertyColumn<>(new ResourceModel("minCoordinationMessages", "# Min Coordination Messages"),
                "minCoordinationMessages"));
        columns.add(new PropertyColumn<>(new ResourceModel("maxCoordinationMessages", "# Max Coordination Messages"),
                "maxCoordinationMessages"));
        columns.add(new PropertyColumn<>(new ResourceModel("coordinationAlgorithmReasoningTime",
                "Coordination Logic Execution Time (ms)"), "coordinationAlgorithmReasoningTime"));
        columns.add(new PropertyColumn<>(new ResourceModel("networkOverheadForCoordinationMessagesExchangeTime",
                "Network Coordination Overhead Time (ms)"), "networkOverheadForCoordinationMessagesExchangeTime"));
        columns.add(new PropertyColumn<>(new ResourceModel("waitTimeForSchedulingOperation",
                "Waiting Time For Forwarding Service Request (ms)"), "waitTimeForSchedulingOperation"));
        columns.add(new PropertyColumn<>(new ResourceModel("waitTimeForSynchronizing",
                "Waiting Time For Synchronizing With Other CDs (ms)"), "waitTimeForSynchronizing"));
        columns.add(new PropertyColumn<>(new ResourceModel("serviceExecutionTime",
                "Service Execution Time (ms)"), "serviceExecutionTime"));
        columns.add(new PropertyColumn<>(new ResourceModel("networkOverheadForBusinessMessagesExchangeTime",
                "Network Business Overhead (ms)"), "networkOverheadForBusinessMessagesExchangeTime"));
        columns.add(new AbstractColumn<AVGCoordinationDelegateTO, String>(new ResourceModel("actions", "")) {

            private static final long serialVersionUID = -3503023501954863131L;

            @Override
            public String getCssClass() {
                return "action";
            }

            @Override
            public void populateItem(final Item<ICellPopulator<AVGCoordinationDelegateTO>> item,
                    final String componentId,
                    final IModel<AVGCoordinationDelegateTO> model) {
                ChoreographyActionsPanel choreographyActionsPanel = new ChoreographyActionsPanel(componentId);
                choreographyActionsPanel.addAction(new IndicatingAjaxLink<Void>("link") {

                    private static final long serialVersionUID = 1467240526305723425L;

                    @Override
                    public void onClick(final AjaxRequestTarget target) {
                        PageParameters param = new PageParameters();
                        param.add("chor", choreographyId);
                        param.add("cd", model.getObject().getName());
                        setResponsePage(AVGCDDetailPage.class, param);
                    }
                }, ChoreographyActionsPanel.ChoreographyActionType.SHOW);
                item.add(choreographyActionsPanel);
            }
        });

        return columns;

    }

    protected final class CDComparator implements Comparator<AVGCoordinationDelegateTO>, Serializable {

        private static final long serialVersionUID = -5323560030352040506L;

        @Override
        public int compare(final AVGCoordinationDelegateTO o1, final AVGCoordinationDelegateTO o2) {
            return o1.getName().compareTo(o2.getName());
        }
    }

    protected final class CDProvider extends DirectoryDataProvider<AVGCoordinationDelegateTO> {

        private static final long serialVersionUID = -185944053385660794L;

        private final Comparator<AVGCoordinationDelegateTO> comparator = new CDComparator();

        private CDProvider(final int paginatorRows) {
            super(paginatorRows);
        }

        @Override
        public Iterator<AVGCoordinationDelegateTO> iterator(final long first, final long count) {
            List<AVGCoordinationDelegateTO> list = restClient.averageCdList(choreographyId);
            Collections.sort(list, comparator);
            return list.subList((int) first, (int) first + (int) count).iterator();
        }

        @Override
        public long size() {
            return restClient.averageCdList(choreographyId).size();
        }

        @Override
        public IModel<AVGCoordinationDelegateTO> model(final AVGCoordinationDelegateTO object) {
            return new CompoundPropertyModel<>(object);
        }
    }
}
