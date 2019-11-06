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

public class VirtualMachineInfoTO extends AbstractBaseBean {

    private static final long serialVersionUID = 3612465719052681573L;

    private String key;

    private String chorId;

    private String chorDisplay;

    private String ip;

    private String hostname;

    private String sysOp;

    private String choreography;

    private boolean isChoreography;

    private boolean isLoadBalancer;

    private int cpuCount;

    private double cpuUsageRatio;

    private int ramUsage;

    private int ramTotal;

    private int storageUsage;

    private int storageTotal;

    public String getKey() {
        return key;
    }

    public void setKey(final String key) {
        this.key = key;
    }

    public String getChorId() {
        return chorId;
    }

    public void setChorId(final String chorId) {
        this.chorId = chorId;
    }

    public String getChorDisplay() {
        return chorDisplay;
    }

    public void setChorDisplay(final String chorDisplay) {
        this.chorDisplay = chorDisplay;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(final String ip) {
        this.ip = ip;
    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(final String hostname) {
        this.hostname = hostname;
    }

    public String getSysOp() {
        return sysOp;
    }

    public void setSysOp(final String sysOp) {
        this.sysOp = sysOp;
    }

    public String getChoreography() {
        return choreography;
    }

    public void setChoreography(final String choreography) {
        this.choreography = choreography;
    }

    public boolean getIsChoreography() {
        return isChoreography;
    }

    public void setIsChoreography(final boolean isChoreography) {
        this.isChoreography = isChoreography;
    }

    public boolean getIsLoadBalancer() {
        return isLoadBalancer;
    }

    public void setIsLoadBalancer(final boolean isLoadBalancer) {
        this.isLoadBalancer = isLoadBalancer;
    }

    public int getCpuCount() {
        return cpuCount;
    }

    public void setCpuCount(final int cpuCount) {
        this.cpuCount = cpuCount;
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
}
