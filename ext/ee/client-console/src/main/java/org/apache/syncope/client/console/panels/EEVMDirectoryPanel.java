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
import eu.chorevolution.idm.common.to.VirtualMachineInfoTO;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import org.apache.syncope.client.console.commons.DirectoryDataProvider;
import org.apache.syncope.client.console.pages.ChoreographyDetailPage;
import org.apache.syncope.client.console.panels.EEVMDirectoryPanel.VirtualMachinesDataProvider;
import org.apache.syncope.client.console.rest.EnactmentEngineRestClient;
import org.apache.syncope.client.console.wicket.markup.html.bootstrap.dialog.BaseModal;
import org.apache.syncope.client.console.wicket.markup.html.form.ActionLink;
import org.apache.syncope.client.console.wizards.WizardMgtPanel;
import org.apache.wicket.PageReference;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.ajax.markup.html.IndicatingAjaxLink;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.extensions.markup.html.repeater.data.table.AbstractColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;

public class EEVMDirectoryPanel extends DirectoryPanel<
        VirtualMachineInfoTO, VirtualMachineInfoTO, VirtualMachinesDataProvider, EnactmentEngineRestClient> {

    private static final long serialVersionUID = 3727444742501082182L;

    private String enactmentEngineKey;

    private final BaseModal<Serializable> utilityModal = new BaseModal<>("outer");

    public EEVMDirectoryPanel(final String id, final PageReference pageRef,
            final String enactmentEngineKey) {

        super(id, new DirectoryPanel.Builder<VirtualMachineInfoTO, VirtualMachineInfoTO, EnactmentEngineRestClient>(
                new EnactmentEngineRestClient(), pageRef) {

            private static final long serialVersionUID = 4218621294354212530L;

            @Override
            protected WizardMgtPanel<VirtualMachineInfoTO> newInstance(final String id, final boolean wizardInModal) {
                return new EEVMDirectoryPanel(id, this);
            }
        }.disableCheckBoxes());

        this.enactmentEngineKey = enactmentEngineKey;

        setFooterVisibility(true);
        addOuterObject(utilityModal);
        utilityModal.addSubmitButton();
        utilityModal.size(Modal.Size.Small);
        initResultTable();
    }

    private EEVMDirectoryPanel(
            final String id,
            final DirectoryPanel.Builder<VirtualMachineInfoTO, VirtualMachineInfoTO,
                    EnactmentEngineRestClient> builder) {

        super(id, builder);
    }

    @Override
    protected VirtualMachinesDataProvider dataProvider() {
        return new VirtualMachinesDataProvider(rows);
    }

    @Override
    protected String paginatorRowsKey() {
        return ChoreographyDetailPage.PREF_SERVICE_PAGINATOR_ROWS;
    }

    @Override
    protected Collection<ActionLink.ActionType> getBulkActions() {
        return Collections.<ActionLink.ActionType>emptyList();
    }

    @Override
    protected List<IColumn<VirtualMachineInfoTO, String>> getColumns() {
        final List<IColumn<VirtualMachineInfoTO, String>> columns = new ArrayList<>();

        columns.add(new AbstractColumn<VirtualMachineInfoTO, String>(new ResourceModel("", "Type")) {
            private static final long serialVersionUID = -5860164518940274921L;

            @Override
            public void populateItem(final Item<ICellPopulator<VirtualMachineInfoTO>> item, final String componentId,
                    final IModel<VirtualMachineInfoTO> model) {
                if (model.getObject().getIsChoreography()) {
                    item.add(new Label(componentId, "Choreography"));
                } else {
                    item.add(new Label(componentId, "Load Balancer"));
                }
            }
        });

        columns.add(new PropertyColumn<>(new ResourceModel(
                "chorDisplay", "Choreography"), "Choreography", "chorDisplay"));
        columns.add(new PropertyColumn<>(new ResourceModel("sysOp", "OS"), "OS", "sysOp"));
        columns.add(new PropertyColumn<>(new ResourceModel("ip", "IP Address"), "IP Address", "ip"));
        columns.add(new PropertyColumn<>(new ResourceModel("hostname", "Hostname"), "Hostname", "hostname"));
        columns.add(new PropertyColumn<>(new ResourceModel("cpuCount", "CPU"), "CPU", "cpuCount"));
        columns.add(new PropertyColumn<>(new ResourceModel(
                "cpuUsageRatio", "CPU Utilization (%)"), "CPU Utilization (%)", "cpuUsageRatio"));
        columns.add(new PropertyColumn<>(new ResourceModel(
                "ramUsage", "Used Memory (MB)"), "Used Memory (MB)", "ramUsage"));
        columns.add(new PropertyColumn<>(new ResourceModel(
                "ramTotal", "Total Memory (MB)"), "Total Memory (MB)", "ramTotal"));
        columns.add(new PropertyColumn<>(new ResourceModel(
                "storageUsage", "Used Storage (GB)"), "Used Storage (GB)", "storageUsage"));
        columns.add(new PropertyColumn<>(new ResourceModel(
                "storageTotal", "Total Storage (GB)"), "Total Storage (GB)", "storageTotal"));

        columns.add(new AbstractColumn<VirtualMachineInfoTO, String>(new ResourceModel("", "")) {

            private static final long serialVersionUID = -1374917837616789356L;

            @Override
            public String getCssClass() {
                return "action";
            }

            @Override
            public void populateItem(final Item<ICellPopulator<VirtualMachineInfoTO>> item, final String componentId,
                    final IModel<VirtualMachineInfoTO> model) {
                ChoreographyActionsPanel choreographyActionsPanel = new ChoreographyActionsPanel(componentId);
                choreographyActionsPanel.addAction(new IndicatingAjaxLink<Void>("link") {

                        private static final long serialVersionUID = 3104631231085231035L;

                        @Override
                        public void onClick(final AjaxRequestTarget target) {

                        }
                    }, ChoreographyActionsPanel.ChoreographyActionType.SHOW);
                item.add(choreographyActionsPanel);
            }
        });

        return columns;

    }

    protected final class VirtualMachinesDataProvider extends DirectoryDataProvider<VirtualMachineInfoTO> {

        private static final long serialVersionUID = -4827492659202167329L;

        private final Comparator<VirtualMachineInfoTO> comparator;

        private VirtualMachinesDataProvider(final int paginatorRows) {
            super(paginatorRows);
            comparator = new VMDataComparator();
        }

        @Override
        public Iterator<VirtualMachineInfoTO> iterator(final long first, final long count) {
            List<VirtualMachineInfoTO> list = restClient.getEEVmList(enactmentEngineKey);
            Collections.sort(list, comparator);
            return list.subList((int) first, (int) first + (int) count).iterator();
        }

        @Override
        public long size() {
            return restClient.getEEVmList(enactmentEngineKey).size();
        }

        @Override
        public IModel<VirtualMachineInfoTO> model(final VirtualMachineInfoTO object) {
            return new CompoundPropertyModel<>(object);
        }
    }

    public static class VMDataComparator implements Comparator<VirtualMachineInfoTO>, Serializable {

        private static final long serialVersionUID = 7394234509595556376L;

        @Override
        public int compare(final VirtualMachineInfoTO o1, final VirtualMachineInfoTO o2) {
            return o1.getKey().compareTo(o2.getKey());
        }

    }
}
