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
package eu.chorevolution.idm.common.to;

import org.apache.syncope.common.lib.AbstractBaseBean;

public class AVGOperationDataTO extends AbstractBaseBean {

    private static final long serialVersionUID = 1345392734103423810L;

    private String name;

    private double serviceExecutionTime;

    private double serviceWaitTimeForResponse;

    private double networkOverheadForBusinessMessagesExchangeTime;

    private double coordinationAlgorithmReasoningTime;

    private double networkOverheadForCoordinationMessagesExchangeTime;

    private double waitTimeForSchedulingOperation;

    private double waitTimeForSynchronizing;

    private int minMessageNumber;

    private int maxMessageNumber;

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public double getServiceExecutionTime() {
        return serviceExecutionTime;
    }

    public void setServiceExecutionTime(final double serviceExecutionTime) {
        this.serviceExecutionTime = serviceExecutionTime;
    }

    public double getServiceWaitTimeForResponse() {
        return serviceWaitTimeForResponse;
    }

    public void setServiceWaitTimeForResponse(final double serviceWaitTimeForResponse) {
        this.serviceWaitTimeForResponse = serviceWaitTimeForResponse;
    }

    public double getNetworkOverheadForBusinessMessagesExchangeTime() {
        return networkOverheadForBusinessMessagesExchangeTime;
    }

    public void setNetworkOverheadForBusinessMessagesExchangeTime(
            final double networkOverheadForBusinessMessagesExchangeTime) {
        this.networkOverheadForBusinessMessagesExchangeTime = networkOverheadForBusinessMessagesExchangeTime;
    }

    public double getCoordinationAlgorithmReasoningTime() {
        return coordinationAlgorithmReasoningTime;
    }

    public void setCoordinationAlgorithmReasoningTime(
            final double coordinationAlgorithmReasoningTime) {
        this.coordinationAlgorithmReasoningTime = coordinationAlgorithmReasoningTime;
    }

    public double getNetworkOverheadForCoordinationMessagesExchangeTime() {
        return networkOverheadForCoordinationMessagesExchangeTime;
    }

    public void setNetworkOverheadForCoordinationMessagesExchangeTime(
            final double networkOverheadForCoordinationMessagesExchangeTime) {
        this.networkOverheadForCoordinationMessagesExchangeTime = networkOverheadForCoordinationMessagesExchangeTime;
    }

    public double getWaitTimeForSchedulingOperation() {
        return waitTimeForSchedulingOperation;
    }

    public void setWaitTimeForSchedulingOperation(final double waitTimeForSchedulingOperation) {
        this.waitTimeForSchedulingOperation = waitTimeForSchedulingOperation;
    }

    public double getWaitTimeForSynchronizing() {
        return waitTimeForSynchronizing;
    }

    public void setWaitTimeForSynchronizing(final double waitTimeForSynchronizing) {
        this.waitTimeForSynchronizing = waitTimeForSynchronizing;
    }

    public int getMinMessageNumber() {
        return minMessageNumber;
    }

    public void setMinMessageNumber(final int minMessageNumber) {
        this.minMessageNumber = minMessageNumber;
    }

    public int getMaxMessageNumber() {
        return maxMessageNumber;
    }

    public void setMaxMessageNumber(final int maxMessageNumber) {
        this.maxMessageNumber = maxMessageNumber;
    }
}
