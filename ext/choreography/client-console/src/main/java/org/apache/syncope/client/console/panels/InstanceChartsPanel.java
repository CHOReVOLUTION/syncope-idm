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

import com.pingunaut.wicket.chartjs.chart.impl.Bar;
import com.pingunaut.wicket.chartjs.chart.impl.Doughnut;
import com.pingunaut.wicket.chartjs.core.panel.BarChartPanel;
import com.pingunaut.wicket.chartjs.core.panel.DoughnutChartPanel;
import eu.chorevolution.idm.common.to.ChoreographyInstanceTO;
import eu.chorevolution.idm.common.to.CoordinationDelegateTO;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import org.apache.syncope.client.console.rest.MonitorRestClient;
import org.apache.syncope.client.console.widgets.LabeledBarDataSet;
import org.apache.syncope.client.console.widgets.LabeledDoughnutChartData;
import org.apache.syncope.client.console.widgets.StatsWidget;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;

public class InstanceChartsPanel extends Panel {

    private static final long serialVersionUID = 2437848762609438151L;

    private final InstanceStatisticsBundle bundle;

    private final MonitorRestClient restClient;

    private final ChoreographyInstanceTO choreographyInstance;

    public InstanceChartsPanel(final String id, final ChoreographyInstanceTO choreographyInstance) {
        super(id);

        this.restClient = new MonitorRestClient();

        List<CoordinationDelegateTO> cds = restClient.cdList(choreographyInstance.getKey());
        this.bundle = new InstanceStatisticsBundle(cds);

        this.choreographyInstance = choreographyInstance;

        // Set duration info
        add(new Label("duration",
                getString("duration") + " " + choreographyInstance.getExecutionTime() + " ms"
        ));

        WebMarkupContainer charts = new WebMarkupContainer("charts");
        //Total overhead and total execution time per CD
        charts.add(new StatsWidget("cdTimesChart") {

            private static final long serialVersionUID = -8150750732895436230L;

            @Override
            public void create() {

                Bar bar = new Bar();
                bar.getData().getLabels().addAll(bundle.labels);

                LabeledBarDataSet coordinationTimeDataSet = new LabeledBarDataSet(bundle.coordinationTimes);
                coordinationTimeDataSet.setFillColor("rgba(227,20,20,0.6)");
                coordinationTimeDataSet.setStrokeColor("rgba(227,20,20,0.8)");
                coordinationTimeDataSet.setLabel(getString("coordination_time"));
                bar.getData().getDatasets().add(coordinationTimeDataSet);

                LabeledBarDataSet waitTimeDataSet = new LabeledBarDataSet(bundle.waitTimes);
                waitTimeDataSet.setFillColor("rgba(20,227,20,0.6)");
                waitTimeDataSet.setStrokeColor("rgba(20,227,20,0.8)");
                waitTimeDataSet.setLabel(getString("wait_time"));
                bar.getData().getDatasets().add(waitTimeDataSet);

                LabeledBarDataSet serviceExecutionTimeDataSet = new LabeledBarDataSet(bundle.serviceExecutionTimes);
                serviceExecutionTimeDataSet.setFillColor("rgba(20,20,227,0.6)");
                serviceExecutionTimeDataSet.setStrokeColor("rgba(20,20,227,0.8)");
                serviceExecutionTimeDataSet.setLabel(getString("service_execution_time"));
                bar.getData().getDatasets().add(serviceExecutionTimeDataSet);

                bar.getOptions().setMultiTooltipTemplate("<%= datasetLabel %>: <%= value %> ms");

                add(new BarChartPanel("chart", Model.of(bar)));
                add(new Label("title", getString("cd_times_chart_title")));
                add(new Label("caption", ""));

            }
        });
        //Total task execution time and total overhead
        charts.add(new StatsWidget("totalTimesChart") {

            private static final long serialVersionUID = -8150750732895436230L;

            @Override
            public void create() {
                Doughnut doughnut = new Doughnut();
                doughnut.getOptions().setResponsive(true);
                doughnut.getOptions().setMaintainAspectRatio(true);
                doughnut.getOptions().setTooltipTemplate("<%= label %>: <%= value %> ms");
                doughnut.getData().add(
                        new LabeledDoughnutChartData(
                                (int) bundle.totalServiceExecution,
                                "rgb(20,20,227)",
                                getString("service_execution_time")
                        ));
                doughnut.getData().add(
                        new LabeledDoughnutChartData(
                                (int) bundle.totalCoordinationTime,
                                "rgb(227,20,20)",
                                getString("coordination_time")
                        ));
                doughnut.getData().add(
                        new LabeledDoughnutChartData(
                                (int) bundle.totalWait,
                                "rgb(20,227,20)",
                                getString("wait_time")
                        ));
                add(new DoughnutChartPanel("chart", Model.of(doughnut)));
                add(new Label("title", getString("instance_total_times_title")));
                add(new Label("caption", ""));
            }
        });
        //Operations executed and sent messages per CD
        charts.add(new StatsWidget("cdOperationsChart") {

            private static final long serialVersionUID = -8150750732895436230L;

            @Override
            public void create() {
                Bar bar = new Bar();
                bar.getData().getLabels().addAll(bundle.labels);

                LabeledBarDataSet operationsDataSet = new LabeledBarDataSet(bundle.operations);
                operationsDataSet.setFillColor("rgba(151,187,205,0.6)");
                operationsDataSet.setStrokeColor("rgba(151,187,205,0.8)");
                operationsDataSet.setLabel(getString("performed_operations"));
                bar.getData().getDatasets().add(operationsDataSet);

                LabeledBarDataSet messagesDataSet = new LabeledBarDataSet(bundle.messages);
                messagesDataSet.setLabel(getString("coordination_messages_sent"));
                bar.getData().getDatasets().add(messagesDataSet);

                bar.getOptions().setMultiTooltipTemplate("<%= datasetLabel %>: <%= value %>");
                add(new BarChartPanel("chart", Model.of(bar)));
                add(new Label("title", getString("instance_cd_operations_chart_title")));
                add(new Label("caption", ""));
            }
        });
        //Total executed operations and sent messages
        charts.add(new StatsWidget("totalOperationsChart") {

            private static final long serialVersionUID = -8150750732895436230L;

            @Override
            public void create() {
                Doughnut doughnut = new Doughnut();
                doughnut.getOptions().setResponsive(true);
                doughnut.getOptions().setMaintainAspectRatio(true);
                doughnut.getOptions().setTooltipTemplate("<%= label %>: <%= value %>");
                doughnut.getData().add(
                        new LabeledDoughnutChartData(
                                bundle.totalMessages,
                                "rgb(220,220,220)",
                                getString("sent_messages")
                        ));
                doughnut.getData().add(
                        new LabeledDoughnutChartData(
                                bundle.totalOperations,
                                "rgb(151,187,205)",
                                getString("performed_operations")
                        ));
                add(new DoughnutChartPanel("chart", Model.of(doughnut)));
                add(new Label("title", getString("instance_total_cd_operations_chart_title")));
                add(new Label("caption", ""));
            }
        });
        //Set legend
        ChartsLegendPanel chartsLegendPanel = new ChartsLegendPanel("chart_legend");
        charts.add(chartsLegendPanel.addLegendGroup(ChartsLegendPanel.ChartsLegendType.INSTANCE_DETAIL));
        add(charts);
    }

