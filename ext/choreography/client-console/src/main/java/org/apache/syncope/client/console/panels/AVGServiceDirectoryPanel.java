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
import eu.chorevolution.idm.common.to.AVGServiceTO;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import org.apache.syncope.client.console.commons.DirectoryDataProvider;
import org.apache.syncope.client.console.pages.InstanceStatsPage;
import org.apache.syncope.client.console.pages.AVGServiceDetailPage;
import org.apache.syncope.client.console.panels.AVGServiceDirectoryPanel.AVGServiceProvider;
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

public class AVGServiceDirectoryPanel extends DirectoryPanel<
        AVGServiceTO, AVGServiceTO, AVGServiceProvider, MonitorRestClient> {

    private static final long serialVersionUID = 3727444742501082182L;

    private String choreographyId;

    public AVGServiceDirectoryPanel(final String id, final PageReference pageRef, final String choreographyId) {

        super(id, new DirectoryPanel.Builder<AVGServiceTO, AVGServiceTO, MonitorRestClient>(
                new MonitorRestClient(), pageRef) {

            private static final long serialVersionUID = 8769126634538601689L;

            @Override
            protected WizardMgtPanel<AVGServiceTO> newInstance(final String id, final boolean wizardInModal) {
                return new AVGServiceDirectoryPanel(id, this);
            }
        }.disableCheckBoxes());

        this.choreographyId = choreographyId;

        setFooterVisibility(true);
        modal.addSubmitButton();
        modal.size(Modal.Size.Large);
        initResultTable();
    }

    private AVGServiceDirectoryPanel(
            final String id,
            final DirectoryPanel.Builder<AVGServiceTO, AVGServiceTO, MonitorRestClient> builder) {

        super(id, builder);
    }

    @Override
    protected AVGServiceProvider dataProvider() {
        return new AVGServiceProvider(rows);
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
    protected List<IColumn<AVGServiceTO, String>> getColumns() {
        final List<IColumn<AVGServiceTO, String>> columns = new ArrayList<>();
        columns.add(new PropertyColumn<>(new ResourceModel("name", "Name"), "Name", "name"));
        columns.add(new PropertyColumn<>(new ResourceModel("minExecutedOperations", "# Min Executed Operations"),
                "minExecutedOperations"));
        columns.add(new PropertyColumn<>(new ResourceModel("maxExecutedOperations", "# Max Executed Operations"),
                "maxExecutedOperations"));
        columns.add(new PropertyColumn<>(new ResourceModel("executionTime", "Average Overall Execution Time (ms)"),
                "executionTime"));
        columns.add(new AbstractColumn<AVGServiceTO, String>(new ResourceModel("actions", "")) {

            private static final long serialVersionUID = -3503023501954863131L;

            @Override
            public String getCssClass() {
                return "action";
            }

            @Override
            public void populateItem(final Item<ICellPopulator<AVGServiceTO>> item, final String componentId,
                    final IModel<AVGServiceTO> model) {
                /* ActionLinksPanel.Builder<Serializable> actionLinks = ActionLinksPanel.builder();
                 * actionLinks.setDisableIndicator(true);
                 * actionLinks.add(new ActionLink<Serializable>() {
                 *
                 * private static final long serialVersionUID = -3722207913631435501L;
                 *
                 * @Override
                 * public void onClick(final AjaxRequestTarget target, final Serializable ignore) {
                 * PageParameters param = new PageParameters();
                 * param.add("chor", choreographyId);
                 * param.add("service", model.getObject().getName());
                 * setResponsePage(AVGServiceDetailPage.class, param);
                 * }
                 * }, ActionLink.ActionType.SEARCH);
                 * item.add(actionLinks.build(componentId)); */
                ChoreographyActionsPanel choreographyActionsPanel = new ChoreographyActionsPanel(componentId);
                choreographyActionsPanel.addAction(new IndicatingAjaxLink<Void>("link") {

                    private static final long serialVersionUID = 1467240526305723425L;

                    @Override
                    public void onClick(final AjaxRequestTarget target) {
                        PageParameters param = new PageParameters();
                        param.add("chor", choreographyId);
                        param.add("service", model.getObject().getName());
                        setResponsePage(AVGServiceDetailPage.class, param);
                    }
                }, ChoreographyActionsPanel.ChoreographyActionType.SHOW);
                item.add(choreographyActionsPanel);
            }
        });

        return columns;

    }

    protected final class AVGServiceComparator implements Comparator<AVGServiceTO>, Serializable {

        private static final long serialVersionUID = 1195401098208482891L;

        @Override
        public int compare(final AVGServiceTO o1, final AVGServiceTO o2) {
            return o1.getName().compareTo(o2.getName());
        }
    }

    protected final class AVGServiceProvider extends DirectoryDataProvider<AVGServiceTO> {

        private static final long serialVersionUID = -185944053385660794L;

        private final Comparator<AVGServiceTO> comparator = new AVGServiceComparator();

        private AVGServiceProvider(final int paginatorRows) {
            super(paginatorRows);
        }

        @Override
        public Iterator<AVGServiceTO> iterator(final long first, final long count) {
            List<AVGServiceTO> list = restClient.averageServiceList(choreographyId);
            Collections.sort(list, comparator);
            return list.subList((int) first, (int) first + (int) count).iterator();
        }

        @Override
        public long size() {
            return restClient.averageServiceList(choreographyId).size();
        }

        @Override
        public IModel<AVGServiceTO> model(final AVGServiceTO object) {
            return new CompoundPropertyModel<>(object);
        }
    }
}
