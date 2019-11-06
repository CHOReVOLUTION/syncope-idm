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
package org.apache.syncope.client.console.panels;

import eu.chorevolution.idm.common.to.SynthesisProcessorTO;
import java.io.Serializable;
import java.net.MalformedURLException;
import org.apache.commons.lang3.StringUtils;
import org.apache.syncope.client.console.SyncopeConsoleSession;
import org.apache.syncope.client.console.commons.Constants;
import org.apache.syncope.client.console.pages.BasePage;
import static org.apache.syncope.client.console.panels.AbstractModalPanel.LOG;
import org.apache.syncope.client.console.rest.SynthesisProcessorRestClient;
import org.apache.syncope.client.console.wicket.markup.html.bootstrap.dialog.BaseModal;
import org.apache.syncope.common.lib.SyncopeClientException;
import org.apache.wicket.PageReference;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.form.Form;

public class AddSynthesisProcessorModalPanel extends AbstractSynthesisProcessorModalPanel {

    private static final long serialVersionUID = -3706242199931379878L;

    private final SynthesisProcessorRestClient restClient;

    public AddSynthesisProcessorModalPanel(final BaseModal<Serializable> modal, final PageReference pageRef) {
        super(modal, pageRef);

        this.restClient = new SynthesisProcessorRestClient();
    }

    @Override
    public void onSubmit(final AjaxRequestTarget target, final Form<?> form) {
        try {
            checkURL(urlInput.getModelObject());
            SynthesisProcessorTO synthesisProcessorTO = new SynthesisProcessorTO();
            synthesisProcessorTO.setName(nameInput.getModelObject());
            synthesisProcessorTO.setBaseUrl(urlInput.getModelObject());
            synthesisProcessorTO.setUsername(usernameInput.getModelObject());
            synthesisProcessorTO.setPassword(passwordInput.getModelObject());
            restClient.create(synthesisProcessorTO);
            modal.close(target);
            SyncopeConsoleSession.get().info(getString(Constants.OPERATION_SUCCEEDED));
        } catch (MalformedURLException e) {
            SyncopeConsoleSession.get().error(getString("invalid_url"));
        } catch (SyncopeClientException e) {
            LOG.error("While creating new Synthesis Processor", e);
            SyncopeConsoleSession.get().error(StringUtils.isBlank(e.getMessage())
                    ? e.getClass().getName() : e.getMessage());
        }
        ((BasePage) pageRef.getPage()).getNotificationPanel().refresh(target);
    }
}
