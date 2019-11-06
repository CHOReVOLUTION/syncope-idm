/*
 * Copyright 2015 The CHOReVOLUTION project
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

@XmlRootElement(name = "choreography")
@XmlType
public class ChoreographyTO extends AbstractBaseBean {

    private static final long serialVersionUID = 4518569389223742217L;

    private String key;

    private String choreographyId;

    private String name;

    private String description;

    private byte[] chorspec;

    private byte[] diagram;

    private byte[] messages;

    private byte[] image;

    private String status;

    private String synthesisProcessorKey;

    private String enactmentEngineKey;

    public String getKey() {
        return key;
    }

    public void setKey(final String key) {
        this.key = key;
    }

    public String getChoreographyId() {
        return choreographyId;
    }

    public void setChoreographyId(final String choreographyId) {
        this.choreographyId = choreographyId;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public byte[] getChorspec() {
        return chorspec;
    }

    public void setChorspec(final byte[] chorspec) {
        this.chorspec = chorspec;
    }

    public byte[] getDiagram() {
        return diagram;
    }

    public void setDiagram(final byte[] diagram) {
        this.diagram = diagram;
    }

    public byte[] getMessages() {
        return messages;
    }

    public void setMessages(final byte[] messages) {
        this.messages = messages;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(final byte[] image) {
        this.image = image;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(final String status) {
        this.status = status;
    }

    public String getSynthesisProcessorKey() {
        return synthesisProcessorKey;
    }

    public void setSynthesisProcessorKey(final String synthesisProcessorKey) {
        this.synthesisProcessorKey = synthesisProcessorKey;
    }

    public String getEnactmentEngineKey() {
        return enactmentEngineKey;
    }

    public void setEnactmentEngineKey(final String enactmentEngineKey) {
        this.enactmentEngineKey = enactmentEngineKey;
    }

}
