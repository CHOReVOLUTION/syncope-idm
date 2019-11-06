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
import eu.chorevolution.idm.common.to.AVGOperationDataTO;
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
import org.apache.syncope.client.console.panels.AVGOperationDirectoryPanel.AVGOperationProvider;
import org.apache.syncope.client.console.rest.MonitorRestClient;
import org.apache.syncope.client.console.wicket.markup.html.form.ActionLink;
import org.apache.syncope.client.console.wizards.WizardMgtPanel;
import org.apache.wicket.PageReference;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;

public class AVGOperationDirectoryPanel extends DirectoryPanel<
        AVGOperationDataTO, AVGOperationDataTO, AVGOperationProvider, MonitorRestClient> {

    private static final long serialVersionUID = 3727444742501082182L;

    private String choreographyId;

    private String artifactName;

    private ArtifactType artifactType;

    public AVGOperationDirectoryPanel(final String id, final PageReference pageRef,
            final String choreographyId, final String artifactName, final ArtifactType artifactType) {

        super(id, new DirectoryPanel.Builder<AVGOperationDataTO, AVGOperationDataTO, MonitorRestClient>(
                new MonitorRestClient(), pageRef) {

            private static final long serialVersionUID = 4218621294354212530L;

            @Override
            protected WizardMgtPanel<AVGOperationDataTO> newInstance(final String id, final boolean wizardInModal) {
                return new AVGOperationDirectoryPanel(id, this);
            }
        }.disableCheckBoxes());

        this.choreographyId = choreographyId;
        this.artifactName = artifactName;
        this.artifactType = artifactType;

        setFooterVisibility(true);
        modal.addSubmitButton();
        modal.size(Modal.Size.Large);
        initResultTable();
    }

    private AVGOperationDirectoryPanel(
            final String id,
            final DirectoryPanel.Builder<AVGOperationDataTO, AVGOperationDataTO, MonitorRestClient> builder) {

        super(id, builder);
    }

    @Override
    protected AVGOperationProvider dataProvider() {
        return new AVGOperationProvider(rows);
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
    protected List<IColumn<AVGOperationDataTO, String>> getColumns() {
        final List<IColumn<AVGOperationDataTO, String>> columns = new ArrayList<>();
        columns.add(new PropertyColumn<>(new ResourceModel("name"), "Name", "name"));
        switch (artifactType) {
            case CD:
                columns.add(new PropertyColumn<>(new ResourceModel("minMessageNumber", "# Min Coordination Messages"),
                        "minMessageNumber"));
                columns.add(new PropertyColumn<>(new ResourceModel("maxMessageNumber", "# Max Coordination Messages"),
                        "maxMessageNumber"));
                columns.add(new PropertyColumn<>(new ResourceModel("coordinationAlgorithmReasoningTime",
                        "Coordination Logic Execution Time (ms)"), "coordinationAlgorithmReasoningTime"));
                columns.add(new PropertyColumn<>(new ResourceModel(
                        "networkOverheadForCoordinationMessagesExchangeTime",
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
            // no operation
        }
        return columns;

    }

    protected final class AVGOperationComparator implements Comparator<AVGOperationDataTO>, Serializable {

        private static final long serialVersionUID = 5830735410088350127L;

        @Override
        public int compare(final AVGOperationDataTO o1, final AVGOperationDataTO o2) {
            return o1.getName().compareTo(o2.getName());
        }
    }

    protected final class AVGOperationProvider extends DirectoryDataProvider<AVGOperationDataTO> {

        private static final long serialVersionUID = -185944053385660794L;

        private final Comparator<AVGOperationDataTO> comparator = new AVGOperationComparator();

        private AVGOperationProvider(final int paginatorRows) {
            super(paginatorRows);
        }

        @Override
        public Iterator<AVGOperationDataTO> iterator(final long first, final long count) {
            List<AVGOperationDataTO> list = restClient.averageOperationList(choreographyId, artifactName);
            Collections.sort(list, comparator);
            return list.subList((int) first, (int) first + (int) count).iterator();
        }

        @Override
        public long size() {
            return restClient.averageOperationList(choreographyId, artifactName).size();
        }

        @Override
        public IModel<AVGOperationDataTO> model(final AVGOperationDataTO object) {
            return new CompoundPropertyModel<>(object);
        }
    }
}
