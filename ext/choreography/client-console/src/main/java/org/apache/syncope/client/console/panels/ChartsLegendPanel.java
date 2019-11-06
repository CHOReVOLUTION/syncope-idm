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

import org.apache.wicket.markup.html.panel.Fragment;
import org.apache.wicket.markup.html.panel.Panel;

public class ChartsLegendPanel extends Panel {

    private static final long serialVersionUID = -5697479237359450846L;

    public ChartsLegendPanel(final String id) {
        super(id);

        setOutputMarkupId(true);

        super.add(new Fragment("cdOverallTimesPanel", "emptyFragment", this));
        super.add(new Fragment("cdDetailTimesPanel", "emptyFragment", this));
        super.add(new Fragment("operationsPanel", "emptyFragment", this));
        super.add(new Fragment("servicePanel", "emptyFragment", this));
    }

        public ChartsLegendPanel addLegendGroup(
            final ChartsLegendType chartsLegendType) {

        Fragment fragment = null;

        switch (chartsLegendType) {
            case INSTANCE_DETAIL:
                fragment = new Fragment("cdOverallTimesPanel", "cdOverallTimesFragment", this);
                super.addOrReplace(new Fragment("operationsPanel", "operationsFragment", this));
                break;

            case CD_DETAIL:
                fragment = new Fragment("cdDetailTimesPanel", "cdDetailTimesFragment", this);

                break;

            case SERVICE_DETAIL:
                fragment = new Fragment("servicePanel", "serviceFragment", this);

                break;

            default:
                // No action
        }
        if (fragment != null) {
            super.addOrReplace(fragment);
        }
        return this;
    }

    public enum ChartsLegendType {
        INSTANCE_DETAIL,
        CD_DETAIL,
        SERVICE_DETAIL,
    }

}
