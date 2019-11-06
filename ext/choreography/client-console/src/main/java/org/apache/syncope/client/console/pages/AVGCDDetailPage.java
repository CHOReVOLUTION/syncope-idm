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
import com.pingunaut.wicket.chartjs.chart.impl.Doughnut;
import com.pingunaut.wicket.chartjs.core.panel.BarChartPanel;
import com.pingunaut.wicket.chartjs.core.panel.DoughnutChartPanel;
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
import org.apache.syncope.client.console.widgets.LabeledDoughnutChartData;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;

public class AVGCDDetailPage extends BaseExtPage {
    
    private static final long serialVersionUID = 3932245590233237182L;

    public static final String PREF_STATS_PAGINATOR_ROWS = "stats.paginator.rows";

    private final MonitorRestClient restClient;

    private final String choreographyId;

    private final String cdName;

    private final CDStatisticsBundle bundle;

    public AVGCDDetailPage(final PageParameters parameters) {
        super(parameters);

        restClient = new MonitorRestClient();

        choreographyId = parameters.get("chor").toString();
        cdName = parameters.get("cd").toString();

        body.add(new Label("header", getString("header_title") + " " + cdName));

        ChoreographyTO choreography = restClient.getChoreography(choreographyId);
        List<AVGOperationDataTO> operations
                = restClient.averageOperationList(choreography.getChoreographyId(), cdName);
        bundle = new CDStatisticsBundle(operations);

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
            private static final long serialVersionUID = -8150750732895436230L;

            @Override
            public void create() {

                Bar bar = new Bar();
                bar.getData().getLabels().addAll(bundle.labels);

                //Coordination Algorithm Reasoning Time (Light Red)
                LabeledBarDataSet algorithmReasoningDataSet =
                        new LabeledBarDataSet(bundle.coordinationAlgorithmReasoningTimes);
                algorithmReasoningDataSet.setFillColor("rgb(244,67,54)");
                algorithmReasoningDataSet.setStrokeColor("rgb(227,20,20)");
                algorithmReasoningDataSet.setLabel(getString("coordination_algorithm_reasoning"));
                bar.getData().getDatasets().add(algorithmReasoningDataSet);

                //Network Overhead For Coordination Messages (Dark Red)
                LabeledBarDataSet networkCoordinationOverheadDataSet =
                        new LabeledBarDataSet(bundle.networkOverheadForCoordinationMessagesExchangeTimes);
                networkCoordinationOverheadDataSet.setFillColor("rgb(198,40,40)");
                networkCoordinationOverheadDataSet.setStrokeColor("rgb(227,20,20)");
                networkCoordinationOverheadDataSet.setLabel(getString("network_coordination_overhead"));
                bar.getData().getDatasets().add(networkCoordinationOverheadDataSet);

                //Wait Times (Green)
                LabeledBarDataSet waitScheduleTimeDataSet =
                        new LabeledBarDataSet(bundle.waitTimesForSchedulingOperation);
                waitScheduleTimeDataSet.setFillColor("rgb(76,175,80)");
                waitScheduleTimeDataSet.setStrokeColor("rgb(20,227,20)");
                waitScheduleTimeDataSet.setLabel(getString("wait_scheduling_operation"));
                bar.getData().getDatasets().add(waitScheduleTimeDataSet);

                LabeledBarDataSet waitSyncrhoTimeDataSet = new LabeledBarDataSet(bundle.waitTimesForSynchronizing);
                waitSyncrhoTimeDataSet.setFillColor("rgb(46,125,50)");
                waitSyncrhoTimeDataSet.setStrokeColor("rgb(20,227,20)");
                waitSyncrhoTimeDataSet.setLabel(getString("wait_synchronizing"));
                bar.getData().getDatasets().add(waitSyncrhoTimeDataSet);

                //Service Execution Time (Blue)
                LabeledBarDataSet serviceExecutionTimeDataSet = new LabeledBarDataSet(bundle.serviceExecutionTimes);
                serviceExecutionTimeDataSet.setFillColor("rgb(63,81,181)");
                serviceExecutionTimeDataSet.setStrokeColor("rgb(20,20,227)");
                serviceExecutionTimeDataSet.setLabel(getString("service_execution_time"));
                bar.getData().getDatasets().add(serviceExecutionTimeDataSet);

                //Network Overhead For Business Messages (Blue)
                LabeledBarDataSet networkBusinessOverheadDataSet =
                        new LabeledBarDataSet(bundle.networkOverheadForBusinessMessagesExchangeTimes);
                networkBusinessOverheadDataSet.setFillColor("rgb(40,53,147)");
                networkBusinessOverheadDataSet.setStrokeColor("rgb(20,20,227)");
                networkBusinessOverheadDataSet.setLabel(getString("network_business_overhead"));
                bar.getData().getDatasets().add(networkBusinessOverheadDataSet);

                bar.getOptions().setMultiTooltipTemplate("<%= datasetLabel %>: <%= value %> ms");

                add(new BarChartPanel("chart", Model.of(bar)));
                add(new Label("title", getString("cd_times_chart_title")));
                add(new Label("caption", ""));
            }
        });
        charts.add(new StatsWidget("cdOverallChart") {

            private static final long serialVersionUID = -8150750732895436230L;

            @Override
            public void create() {
                Doughnut doughnut = new Doughnut();
                doughnut.getOptions().setResponsive(true);
                doughnut.getOptions().setMaintainAspectRatio(true);
                doughnut.getOptions().setTooltipTemplate("<%= label %>: <%= value %> ms");
                double serviceExecutionTime = bundle.totalServiceExecutionTime;
                double networkOverheadForBusinessMessagesExchangeTime
                        = bundle.totalNetworkOverheadForBusinessMessagesExchangeTime;
                double coordinationAlgorithmReasoningTime = bundle.totalCoordinationAlgorithmReasoningTime;
                double networkOverheadForCoordinationMessagesExchangeTime
                        = bundle.totalNetworkOverheadForCoordinationMessagesExchangeTime;
                double waitTimeForSchedulingOperation = bundle.totalWaitTimesForSchedulingOperation;
                double waitTimeForSynchronizing = bundle.totalWaitTimesForSynchronizing;

                // Service Execution and Network Business Overhead (Blue)
                doughnut.getData().add(
                        new LabeledDoughnutChartData(
                                (int) serviceExecutionTime,
                                "rgb(63,81,181)",
                                getString("service_execution_time")
                        ));
                doughnut.getData().add(
                        new LabeledDoughnutChartData(
                                (int) networkOverheadForBusinessMessagesExchangeTime,
                                "rgb(40,53,147)",
                                getString("network_business_overhead")
                        ));

                // Coordination Algorithm Reasoning Time and Network Coordination Overhead (Red)
                doughnut.getData().add(
                        new LabeledDoughnutChartData(
                                (int) coordinationAlgorithmReasoningTime,
                                "rgb(244,67,54)",
                                getString("coordination_algorithm_reasoning")
                        ));
                doughnut.getData().add(
                        new LabeledDoughnutChartData(
                                (int) networkOverheadForCoordinationMessagesExchangeTime,
                                "rgb(198,40,40)",
                                getString("network_coordination_overhead")
                        ));

                // Wait times (Green)
                doughnut.getData().add(
                        new LabeledDoughnutChartData(
                                (int) waitTimeForSchedulingOperation,
                                "rgb(76,175,80)",
                                getString("wait_scheduling_operation")
                        ));
                doughnut.getData().add(
                        new LabeledDoughnutChartData(
                                (int) waitTimeForSynchronizing,
                                "rgb(46,125,50)",
                                getString("wait_synchronizing")
                        ));
                add(new DoughnutChartPanel("chart", Model.of(doughnut)));
                add(new Label("title", getString("total_cd_times_detail_chart_title")));
                add(new Label("caption", ""));
            }
        });
        // Set Chart Legend
        ChartsLegendPanel chartsLegendPanel = new ChartsLegendPanel("chart_legend");
        charts.add(chartsLegendPanel.addLegendGroup(ChartsLegendPanel.ChartsLegendType.CD_DETAIL));

        content.add(charts);

        content.add(new AVGOperationDirectoryPanel(
                "operations", getPageReference(), choreographyId, cdName, ArtifactType.CD));
        body.add(content);
    }

    private class CDStatisticsBundle implements Serializable {

        private static final long serialVersionUID = 3642028474262240172L;

        private List<String> labels;

        private List<Double> coordinationAlgorithmReasoningTimes;

        private List<Double> networkOverheadForCoordinationMessagesExchangeTimes;

        private List<Double> waitTimesForSchedulingOperation;

        private List<Double> waitTimesForSynchronizing;

        private List<Double> serviceExecutionTimes;

        private List<Double> networkOverheadForBusinessMessagesExchangeTimes;

        private List<Integer> minSentMessages;

        private List<Integer> maxSentMessages;

        private double totalCoordinationAlgorithmReasoningTime;
        private double totalNetworkOverheadForCoordinationMessagesExchangeTime;
        private double totalWaitTimesForSchedulingOperation;
        private double totalWaitTimesForSynchronizing;
        private double totalServiceExecutionTime;
        private double totalNetworkOverheadForBusinessMessagesExchangeTime;

        CDStatisticsBundle(final List<AVGOperationDataTO> operations) {

            labels = new LinkedList<>();
            coordinationAlgorithmReasoningTimes = new LinkedList<>();
            networkOverheadForCoordinationMessagesExchangeTimes = new LinkedList<>();
            waitTimesForSchedulingOperation = new LinkedList<>();
            waitTimesForSynchronizing = new LinkedList<>();
            serviceExecutionTimes = new LinkedList<>();
            networkOverheadForBusinessMessagesExchangeTimes = new LinkedList<>();
            minSentMessages = new LinkedList<>();
            maxSentMessages = new LinkedList<>();
            
            for (AVGOperationDataTO operation : operations) {
                labels.add(operation.getName());
                coordinationAlgorithmReasoningTimes.add(operation.getCoordinationAlgorithmReasoningTime());
                networkOverheadForCoordinationMessagesExchangeTimes.add(
                        operation.getNetworkOverheadForCoordinationMessagesExchangeTime());
                waitTimesForSchedulingOperation.add(operation.getWaitTimeForSchedulingOperation());
                waitTimesForSynchronizing.add(operation.getWaitTimeForSynchronizing());
                serviceExecutionTimes.add(operation.getServiceExecutionTime());
                networkOverheadForBusinessMessagesExchangeTimes.add(
                        operation.getNetworkOverheadForBusinessMessagesExchangeTime());
                minSentMessages.add(operation.getMinMessageNumber());
                maxSentMessages.add(operation.getMaxMessageNumber());

                totalCoordinationAlgorithmReasoningTime =
                        totalCoordinationAlgorithmReasoningTime
                        + operation.getCoordinationAlgorithmReasoningTime();
                totalNetworkOverheadForCoordinationMessagesExchangeTime =
                        totalNetworkOverheadForCoordinationMessagesExchangeTime
                        + operation.getNetworkOverheadForCoordinationMessagesExchangeTime();
                totalWaitTimesForSchedulingOperation =
                        totalWaitTimesForSchedulingOperation
                        + operation.getWaitTimeForSchedulingOperation();
                totalWaitTimesForSynchronizing =
                        totalWaitTimesForSynchronizing
                        + operation.getWaitTimeForSynchronizing();
                totalServiceExecutionTime =
                        totalServiceExecutionTime
                        + operation.getServiceExecutionTime();
                totalNetworkOverheadForBusinessMessagesExchangeTime =
                        totalNetworkOverheadForBusinessMessagesExchangeTime
                        + operation.getNetworkOverheadForBusinessMessagesExchangeTime();
            }
        }
    }

}
