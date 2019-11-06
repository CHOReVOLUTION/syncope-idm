/*
 * Copyright 2016 The CHOReVOLUTION project
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
package eu.chorevolution.idm.common.types;

import javax.xml.bind.annotation.XmlType;
import org.apache.syncope.common.lib.AbstractBaseBean;

@XmlType
public class SecurityFilterInfo extends AbstractBaseBean {

    private static final long serialVersionUID = -2334732785143202012L;

    private SecurityFilterStatus status;

    private String securityContext;

    public SecurityFilterStatus getStatus() {
        return status;
    }

    public void setStatus(final SecurityFilterStatus status) {
        this.status = status;
    }

    public String getSecurityContext() {
        return securityContext;
    }

    public void setSecurityContext(final String securityContext) {
        this.securityContext = securityContext;
    }

}
