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

import de.agilecoders.wicket.extensions.markup.html.bootstrap.form.select.BootstrapSelect;
import java.io.Serializable;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.apache.syncope.client.console.SyncopeConsoleSession;
import org.apache.syncope.client.console.commons.Constants;
import org.apache.syncope.client.console.pages.BasePage;
import org.apache.syncope.client.console.rest.AnyObjectRestClient;
import org.apache.syncope.client.console.rest.ChoreographyRestClient;
import org.apache.syncope.client.console.wicket.markup.html.bootstrap.dialog.BaseModal;
import org.apache.syncope.common.lib.SyncopeClientException;
import org.apache.syncope.common.lib.SyncopeConstants;
import org.apache.syncope.common.lib.search.AnyObjectFiqlSearchConditionBuilder;
import org.apache.syncope.common.lib.to.AnyObjectTO;
import org.apache.syncope.common.lib.to.GroupTO;
import org.apache.wicket.PageReference;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.markup.html.repeater.util.SortParam;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.IModel;

public class SelectEEModalPanel extends AbstractModalPanel<Serializable> {

    private static final long serialVersionUID = -1656302837627844520L;

    private final ChoreographyRestClient choreographyRestClient;

    private final GroupTO choreography;

    private final DropDownChoice<AnyObjectTO> enactmentEngineSelect;

    public SelectEEModalPanel(
            final BaseModal<Serializable> modal,
            final GroupTO choreography,
            final PageReference pageRef) {
        super(modal, pageRef);

        this.choreography = choreography;
        this.choreographyRestClient = new ChoreographyRestClient();

        final WebMarkupContainer container = new WebMarkupContainer("container");
        container.setOutputMarkupId(true);
        add(container);

        final Form<String> form = new Form<>("selectEEForm");
        form.setMarkupId("selectEEForm");
        form.setOutputMarkupId(true);
        container.add(form);

        this.enactmentEngineSelect = new EnactmentEngineDropdown("eeSelect");
        form.add(enactmentEngineSelect);
    }

    @Override
    public void onSubmit(final AjaxRequestTarget target, final Form<?> form) {
        try {
            AnyObjectTO selectedEE = enactmentEngineSelect.getModel().getObject();
            if (selectedEE != null) {
                choreographyRestClient.enactChoreography(choreography.getKey(), selectedEE.getKey());
                modal.close(target);
                SyncopeConsoleSession.get().info(getString(Constants.OPERATION_SUCCEEDED));    
            } else {
                SyncopeConsoleSession.get()
                        .error("No Enactment Engine selected for choreography " + choreography.getName());
            }
        } catch (SyncopeClientException e) {
            LOG.error("While enacting choreography {}", choreography.getName(), e);
            SyncopeConsoleSession.get().error(StringUtils.isBlank(e.getMessage())
                    ? e.getClass().getName() : e.getMessage());
        }
        ((BasePage) pageRef.getPage()).getNotificationPanel().refresh(target);
    }

    private class EnactmentEngineDropdown extends BootstrapSelect<AnyObjectTO> {

        private static final long serialVersionUID = -5192516768556628105L;

        private AnyObjectTO enactmentEngine;

        private class EnactmentEngineRenderer extends ChoiceRenderer<AnyObjectTO> {

            private static final long serialVersionUID = -5883415718568173146L;

            @Override
            public String getDisplayValue(final AnyObjectTO object) {
                return object.getName();
            }
        }

        EnactmentEngineDropdown(final String id) {
            super(id);

            AnyObjectRestClient anyObjectRestClient = new AnyObjectRestClient();

            List<AnyObjectTO> enactmentEngines = anyObjectRestClient.search(
                    SyncopeConstants.ROOT_REALM,
                    new AnyObjectFiqlSearchConditionBuilder("ENACTMENT ENGINE").query(),
                    1,
                    -1,
                    new SortParam<>("name", false),
                    "ENACTMENT ENGINE");

            setChoiceRenderer(new EnactmentEngineRenderer());
            setChoices(enactmentEngines);
            setModel(new IModel<AnyObjectTO>() {

                private static final long serialVersionUID = 8125687269026843312L;

                @Override
                public AnyObjectTO getObject() {
                    return enactmentEngine;
                }

                @Override
                public void setObject(final AnyObjectTO object) {
                    enactmentEngine = object;
                }

                @Override
                public void detach() {
                    // Empty
                }
            });
        }
        
    }
}
