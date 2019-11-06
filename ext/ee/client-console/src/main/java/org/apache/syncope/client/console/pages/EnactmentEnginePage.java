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
package org.apache.syncope.client.console.pages;

import de.agilecoders.wicket.core.markup.html.bootstrap.dialog.Modal;
import eu.chorevolution.idm.common.ChorevolutionEntitlement;
import java.io.Serializable;
import org.apache.syncope.client.console.BookmarkablePageLinkBuilder;
import org.apache.syncope.client.console.annotations.ExtPage;
import org.apache.syncope.client.console.panels.AddEnactmentEngineModalPanel;
import org.apache.syncope.client.console.panels.EnactmentEngineDirectoryPanel;
import org.apache.syncope.client.console.wicket.markup.html.bootstrap.dialog.BaseModal;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;

@ExtPage(label = "Enactment Engines", icon = "fa-cogs",
        listEntitlement = ChorevolutionEntitlement.CHOREOGRAPHY_LIST, priority = 100)
public class EnactmentEnginePage extends BaseExtPage {

    private static final long serialVersionUID = 3027209313427520303L;

    public static final String PREF_EE_PAGINATOR_ROWS = "ee.paginator.rows";

    private final BaseModal<Serializable> utilityModal = new BaseModal<>("addEnactmentModal");

    private final WebMarkupContainer content;

    public EnactmentEnginePage(final PageParameters parameters) {
        super(parameters);

        body.add(BookmarkablePageLinkBuilder.build("dashboard", "dashboardBr", Dashboard.class));

        content = new WebMarkupContainer("content");
        content.setOutputMarkupId(true);

        content.add(new EnactmentEngineDirectoryPanel("enactmentengines", getPageReference()));

        content.add(new AjaxLink("addLink") {

            private static final long serialVersionUID = 4879178530891785513L;

            @Override
            public void onClick(final AjaxRequestTarget target) {
                utilityModal.header(Model.of("New Enactment Engine"));
                utilityModal.setContent(new AddEnactmentEngineModalPanel(utilityModal, getPageReference()));
                utilityModal.show(true);
                target.add(utilityModal);
            }
        });

        utilityModal.size(Modal.Size.Large);
        utilityModal.addSubmitButton();

        utilityModal.setWindowClosedCallback(new ModalWindow.WindowClosedCallback() {

            private static final long serialVersionUID = -4920274615194015386L;

            @Override
            public void onClose(final AjaxRequestTarget target) {
                target.add(new EnactmentEngineDirectoryPanel("enactmentengines", getPageReference()));
                target.add(content);
                utilityModal.show(false);
            }
        });

        body.add(utilityModal);
        body.add(content);
        body.setOutputMarkupId(true);
    }
}
