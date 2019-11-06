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

public class ServiceTO extends AbstractBaseBean {

    private static final long serialVersionUID = 4651984101923614953L;

    private String name;

    private int executedOperations;
    
    private long executionTime;

    private long waitTimeForResponse;


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

    public long getExecutionTime() {
        return executionTime;
    }

    public void setExecutionTime(final long executionTime) {
        this.executionTime = executionTime;
    }

    public long getWaitTimeForResponse() {
        return waitTimeForResponse;
    }

    public void setWaitTimeForResponse(final long waitTimeForResponse) {
        this.waitTimeForResponse = waitTimeForResponse;
    }
}
