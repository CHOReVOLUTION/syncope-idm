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
import eu.chorevolution.idm.common.to.ChoreographyInstanceTO;
import eu.chorevolution.idm.common.to.ChoreographyTO;
import eu.chorevolution.idm.common.to.CoordinationDelegateTO;
import eu.chorevolution.idm.common.to.OperationDataTO;
import eu.chorevolution.idm.common.types.ArtifactType;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import org.apache.syncope.client.console.BookmarkablePageLinkBuilder;
import org.apache.syncope.client.console.panels.ChartsLegendPanel;
import org.apache.syncope.client.console.panels.OperationDirectoryPanel;
import org.apache.syncope.client.console.rest.MonitorRestClient;
import org.apache.syncope.client.console.widgets.StatsWidget;
import org.apache.syncope.client.console.widgets.LabeledBarDataSet;
import org.apache.syncope.client.console.widgets.LabeledDoughnutChartData;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;

public class CDDetailPage extends BaseExtPage {

    private static final long serialVersionUID = 3932245590233237182L;

    public static final String PREF_STATS_PAGINATOR_ROWS = "stats.paginator.rows";

    private final MonitorRestClient restClient;

    private final String choreographyInstancePK;

    private final String cdName;

    private final CDStatisticsBundle bundle;

    public CDDetailPage(final PageParameters parameters) {
        super(parameters);

        restClient = new MonitorRestClient();

        choreographyInstancePK = parameters.get("instance").toString();
        cdName = parameters.get("cd").toString();

        CoordinationDelegateTO cd = restClient.getCD(choreographyInstancePK, cdName);
        ChoreographyInstanceTO choreographyInstance = restClient.getChoreographyInstance(choreographyInstancePK);
        ChoreographyTO choreography = restClient.getChoreography(choreographyInstance.getChoreographyId());

        body.add(new Label("header", getString("header_title") + " " + cd.getName()));

        List<OperationDataTO> operations = restClient.operationList(choreographyInstancePK, cdName);
        bundle = new CDStatisticsBundle(operations);

        body.add(BookmarkablePageLinkBuilder.build("dashboard", "dashboardBr", Dashboard.class));
        body.add(BookmarkablePageLinkBuilder.build("choreographyBr", ChoreographyPage.class));

        PageParameters choreographyParams = new PageParameters();
        choreographyParams.add("chor", choreography.getKey());
        body.add(new BookmarkablePageLink<>("choreographyDetailsBr", ChoreographyDetailPage.class, choreographyParams)
                    .add(new Label("choreographyTitle", choreography.getName())));

        PageParameters instanceParams = new PageParameters();
        instanceParams.add("instance", choreographyInstance.getKey());
        body.add(new BookmarkablePageLink<>("instanceStatsBr", InstanceStatsPage.class, instanceParams)
                    .add(new Label("instanceTitle", "Instance " + choreographyInstance.getChoreographyInstanceId())));


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

                //Coordination Algorithm Reasoning Time (Red)
                LabeledBarDataSet algorithmReasoningDataSet = new LabeledBarDataSet(
                        bundle.coordinationAlgorithmReasoningTimes);
                algorithmReasoningDataSet.setFillColor("rgb(244,67,54)");
                algorithmReasoningDataSet.setStrokeColor("rgb(227,20,20)");
                algorithmReasoningDataSet.setLabel(getString("coordination_algorithm_reasoning"));
                bar.getData().getDatasets().add(algorithmReasoningDataSet);

                //Network Overhead For Coordination Messages (Red)
                LabeledBarDataSet networkCoordinationOverheadDataSet = new LabeledBarDataSet(
                        bundle.networkOverheadForCoordinationMessagesExchangeTimes);
                networkCoordinationOverheadDataSet.setFillColor("rgb(198,40,40)");
                networkCoordinationOverheadDataSet.setStrokeColor("rgb(227,20,20))");
                networkCoordinationOverheadDataSet.setLabel(getString("network_coordination_overhead"));
                bar.getData().getDatasets().add(networkCoordinationOverheadDataSet);

                //Wait Times (Green)
                LabeledBarDataSet waitScheduleTimeDataSet
                        = new LabeledBarDataSet(bundle.waitTimesForSchedulingOperation);
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
                LabeledBarDataSet networkBusinessOverheadDataSet = new LabeledBarDataSet(
                        bundle.networkOverheadForBusinessMessagesExchangeTimes);
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
                long serviceExecutionTime = cd.getServiceExecutionTime();
                long networkOverheadForBusinessMessagesExchangeTime
                        = cd.getNetworkOverheadForBusinessMessagesExchangeTime();
                long coordinationAlgorithmReasoningTime = cd.getCoordinationAlgorithmReasoningTime();
                long networkOverheadForCoordinationMessagesExchangeTime
                        = cd.getNetworkOverheadForCoordinationMessagesExchangeTime();
                long waitTimeForSchedulingOperation = cd.getWaitTimeForSchedulingOperation();
                long waitTimeForSynchronizing = cd.getWaitTimeForSynchronizing();

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

        content.add(new OperationDirectoryPanel(
                "operations", getPageReference(), choreographyInstancePK, cdName, ArtifactType.CD));
        body.add(content);
    }

    private class CDStatisticsBundle implements Serializable {

        private static final long serialVersionUID = 3642028474262240172L;

        private List<String> labels;

        private List<Long> coordinationAlgorithmReasoningTimes;

        private List<Long> networkOverheadForCoordinationMessagesExchangeTimes;

        private List<Long> waitTimesForSchedulingOperation;

        private List<Long> waitTimesForSynchronizing;

        private List<Long> serviceExecutionTimes;

        private List<Long> networkOverheadForBusinessMessagesExchangeTimes;

        private List<Integer> sentMessages;

        CDStatisticsBundle(final List<OperationDataTO> operations) {

            labels = new LinkedList<>();
            coordinationAlgorithmReasoningTimes = new LinkedList<>();
            networkOverheadForCoordinationMessagesExchangeTimes = new LinkedList<>();
            waitTimesForSchedulingOperation = new LinkedList<>();
            waitTimesForSynchronizing = new LinkedList<>();
            serviceExecutionTimes = new LinkedList<>();
            networkOverheadForBusinessMessagesExchangeTimes = new LinkedList<>();
            sentMessages = new LinkedList<>();

            for (OperationDataTO operation : operations) {
                labels.add(operation.getName());
                coordinationAlgorithmReasoningTimes.add(operation.getCoordinationAlgorithmReasoningTime());
                networkOverheadForCoordinationMessagesExchangeTimes.add(
                        operation.getNetworkOverheadForCoordinationMessagesExchangeTime());
                waitTimesForSchedulingOperation.add(operation.getWaitTimeForSchedulingOperation());
                waitTimesForSynchronizing.add(operation.getWaitTimeForSynchronizing());
                serviceExecutionTimes.add(operation.getServiceExecutionTime());
                networkOverheadForBusinessMessagesExchangeTimes.add(
                        operation.getNetworkOverheadForBusinessMessagesExchangeTime());
                sentMessages.add(operation.getMessageNumber());
            }
        }
    }

}