    private class InstanceStatisticsBundle implements Serializable {

        private static final long serialVersionUID = 3642028474262240172L;

        private List<String> labels;

        private List<Long> coordinationTimes;

        private List<Long> serviceExecutionTimes;

        private List<Long> waitTimes;

        private List<Integer> operations;

        private List<Integer> messages;

        private int totalMessages;

        private int totalOperations;

        private long totalServiceExecution;

        private long totalCoordinationTime;

        private long totalWait;

        InstanceStatisticsBundle(final List<CoordinationDelegateTO> cds) {
            labels = new LinkedList<>();
            coordinationTimes = new LinkedList<>();
            serviceExecutionTimes = new LinkedList<>();
            waitTimes = new LinkedList<>();
            operations = new LinkedList<>();
            messages = new LinkedList<>();
            totalMessages = 0;
            totalOperations = 0;
            totalServiceExecution = 0;
            totalCoordinationTime = 0;
            totalWait = 0;

            for (CoordinationDelegateTO cd : cds) {
                labels.add(cd.getName());
                coordinationTimes.add(
                        cd.getCoordinationAlgorithmReasoningTime()
                        + cd.getNetworkOverheadForCoordinationMessagesExchangeTime()
                );
                serviceExecutionTimes.add(
                        cd.getServiceExecutionTime() + cd.getNetworkOverheadForBusinessMessagesExchangeTime()
                );
                waitTimes.add(
                        cd.getWaitTimeForSchedulingOperation() + cd.getWaitTimeForSynchronizing()
                );
                operations.add(cd.getExecutedOperations());
                messages.add(cd.getCoordinationMessages());
                totalMessages = totalMessages + cd.getCoordinationMessages();
                totalOperations = totalOperations + cd.getExecutedOperations();
                totalServiceExecution = totalServiceExecution
                        + cd.getServiceExecutionTime()
                        + cd.getNetworkOverheadForBusinessMessagesExchangeTime();
                totalCoordinationTime = totalCoordinationTime
                        + cd.getCoordinationAlgorithmReasoningTime()
                        + cd.getNetworkOverheadForCoordinationMessagesExchangeTime();
                totalWait = totalWait + cd.getWaitTimeForSchedulingOperation() + cd.getWaitTimeForSynchronizing();
            }
        }
    }
}
