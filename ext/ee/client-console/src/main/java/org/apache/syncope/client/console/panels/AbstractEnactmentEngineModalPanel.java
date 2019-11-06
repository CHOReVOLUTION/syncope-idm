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

import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import org.apache.syncope.client.console.rest.EnactmentEngineRestClient;
import org.apache.syncope.client.console.wicket.markup.html.bootstrap.dialog.BaseModal;
import org.apache.syncope.client.console.wicket.markup.html.form.AjaxTextFieldPanel;
import org.apache.syncope.client.console.wicket.markup.html.form.EncryptedFieldPanel;
import org.apache.syncope.client.console.wicket.markup.html.form.FieldPanel;
import org.apache.wicket.PageReference;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.Model;

public abstract class AbstractEnactmentEngineModalPanel extends AbstractModalPanel<Serializable> {

    private static final long serialVersionUID = 1570261203472359825L;

    protected final EnactmentEngineRestClient restClient;

    protected final FieldPanel<String> nameInput;

    protected final FieldPanel<String> usernameInput;

    protected final FieldPanel<String> passwordInput;

    protected final FieldPanel<String> urlInput;

    public AbstractEnactmentEngineModalPanel(
            final BaseModal<Serializable> modal,
            final PageReference pageRef) {
        super(modal, pageRef);

        this.restClient = new EnactmentEngineRestClient();

        final WebMarkupContainer container = new WebMarkupContainer("container");
        container.setOutputMarkupId(true);
        add(container);

        final Form<String> form = new Form<>("AddEEForm");
        form.setMarkupId("AddEEForm");
        form.setOutputMarkupId(true);
        container.add(form);

        this.nameInput = new AjaxTextFieldPanel("name", "Name", new Model<>(), true);
        nameInput.setRequired(true);
        nameInput.addRequiredLabel();
        form.add(nameInput);

        this.usernameInput = new AjaxTextFieldPanel("username", "Username", new Model<>(), true);
        usernameInput.setRequired(true);
        usernameInput.addRequiredLabel();
        form.add(usernameInput);

        this.passwordInput = new EncryptedFieldPanel("password", "Password", new Model<>(), true);
        passwordInput.setRequired(true);
        passwordInput.addRequiredLabel();
        form.add(passwordInput);

        this.urlInput = new AjaxTextFieldPanel("url", "Base URL", new Model<>(), true);
        urlInput.setRequired(true);
        urlInput.addRequiredLabel();
        form.add(urlInput);
    }

    protected void checkURL(final String s) throws MalformedURLException {
        URL url = new URL(s);
    }

}
