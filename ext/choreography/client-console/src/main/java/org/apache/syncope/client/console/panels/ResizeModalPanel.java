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

import eu.chorevolution.idm.common.to.ChoreographyTO;
import java.io.Serializable;
import org.apache.commons.lang3.StringUtils;
import org.apache.syncope.client.console.SyncopeConsoleSession;
import org.apache.syncope.client.console.commons.Constants;
import org.apache.syncope.client.console.pages.BasePage;
import org.apache.syncope.client.console.rest.ChoreographyRestClient;
import org.apache.syncope.client.console.wicket.markup.html.bootstrap.dialog.BaseModal;
import org.apache.syncope.client.console.wicket.markup.html.form.AjaxSpinnerFieldPanel;
import org.apache.syncope.client.console.wicket.markup.html.form.FieldPanel;
import org.apache.syncope.common.lib.SyncopeClientException;
import org.apache.wicket.PageReference;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.Model;

public class ResizeModalPanel extends AbstractModalPanel<Serializable> {

    private static final long serialVersionUID = 1570261203472359825L;

    private final BaseModal<Serializable> resizeModal;

    private final ChoreographyTO choreography;

    private final ChoreographyRestClient choreographyRestClient;

    private final FieldPanel<Integer> sizeInput;

    public ResizeModalPanel(
            final BaseModal<Serializable> modal,
            final String choreographyKey,
            final PageReference pageRef) {
        super(modal, pageRef);
        this.resizeModal = modal;
        this.choreographyRestClient = new ChoreographyRestClient();
        this.choreography = choreographyRestClient.getChoreography(choreographyKey);

        modal.header(Model.of("Resize Choreography"));

        final WebMarkupContainer container = new WebMarkupContainer("container");
        container.setOutputMarkupId(true);
        add(container);

        final Form<String> form = new Form<>("resizeChoreographyForm");
        form.setMarkupId("resizeChoreographyForm");
        form.setOutputMarkupId(true);
        container.add(form);

        this.sizeInput = new AjaxSpinnerFieldPanel.Builder<Integer>()
                .build("size", "New Size", Integer.class, new Model<>());
        sizeInput.setRequired(true);
        sizeInput.addRequiredLabel();
        sizeInput.setModelObject(1); //Retreive this info from somewhere
        form.add(sizeInput);
    }

    @Override
    public void onSubmit(final AjaxRequestTarget target, final Form<?> form) {
        try {
            choreographyRestClient.resizeChoreography(choreography.getKey(), sizeInput.getModelObject());
            resizeModal.close(target);
            SyncopeConsoleSession.get().info(getString(Constants.OPERATION_SUCCEEDED));
        } catch (SyncopeClientException e) {
            LOG.error("While resizing {}", choreography.getName(), e);
            SyncopeConsoleSession.get().error(StringUtils.isBlank(e.getMessage())
                    ? e.getClass().getName() : e.getMessage());
        }
        ((BasePage) pageRef.getPage()).getNotificationPanel().refresh(target);
    }

}
