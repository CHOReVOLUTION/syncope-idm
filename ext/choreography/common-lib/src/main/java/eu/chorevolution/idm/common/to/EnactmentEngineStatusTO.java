/*
 * Copyright 2017 The CHOReVOLUTION project.
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

public class EnactmentEngineStatusTO extends AbstractBaseBean {

    private static final long serialVersionUID = -3983719350936183516L;

    private String key;

    private double cpuUsageRatio;

    private int ramUsage;

    private int ramTotal;

    private int storageUsage;

    private int storageTotal;

    private int virtualMachinesCount;


    public String getKey() {
        return key;
    }

    public void setKey(final String key) {
        this.key = key;
    }

    public double getCpuUsageRatio() {
        return cpuUsageRatio;
    }

    public void setCpuUsageRatio(final double cpuUsageRatio) {
        this.cpuUsageRatio = cpuUsageRatio;
    }

    public int getRamUsage() {
        return ramUsage;
    }

    public void setRamUsage(final int ramUsage) {
        this.ramUsage = ramUsage;
    }

    public int getRamTotal() {
        return ramTotal;
    }

    public void setRamTotal(final int ramTotal) {
        this.ramTotal = ramTotal;
    }

    public int getStorageUsage() {
        return storageUsage;
    }

    public void setStorageUsage(final int storageUsage) {
        this.storageUsage = storageUsage;
    }

    public int getStorageTotal() {
        return storageTotal;
    }

    public void setStorageTotal(final int storageTotal) {
        this.storageTotal = storageTotal;
    }

    public int getVirtualMachinesCount() {
        return virtualMachinesCount;
    }

    public void setVirtualMachinesCount(final int virtualMachinesCount) {
        this.virtualMachinesCount = virtualMachinesCount;
    }

}
