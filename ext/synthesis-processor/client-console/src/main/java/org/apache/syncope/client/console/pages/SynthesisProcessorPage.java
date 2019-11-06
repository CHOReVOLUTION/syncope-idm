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
package org.apache.syncope.client.console.pages;

import de.agilecoders.wicket.core.markup.html.bootstrap.dialog.Modal;
import eu.chorevolution.idm.common.ChorevolutionEntitlement;
import java.io.Serializable;
import org.apache.syncope.client.console.BookmarkablePageLinkBuilder;
import org.apache.syncope.client.console.annotations.ExtPage;
import org.apache.syncope.client.console.panels.AddSynthesisProcessorModalPanel;
import org.apache.syncope.client.console.panels.SynthesisProcessorDirectoryPanel;
import org.apache.syncope.client.console.wicket.markup.html.bootstrap.dialog.BaseModal;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;

@ExtPage(label = "Synthesis Processors", icon = "fa-wrench",
        listEntitlement = ChorevolutionEntitlement.CHOREOGRAPHY_LIST, priority = 100)
public class SynthesisProcessorPage extends BaseExtPage {

    public static final String PREF_SYNTH_PAGINATOR_ROWS = "synthesisprocessor.paginator.rows";

    private static final long serialVersionUID = -337325733703904383L;

    private final BaseModal<Serializable> utilityModal = new BaseModal<>("addSynthesisProcessorModal");

    private final WebMarkupContainer content;

    public SynthesisProcessorPage(final PageParameters parameters) {
        super(parameters);

        body.add(BookmarkablePageLinkBuilder.build("dashboard", "dashboardBr", Dashboard.class));

        content = new WebMarkupContainer("content");
        content.setOutputMarkupId(true);

        content.add(new SynthesisProcessorDirectoryPanel("synthesisprocessors", getPageReference()));

                content.add(new AjaxLink("addLink") {

            private static final long serialVersionUID = 4879178530891785513L;

            @Override
            public void onClick(final AjaxRequestTarget target) {
                utilityModal.header(Model.of("New Synthesis Processor"));
                utilityModal.setContent(new AddSynthesisProcessorModalPanel(utilityModal, getPageReference()));
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
                target.add(new SynthesisProcessorDirectoryPanel("synthesisprocessors", getPageReference()));
                target.add(content);
                utilityModal.show(false);
            }
        });

        body.add(utilityModal);
        body.add(content);
        body.setOutputMarkupId(true);
    }
}
