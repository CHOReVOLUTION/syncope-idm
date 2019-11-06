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
public class CoordinationDelegateTO extends AbstractBaseBean {

    private static final long serialVersionUID = -7470302261846306678L;

    private String name;

    private int executedOperations;

    private int coordinationMessages;

    private long serviceExecutionTime;

    private long networkOverheadForBusinessMessagesExchangeTime;

    private long coordinationAlgorithmReasoningTime;

    private long networkOverheadForCoordinationMessagesExchangeTime;

    private long waitTimeForSchedulingOperation;

    private long waitTimeForSynchronizing;

    private String instanceId;

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public int getExecutedOperations() {
        return executedOperations;
    }

    public void setExecutedOperations(final int executedOperations) {
        this.executedOperations = executedOperations;
    }

    public int getCoordinationMessages() {
        return coordinationMessages;
    }

    public void setCoordinationMessages(final int coordinationMessages) {
        this.coordinationMessages = coordinationMessages;
    }

    public long getServiceExecutionTime() {
        return serviceExecutionTime;
    }

    public void setServiceExecutionTime(final long serviceExecutionTime) {
        this.serviceExecutionTime = serviceExecutionTime;
    }

    public long getNetworkOverheadForBusinessMessagesExchangeTime() {
        return networkOverheadForBusinessMessagesExchangeTime;
    }

    public void setNetworkOverheadForBusinessMessagesExchangeTime(
            final long networkOverheadForBusinessMessagesExchangeTime) {
        this.networkOverheadForBusinessMessagesExchangeTime = networkOverheadForBusinessMessagesExchangeTime;
    }

    public long getCoordinationAlgorithmReasoningTime() {
        return coordinationAlgorithmReasoningTime;
    }

    public void setCoordinationAlgorithmReasoningTime(final long coordinationAlgorithmReasoningTime) {
        this.coordinationAlgorithmReasoningTime = coordinationAlgorithmReasoningTime;
    }

    public long getNetworkOverheadForCoordinationMessagesExchangeTime() {
        return networkOverheadForCoordinationMessagesExchangeTime;
    }

    public void setNetworkOverheadForCoordinationMessagesExchangeTime(
            final long networkOverheadForCoordinationMessagesExchangeTime) {
        this.networkOverheadForCoordinationMessagesExchangeTime = networkOverheadForCoordinationMessagesExchangeTime;
    }

    public long getWaitTimeForSchedulingOperation() {
        return waitTimeForSchedulingOperation;
    }

    public void setWaitTimeForSchedulingOperation(final long waitTimeForSchedulingOperation) {
        this.waitTimeForSchedulingOperation = waitTimeForSchedulingOperation;
    }

    public long getWaitTimeForSynchronizing() {
        return waitTimeForSynchronizing;
    }

    public void setWaitTimeForSynchronizing(final long waitTimeForSynchronizing) {
        this.waitTimeForSynchronizing = waitTimeForSynchronizing;
    }

    public String getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(final String instanceId) {
        this.instanceId = instanceId;
    }

}
