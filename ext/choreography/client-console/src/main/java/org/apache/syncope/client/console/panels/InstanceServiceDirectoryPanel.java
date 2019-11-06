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
import eu.chorevolution.idm.common.to.ServiceTO;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import org.apache.syncope.client.console.commons.DirectoryDataProvider;
import org.apache.syncope.client.console.pages.InstanceStatsPage;
import org.apache.syncope.client.console.pages.ServiceDetailPage;
import org.apache.syncope.client.console.panels.InstanceServiceDirectoryPanel.ServiceProvider;
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

public class InstanceServiceDirectoryPanel extends DirectoryPanel<
        ServiceTO, ServiceTO, ServiceProvider, MonitorRestClient> {

    private static final long serialVersionUID = 3727444742501082182L;

    private String instanceId;

    public InstanceServiceDirectoryPanel(final String id, final PageReference pageRef, final String instanceId) {

        super(id, new DirectoryPanel.Builder<ServiceTO, ServiceTO, MonitorRestClient>(
                new MonitorRestClient(), pageRef) {

            private static final long serialVersionUID = 8769126634538601689L;

            @Override
            protected WizardMgtPanel<ServiceTO> newInstance(final String id, final boolean wizardInModal) {
                return new InstanceServiceDirectoryPanel(id, this);
            }
        }.disableCheckBoxes());

        this.instanceId = instanceId;

        setFooterVisibility(true);
        modal.addSubmitButton();
        modal.size(Modal.Size.Large);
        initResultTable();
    }

    private InstanceServiceDirectoryPanel(
            final String id,
            final DirectoryPanel.Builder<ServiceTO, ServiceTO, MonitorRestClient> builder) {

        super(id, builder);
    }

    @Override
    protected ServiceProvider dataProvider() {
        return new ServiceProvider(rows);
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
    protected List<IColumn<ServiceTO, String>> getColumns() {
        final List<IColumn<ServiceTO, String>> columns = new ArrayList<>();
        columns.add(new PropertyColumn<>(new ResourceModel("name", "Name"), "Name", "name"));
        columns.add(new PropertyColumn<>(new ResourceModel("executedOperations", "# Executed Operations"),
                "executedOperations"));
        columns.add(new PropertyColumn<>(new ResourceModel("executionTime", "Overall Execution Time (ms)"),
                "executionTime"));
        columns.add(new AbstractColumn<ServiceTO, String>(new ResourceModel("actions", "")) {

            private static final long serialVersionUID = -3503023501954863131L;

            @Override
            public String getCssClass() {
                return "action";
            }

            @Override
            public void populateItem(final Item<ICellPopulator<ServiceTO>> item, final String componentId,
                    final IModel<ServiceTO> model) {
                ChoreographyActionsPanel choreographyActionsPanel = new ChoreographyActionsPanel(componentId);
                choreographyActionsPanel.addAction(new IndicatingAjaxLink<Void>("link") {

                    private static final long serialVersionUID = 1467240526305723425L;

                    @Override
                    public void onClick(final AjaxRequestTarget target) {
                        PageParameters param = new PageParameters();
                        param.add("instance", instanceId);
                        param.add("service", model.getObject().getName());
                        setResponsePage(ServiceDetailPage.class, param);
                    }
                }, ChoreographyActionsPanel.ChoreographyActionType.SHOW);
                item.add(choreographyActionsPanel);
            }
        });

        return columns;

    }

    protected final class ServiceComparator implements Comparator<ServiceTO>, Serializable {

        private static final long serialVersionUID = -1842505741294215561L;

        @Override
        public int compare(final ServiceTO o1, final ServiceTO o2) {
            return o1.getName().compareTo(o2.getName());
        }
    }

    protected final class ServiceProvider extends DirectoryDataProvider<ServiceTO> {

        private static final long serialVersionUID = -185944053385660794L;

        private final Comparator<ServiceTO> comparator = new ServiceComparator();

        private ServiceProvider(final int paginatorRows) {
            super(paginatorRows);
        }

        @Override
        public Iterator<ServiceTO> iterator(final long first, final long count) {
            List<ServiceTO> list = restClient.instanceServiceList(instanceId);
            Collections.sort(list, comparator);
            return list.subList((int) first, (int) first + (int) count).iterator();
        }

        @Override
        public long size() {
            return restClient.instanceServiceList(instanceId).size();
        }

        @Override
        public IModel<ServiceTO> model(final ServiceTO object) {
            return new CompoundPropertyModel<>(object);
        }
    }
}
