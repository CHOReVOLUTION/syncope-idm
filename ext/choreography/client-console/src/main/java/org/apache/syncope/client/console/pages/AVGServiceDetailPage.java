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
package org.apache.syncope.client.console.pages;

import com.pingunaut.wicket.chartjs.chart.impl.Bar;
import com.pingunaut.wicket.chartjs.core.panel.BarChartPanel;
import eu.chorevolution.idm.common.to.AVGOperationDataTO;
import eu.chorevolution.idm.common.to.ChoreographyTO;
import eu.chorevolution.idm.common.types.ArtifactType;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import org.apache.syncope.client.console.BookmarkablePageLinkBuilder;
import org.apache.syncope.client.console.panels.AVGOperationDirectoryPanel;
import org.apache.syncope.client.console.panels.ChartsLegendPanel;
import org.apache.syncope.client.console.rest.MonitorRestClient;
import org.apache.syncope.client.console.widgets.StatsWidget;
import org.apache.syncope.client.console.widgets.LabeledBarDataSet;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;

public class AVGServiceDetailPage extends BaseExtPage {

    private static final long serialVersionUID = 3932245590233237182L;

    public static final String PREF_STATS_PAGINATOR_ROWS = "stats.paginator.rows";

    private final MonitorRestClient restClient;

    private final String choreographyId;

    private final String serviceName;

    private final ServiceStatisticsBundle bundle;

    public AVGServiceDetailPage(final PageParameters parameters) {
        super(parameters);

        restClient = new MonitorRestClient();

        choreographyId = parameters.get("chor").toString();
        serviceName = parameters.get("service").toString();

        ChoreographyTO choreography = restClient.getChoreography(choreographyId);

        body.add(new Label("header", getString("header_title") + " " + serviceName));

        List<AVGOperationDataTO> operations = restClient.averageOperationList(choreographyId, serviceName);
        bundle = new ServiceStatisticsBundle(operations);


        body.add(BookmarkablePageLinkBuilder.build("dashboard", "dashboardBr", Dashboard.class));
        body.add(BookmarkablePageLinkBuilder.build("choreographyBr", ChoreographyPage.class));

        PageParameters choreographyParams = new PageParameters();
        choreographyParams.add("chorId", choreography.getChoreographyId());
        body.add(new BookmarkablePageLink<>("choreographyDetailsBr", ChoreographyDetailPage.class, choreographyParams)
                        .add(new Label("choreographyTitle", choreography.getName())));

        body.add(new BookmarkablePageLink<>("averageInstanceBr", AVGInstanceStatsPage.class, choreographyParams));

        WebMarkupContainer content = new WebMarkupContainer("content");
        content.setOutputMarkupId(true);

        WebMarkupContainer charts = new WebMarkupContainer("charts");

        // Sleeptime, Overhead and Execution time for each operation
        charts.add(new StatsWidget("operationsChart") {

            @Override
            public void create() {

                Bar bar = new Bar();
                bar.getData().getLabels().addAll(bundle.labels);

                //Service Execution Time (Blue)
                LabeledBarDataSet serviceExecutionTimeDataSet = new LabeledBarDataSet(bundle.serviceExecutionTimes);
                serviceExecutionTimeDataSet.setFillColor("rgba(20,20,227,0.6)");
                serviceExecutionTimeDataSet.setStrokeColor("rgba(20,20,227,0.8)");
                serviceExecutionTimeDataSet.setLabel(getString("service_execution_time"));
                bar.getData().getDatasets().add(serviceExecutionTimeDataSet);

                bar.getOptions().setTooltipTemplate("<%= datasetLabel %>: <%= value %> ms");

                add(new BarChartPanel("chart", Model.of(bar)));
                add(new Label("title", getString("service_operation_execution_time_chart_title")));
                add(new Label("caption", ""));
            }
        });
        // Set Chart Legend
        ChartsLegendPanel chartsLegendPanel = new ChartsLegendPanel("chart_legend");
        charts.add(chartsLegendPanel.addLegendGroup(ChartsLegendPanel.ChartsLegendType.SERVICE_DETAIL));

        content.add(charts);

        content.add(new AVGOperationDirectoryPanel(
                "operations", getPageReference(), choreographyId, serviceName, ArtifactType.SERVICE));
        body.add(content);
    }

    private class ServiceStatisticsBundle implements Serializable {

        private static final long serialVersionUID = 3642028474262240172L;

        private List<String> labels;

        private List<Double> serviceExecutionTimes;

        ServiceStatisticsBundle(final List<AVGOperationDataTO> operations) {

            labels = new LinkedList<>();
            serviceExecutionTimes = new LinkedList<>();

            for (AVGOperationDataTO operation : operations) {
                labels.add(operation.getName());
                serviceExecutionTimes.add(operation.getServiceExecutionTime());
            }
        }
    }

}
