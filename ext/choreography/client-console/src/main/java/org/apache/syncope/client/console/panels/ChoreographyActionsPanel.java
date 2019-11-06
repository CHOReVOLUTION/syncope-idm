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

import org.apache.syncope.client.console.wicket.markup.html.form.IndicatingOnConfirmAjaxLink;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.ajax.markup.html.IndicatingAjaxLink;
import org.apache.wicket.markup.html.panel.Fragment;
import org.apache.wicket.markup.html.panel.Panel;

public class ChoreographyActionsPanel extends Panel {

    private static final long serialVersionUID = 1939501639210931637L;

    public ChoreographyActionsPanel(final String componentId) {
        super(componentId);

        setOutputMarkupId(true);

        super.add(new Fragment("detailsPanel", "emptyFragment", this));
         super.add(new Fragment("enactPanel", "emptyFragment", this));
        super.add(new Fragment("startPanel", "emptyFragment", this));
        super.add(new Fragment("pausePanel", "emptyFragment", this));
        super.add(new Fragment("stopPanel", "emptyFragment", this));
        super.add(new Fragment("freezePanel", "emptyFragment", this));
        super.add(new Fragment("unfreezePanel", "emptyFragment", this));
        super.add(new Fragment("resizePanel", "emptyFragment", this));
        super.add(new Fragment("editPanel", "emptyFragment", this));
        super.add(new Fragment("deletePanel", "emptyFragment", this));
    }

    public ChoreographyActionsPanel addAction(
            final IndicatingAjaxLink<Void> link,
            final ChoreographyActionType choreographyActionType) {

        Fragment fragment = null;

        switch (choreographyActionType) {
            case SHOW:
                fragment = new Fragment("detailsPanel", "detailsFragment", this);
                fragment.addOrReplace(link.setVisible(true));
                break;

            case ENACT:
                fragment = new Fragment("enactPanel", "enactFragment", this);
                fragment.addOrReplace(link.setVisible(true));
                break;

            case START:
                fragment = new Fragment("startPanel", "startFragment", this);
                fragment.addOrReplace(link.setVisible(true));
                break;

            case PAUSE:
                fragment = new Fragment("pausePanel", "pauseFragment", this);
                fragment.addOrReplace(link.setVisible(true));
                break;

            case STOP:
                fragment = new Fragment("stopPanel", "stopFragment", this);
                fragment.addOrReplace(link.setVisible(true));
                break;

            case FREEZE:
                fragment = new Fragment("freezePanel", "freezeFragment", this);
                fragment.addOrReplace(link.setVisible(true));
                break;

            case UNFREEZE:
                fragment = new Fragment("unfreezePanel", "unfreezeFragment", this);
                fragment.addOrReplace(link.setVisible(true));
                break;

            case RESIZE:
                fragment = new Fragment("resizePanel", "resizeFragment", this);
                fragment.addOrReplace(link.setVisible(true));
                break;

            case EDIT:
                fragment = new Fragment("editPanel", "editFragment", this);
                fragment.addOrReplace(link.setVisible(true));
                break;

            case DELETE:
                fragment = new Fragment("deletePanel", "deleteFragment", this);
                fragment.addOrReplace(new IndicatingOnConfirmAjaxLink<Void>("link", true) {

                    private static final long serialVersionUID = -7978723352517770644L;

                    @Override
                    public void onClick(final AjaxRequestTarget target) {
                        link.onClick(target);
                    }
                });
                break;
            default:
            // No action
        }
        if (fragment != null) {
            super.addOrReplace(fragment);
        }
        return this;
    }

    public enum ChoreographyActionType {
        SHOW,
        ENACT,
        START,
        PAUSE,
        STOP,
        FREEZE,
        UNFREEZE,
        RESIZE,
        EDIT,
        DELETE
    }
}
