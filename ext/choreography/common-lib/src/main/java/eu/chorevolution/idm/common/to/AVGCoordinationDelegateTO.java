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

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import org.apache.syncope.common.lib.AbstractBaseBean;

@XmlRootElement(name = "coordination_delegate")
@XmlType
public class AVGCoordinationDelegateTO extends AbstractBaseBean {

    private static final long serialVersionUID = -7470302261846306678L;

    private String name;

    private int maxExecutedOperations;

    private int minExecutedOperations;

    private int maxCoordinationMessages;

    private int minCoordinationMessages;

    private double serviceExecutionTime;

    private double networkOverheadForBusinessMessagesExchangeTime;

    private double coordinationAlgorithmReasoningTime;

    private double networkOverheadForCoordinationMessagesExchangeTime;

    private double waitTimeForSchedulingOperation;

    private double waitTimeForSynchronizing;

    private String choreographyId;

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public int getMaxExecutedOperations() {
        return maxExecutedOperations;
    }

    public void setMaxExecutedOperations(final int maxExecutedOperations) {
        this.maxExecutedOperations = maxExecutedOperations;
    }

    public int getMinExecutedOperations() {
        return minExecutedOperations;
    }

    public void setMinExecutedOperations(final int minExecutedOperations) {
        this.minExecutedOperations = minExecutedOperations;
    }

    public int getMaxCoordinationMessages() {
        return maxCoordinationMessages;
    }

    public void setMaxCoordinationMessages(final int maxCoordinationMessages) {
        this.maxCoordinationMessages = maxCoordinationMessages;
    }

    public int getMinCoordinationMessages() {
        return minCoordinationMessages;
    }

    public void setMinCoordinationMessages(final int minCoordinationMessages) {
        this.minCoordinationMessages = minCoordinationMessages;
    }

    public double getServiceExecutionTime() {
        return serviceExecutionTime;
    }

    public void setServiceExecutionTime(final double serviceExecutionTime) {
        this.serviceExecutionTime = serviceExecutionTime;
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

    public void setCoordinationAlgorithmReasoningTime(final double coordinationAlgorithmReasoningTime) {
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

    public String getChoreographyId() {
        return choreographyId;
    }

    public void setChoreographyId(final String choreographyId) {
        this.choreographyId = choreographyId;
    }
}
