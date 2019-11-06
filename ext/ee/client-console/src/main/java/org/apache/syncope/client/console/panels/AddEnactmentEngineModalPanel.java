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

import eu.chorevolution.idm.common.to.EnactmentEngineTO;
import java.io.Serializable;
import java.net.MalformedURLException;
import org.apache.commons.lang3.StringUtils;
import org.apache.syncope.client.console.SyncopeConsoleSession;
import org.apache.syncope.client.console.commons.Constants;
import org.apache.syncope.client.console.pages.BasePage;
import static org.apache.syncope.client.console.panels.AbstractModalPanel.LOG;
import org.apache.syncope.client.console.wicket.markup.html.bootstrap.dialog.BaseModal;
import org.apache.syncope.common.lib.SyncopeClientException;
import org.apache.wicket.PageReference;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.form.Form;

public class AddEnactmentEngineModalPanel extends AbstractEnactmentEngineModalPanel {

    private static final long serialVersionUID = -8997920710314548677L;

    public AddEnactmentEngineModalPanel(final BaseModal<Serializable> modal, final PageReference pageRef) {
        super(modal, pageRef);
    }

    @Override
    public void onSubmit(final AjaxRequestTarget target, final Form<?> form) {
        try {
            checkURL(urlInput.getModelObject());
            EnactmentEngineTO enactmentEngineTO = new EnactmentEngineTO();
            enactmentEngineTO.setName(nameInput.getModelObject());
            enactmentEngineTO.setUsername(usernameInput.getModelObject());
            enactmentEngineTO.setPassword(passwordInput.getModelObject());
            enactmentEngineTO.setBaseUrl(urlInput.getModelObject());
            restClient.create(enactmentEngineTO);
            modal.close(target);
            SyncopeConsoleSession.get().info(getString(Constants.OPERATION_SUCCEEDED));
        } catch (MalformedURLException e) {
            SyncopeConsoleSession.get().error(getString("invalid_url"));
        } catch (SyncopeClientException e) {
            LOG.error("While creating new Enactment Engine", e);
            SyncopeConsoleSession.get().error(StringUtils.isBlank(e.getMessage())
                    ? e.getClass().getName() : e.getMessage());
        }
        ((BasePage) pageRef.getPage()).getNotificationPanel().refresh(target);
    }
}
