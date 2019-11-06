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
import eu.chorevolution.idm.common.to.OperationDataTO;
import eu.chorevolution.idm.common.types.ArtifactType;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import org.apache.syncope.client.console.commons.DirectoryDataProvider;
import org.apache.syncope.client.console.pages.CDDetailPage;
import org.apache.syncope.client.console.panels.OperationDirectoryPanel.OperationProvider;
import org.apache.syncope.client.console.rest.MonitorRestClient;
import org.apache.syncope.client.console.wicket.markup.html.form.ActionLink;
import org.apache.syncope.client.console.wizards.WizardMgtPanel;
import org.apache.wicket.PageReference;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;

public class OperationDirectoryPanel extends DirectoryPanel<
        OperationDataTO, OperationDataTO, OperationProvider, MonitorRestClient> {

    private static final long serialVersionUID = 3727444742501082182L;

    private String instanceId;

    private String artifactName;

    private ArtifactType artifactType;

    public OperationDirectoryPanel(final String id, final PageReference pageRef,
            final String instanceId, final String artifactName, final ArtifactType artifactType) {

        super(id, new DirectoryPanel.Builder<OperationDataTO, OperationDataTO, MonitorRestClient>(
                new MonitorRestClient(), pageRef) {

            private static final long serialVersionUID = 4218621294354212530L;

            @Override
            protected WizardMgtPanel<OperationDataTO> newInstance(final String id, final boolean wizardInModal) {
                return new OperationDirectoryPanel(id, this);
            }
        }.disableCheckBoxes());

        this.instanceId = instanceId;
        this.artifactName = artifactName;
        this.artifactType = artifactType;

        setFooterVisibility(true);
        modal.addSubmitButton();
        modal.size(Modal.Size.Large);
        initResultTable();
    }

    private OperationDirectoryPanel(
            final String id,
            final DirectoryPanel.Builder<OperationDataTO, OperationDataTO, MonitorRestClient> builder) {

        super(id, builder);
    }

    @Override
    protected OperationProvider dataProvider() {
        return new OperationProvider(rows);
    }

    @Override
    protected String paginatorRowsKey() {
        return CDDetailPage.PREF_STATS_PAGINATOR_ROWS;
    }

    @Override
    protected Collection<ActionLink.ActionType> getBulkActions() {
        return Collections.<ActionLink.ActionType>emptyList();
    }

    @Override
    protected List<IColumn<OperationDataTO, String>> getColumns() {
        final List<IColumn<OperationDataTO, String>> columns = new ArrayList<>();
        columns.add(new PropertyColumn<>(new ResourceModel("name"), "Name", "name"));
        switch (artifactType) {
            case CD:
                columns.add(new PropertyColumn<>(new ResourceModel("messageNumber", "# Coordination Messages"),
                        "messageNumber"));
                columns.add(new PropertyColumn<>(new ResourceModel("coordinationAlgorithmReasoningTime",
                        "Coordination Logic Execution Time (ms)"), "coordinationAlgorithmReasoningTime"));
                columns.add(new PropertyColumn<>(new ResourceModel("networkOverheadForCoordinationMessagesExchangeTime",
                        "Network Coordination Overhead Time (ms)"),
                        "networkOverheadForCoordinationMessagesExchangeTime"));
                columns.add(new PropertyColumn<>(new ResourceModel("waitTimeForSchedulingOperation",
                        "Waiting Time For Forwarding Service Request (ms)"), "waitTimeForSchedulingOperation"));
                columns.add(new PropertyColumn<>(new ResourceModel("waitTimeForSynchronizing",
                        "Waiting Time For Synchronizing With Other CDs (ms)"), "waitTimeForSynchronizing"));
                columns.add(new PropertyColumn<>(new ResourceModel("serviceExecutionTime",
                        "Service Execution Time (ms)"), "serviceExecutionTime"));
                columns.add(new PropertyColumn<>(new ResourceModel("networkOverheadForBusinessMessagesExchangeTime",
                        "Network Business Overhead (ms)"), "networkOverheadForBusinessMessagesExchangeTime"));
                break;
            case SERVICE:
                columns.add(new PropertyColumn<>(new ResourceModel("serviceExecutionTime",
                        "Service Execution Time (ms)"), "serviceExecutionTime"));
                break;
            default:
            // do nothing
        }
        return columns;

    }

    protected final class OperationProvider extends DirectoryDataProvider<OperationDataTO> {

        private static final long serialVersionUID = -185944053385660794L;

        private final Comparator<OperationDataTO> comparator;

        private OperationProvider(final int paginatorRows) {
            super(paginatorRows);
            comparator = new OperationDataComparator();
        }

        @Override
        public Iterator<OperationDataTO> iterator(final long first, final long count) {
            List<OperationDataTO> list = restClient.operationList(instanceId, artifactName);
            Collections.sort(list, comparator);
            return list.subList((int) first, (int) first + (int) count).iterator();
        }

        @Override
        public long size() {
            return restClient.operationList(instanceId, artifactName).size();
        }

        @Override
        public IModel<OperationDataTO> model(final OperationDataTO object) {
            return new CompoundPropertyModel<>(object);
        }
    }

    public static class OperationDataComparator implements Comparator<OperationDataTO>, Serializable {

        private static final long serialVersionUID = 7394234509595556376L;

        @Override
        public int compare(final OperationDataTO o1, final OperationDataTO o2) {
            return o1.getName().compareTo(o2.getName());
        }

    }
}
