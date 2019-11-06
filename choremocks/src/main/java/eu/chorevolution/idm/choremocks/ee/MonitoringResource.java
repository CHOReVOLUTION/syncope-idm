/*
 * Copyright 2017 The CHOReVOLUTION project
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
package eu.chorevolution.idm.choremocks.ee;

import eu.chorevolution.idm.common.to.EnactmentEngineStatusTO;
import eu.chorevolution.idm.common.to.VirtualMachineInfoTO;
import javax.ws.rs.core.Response;

public class MonitoringResource implements MonitoringApi {

    @Override
    public Response getEngineStatus() {
        EnactmentEngineStatusTO enactmentEngineStatusTO = new EnactmentEngineStatusTO();

        enactmentEngineStatusTO.setKey("dummyKey");
        enactmentEngineStatusTO.setRamUsage(4880);
        enactmentEngineStatusTO.setRamTotal(13336);
        enactmentEngineStatusTO.setStorageTotal(3024);
        enactmentEngineStatusTO.setStorageUsage(628);
        enactmentEngineStatusTO.setCpuUsageRatio(36.0);
        enactmentEngineStatusTO.setVirtualMachinesCount(4);

        return Response.ok(enactmentEngineStatusTO).build();
    }

    @Override
    public Response getEngineVirtualMachines() {
        VirtualMachineInfoTO info1 = new VirtualMachineInfoTO();
        info1.setKey("vm001");
        info1.setChorId("xx45671");
        info1.setChorDisplay("In-store Marketing and Sales");
        info1.setSysOp("Ubuntu 16.10");
        info1.setIp("192.168.0.11");
        info1.setHostname("localhost");
        info1.setCpuCount(4);
        info1.setCpuUsageRatio(30.5);
        info1.setRamUsage(1500);
        info1.setRamTotal(4096);
        info1.setStorageUsage(100);
        info1.setStorageTotal(1000);
        info1.setIsChoreography(true);
        info1.setIsLoadBalancer(false);

        VirtualMachineInfoTO info2 = new VirtualMachineInfoTO();
        info2.setKey("vm002");
        info2.setChorId("xx45671");
        info2.setChorDisplay("In-store Marketing and Sales");
        info2.setSysOp("Ubuntu 16.10");
        info2.setIp("192.168.0.12");
        info2.setHostname("localhost");
        info2.setCpuCount(2);
        info2.setCpuUsageRatio(51);
        info2.setRamUsage(400);
        info2.setRamTotal(1048);
        info2.setStorageUsage(100);
        info2.setStorageTotal(512);
        info2.setIsChoreography(false);
        info2.setIsLoadBalancer(true);

        VirtualMachineInfoTO info3 = new VirtualMachineInfoTO();
        info3.setKey("vm003");
        info3.setChorId("ww45672");
        info3.setChorDisplay("Smart Mobility and Tourism");
        info3.setSysOp("Ubuntu 16.10");
        info3.setIp("192.168.0.13");
        info3.setHostname("localhost");
        info3.setCpuCount(4);
        info3.setCpuUsageRatio(26.5);
        info3.setRamUsage(2230);
        info3.setRamTotal(4096);
        info3.setStorageUsage(88);
        info3.setStorageTotal(512);
        info3.setIsChoreography(false);
        info3.setIsLoadBalancer(true);

        VirtualMachineInfoTO info4 = new VirtualMachineInfoTO();
        info4.setKey("vm004");
        info4.setChorId("ww45672");
        info4.setChorDisplay("Smart Mobility and Tourism");
        info4.setSysOp("Ubuntu 16.10");
        info4.setIp("192.168.0.14");
        info4.setHostname("localhost");
        info4.setCpuCount(4);
        info4.setCpuUsageRatio(44);
        info4.setRamUsage(750);
        info4.setRamTotal(4096);
        info4.setStorageUsage(340);
        info4.setStorageTotal(1000);
        info4.setIsChoreography(true);
        info4.setIsLoadBalancer(false);

        VirtualMachineInfoTO[] result = new VirtualMachineInfoTO[4];
        result[0] = info1;
        result[1] = info2;
        result[2] = info3;
        result[3] = info4;

        if (result == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Error while acting on EE").build();
        }
        return Response.ok(result).build();
    }

    @Override
    public Response getChorStatus(final String choreographyId) {

        if (choreographyId == null || choreographyId.isEmpty()) {
            return Response.serverError().entity("chorID cannot be blank").build();
        }

        EnactmentEngineStatusTO enactmentEngineStatusTO = new EnactmentEngineStatusTO();

        enactmentEngineStatusTO.setKey("dummyKey");
        enactmentEngineStatusTO.setRamUsage(1900);
        enactmentEngineStatusTO.setRamTotal(5144);
        enactmentEngineStatusTO.setStorageTotal(1512);
        enactmentEngineStatusTO.setStorageUsage(200);
        enactmentEngineStatusTO.setCpuUsageRatio(37.0);
        enactmentEngineStatusTO.setVirtualMachinesCount(2);

        return Response.ok(enactmentEngineStatusTO).build();
    }

    @Override
    public Response getChorVirtualMachines(final String choreographyId) {

        if (choreographyId == null || choreographyId.isEmpty()) {
            return Response.serverError().entity("chorID cannot be blank").build();
        }

        VirtualMachineInfoTO info1 = new VirtualMachineInfoTO();
        info1.setKey("vm001");
        info1.setChorId(choreographyId);
        info1.setChorDisplay("In-store Marketing and Sales");
        info1.setSysOp("Ubuntu 16.10");
        info1.setIp("192.168.0.11");
        info1.setHostname("localhost");
        info1.setCpuCount(4);
        info1.setCpuUsageRatio(30.5);
        info1.setRamUsage(1500);
        info1.setRamTotal(4096);
        info1.setStorageUsage(100);
        info1.setStorageTotal(1000);
        info1.setIsChoreography(true);
        info1.setIsLoadBalancer(false);

        VirtualMachineInfoTO info2 = new VirtualMachineInfoTO();
        info2.setKey("vm002");
        info2.setChorId(choreographyId);
        info2.setChorDisplay("In-store Marketing and Sales");
        info2.setSysOp("Ubuntu 16.10");
        info2.setIp("192.168.0.12");
        info2.setHostname("localhost");
        info2.setCpuCount(2);
        info2.setCpuUsageRatio(51);
        info2.setRamUsage(400);
        info2.setRamTotal(1048);
        info2.setStorageUsage(100);
        info2.setStorageTotal(512);
        info2.setIsChoreography(false);
        info2.setIsLoadBalancer(true);

        VirtualMachineInfoTO[] result = new VirtualMachineInfoTO[2];
        result[0] = info1;
        result[1] = info2;

        if (result == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Choreografy not found for choreographyId: " + choreographyId).build();
        }
        return Response.ok(result).build();
    }
}
