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
package org.apache.syncope.client.console.panels;

import eu.chorevolution.idm.common.to.ChoreographyTO;
import java.io.Serializable;
import org.apache.commons.lang3.StringUtils;
import org.apache.syncope.client.console.SyncopeConsoleSession;
import org.apache.syncope.client.console.commons.Constants;
import org.apache.syncope.client.console.pages.BasePage;
import org.apache.syncope.client.console.rest.ChoreographyRestClient;
import org.apache.syncope.client.console.wicket.markup.html.bootstrap.dialog.BaseModal;
import org.apache.syncope.client.console.wicket.markup.html.form.AjaxTextFieldPanel;
import org.apache.syncope.client.console.wicket.markup.html.form.FieldPanel;
import org.apache.syncope.common.lib.SyncopeClientException;
import org.apache.syncope.common.lib.to.GroupTO;
import org.apache.wicket.PageReference;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.Model;

public class EditChoreographyModalPanel extends AbstractModalPanel<Serializable> {

    private static final long serialVersionUID = 3104367246705771520L;

    private final FieldPanel<String> nameInput;

    private final FieldPanel<String> descriptionInput;

    private final ChoreographyRestClient restClient;

    private final GroupTO choreography;

    public EditChoreographyModalPanel(
            final BaseModal<Serializable> modal,
            final PageReference pageRef,
            final GroupTO choreography) {
        super(modal, pageRef);

        this.restClient = new ChoreographyRestClient();
        this.choreography = choreography;

        final WebMarkupContainer container = new WebMarkupContainer("container");
        container.setOutputMarkupId(true);
        add(container);

        final Form<String> form = new Form<>("editChoreographyForm");
        form.setMarkupId("editChoreographyForm");
        form.setOutputMarkupId(true);
        container.add(form);

        this.nameInput = new AjaxTextFieldPanel("name", "Name", new Model<>(), true);
        nameInput.setReadOnly(true);
        nameInput.setModelObject(choreography.getName());
        form.add(nameInput);

        this.descriptionInput = new AjaxTextFieldPanel("description", "Description", new Model<>(), true);
        descriptionInput.setRequired(true);
        if (choreography.getPlainAttrMap().containsKey("description")) {
            descriptionInput.setModelObject(choreography.getPlainAttrMap().get("description").getValues().get(0));
        }

        form.add(descriptionInput);
    }

    @Override
    public void onSubmit(final AjaxRequestTarget target, final Form<?> form) {
        try {
            ChoreographyTO choreographyTO = new ChoreographyTO();
            choreographyTO.setKey(choreography.getKey());
            choreographyTO.setName(choreography.getName());
            choreographyTO.setDescription(descriptionInput.getModelObject());
            restClient.updateChoreograhpy(choreographyTO);
            modal.close(target);
            SyncopeConsoleSession.get().info(getString(Constants.OPERATION_SUCCEEDED));
        } catch (SyncopeClientException e) {
            LOG.error("While creating new Enactment Engine", e);
            SyncopeConsoleSession.get().error(StringUtils.isBlank(e.getMessage())
                    ? e.getClass().getName() : e.getMessage());
        }
        ((BasePage) pageRef.getPage()).getNotificationPanel().refresh(target);
    }
    
}
