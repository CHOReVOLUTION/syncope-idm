/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.syncope.client.enduser;

import java.io.File;
import java.io.Serializable;
import java.util.Properties;
import org.apache.commons.io.FileUtils;
import org.apache.wicket.WicketRuntimeException;
import org.apache.wicket.protocol.http.WebApplication;

public class ChorevolutionEnduserApplication extends SyncopeEnduserApplication implements Serializable {

    private static final String ENDUSER_PROPERTIES = "chorevolutionenduser.properties";

    private static final long serialVersionUID = -4581937136715493196L;

    public static ChorevolutionEnduserApplication get() {
        return (ChorevolutionEnduserApplication) WebApplication.get();
    }

    private boolean viewGroupsEnabled;

    @Override
    protected void init() {
        super.init();

        // read enduser.properties
        Properties props = new Properties();
        try {
            props.load(getClass().getResourceAsStream("/" + ENDUSER_PROPERTIES));
            File enduserDir = new File(props.getProperty("enduser.directory"));
            if (enduserDir.exists() && enduserDir.canRead() && enduserDir.isDirectory()) {
                File enduserDirProps = FileUtils.getFile(enduserDir, ENDUSER_PROPERTIES);
                if (enduserDirProps.exists() && enduserDirProps.canRead() && enduserDirProps.isFile()) {
                    props.clear();
                    props.load(FileUtils.openInputStream(enduserDirProps));
                }
            }
        } catch (Exception e) {
            throw new WicketRuntimeException("Could not read " + ENDUSER_PROPERTIES, e);
        }
        viewGroupsEnabled = Boolean.parseBoolean(props.getProperty("viewgroups"));
    }

    public boolean isViewGroupsEnabled() {
        return viewGroupsEnabled;
    }
}
