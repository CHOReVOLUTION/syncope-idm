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

import java.util.ArrayList;
import java.util.List;
import org.apache.syncope.client.console.pages.ChoreographyPage;
import org.apache.syncope.client.console.wicket.extensions.markup.html.repeater.data.table.AttrColumn;
import org.apache.syncope.common.lib.search.AnyObjectFiqlSearchConditionBuilder;
import org.apache.syncope.common.lib.to.AnyObjectTO;
import org.apache.syncope.common.lib.types.AnyTypeKind;
import org.apache.syncope.common.lib.types.SchemaType;
import org.apache.wicket.PageReference;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.ajax.markup.html.IndicatingAjaxLink;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.extensions.markup.html.repeater.data.table.AbstractColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;
import org.springframework.util.ReflectionUtils;

public class ChoreographyServiceDirectoryPanel extends AnyObjectDirectoryPanel {

    private static final long serialVersionUID = 7303610381539502741L;

    public ChoreographyServiceDirectoryPanel(final String id,
            final PageReference pageRef,
            final String choreographyKey) {
        super(id, new Builder(AnyTypeKind.ANY_OBJECT.name(), pageRef), false);
        this.fiql = new AnyObjectFiqlSearchConditionBuilder("SERVICE")
                .inGroups(choreographyKey).query();
        dataProvider.setFIQL(fiql);
    }

    @Override
    protected String paginatorRowsKey() {
        return ChoreographyPage.PREF_CHOREOGRAPHY_PAGINATOR_ROWS;
    }

    @Override
    protected List<IColumn<AnyObjectTO, String>> getColumns() {
        final List<IColumn<AnyObjectTO, String>> columns = new ArrayList<>();

        addPropertyColumn("key", ReflectionUtils.findField(AnyObjectTO.class, "key"), columns);
        addPropertyColumn("name", ReflectionUtils.findField(AnyObjectTO.class, "name"), columns);

        columns.add(new AttrColumn<>("Service Location", SchemaType.PLAIN));

        columns.add(new AbstractColumn<AnyObjectTO, String>(new ResourceModel("actions", "")) {

            private static final long serialVersionUID = -5137819175810948915L;

            @Override
            public String getCssClass() {
                return "action";
            }

            @Override
            public void populateItem(
                    final Item<ICellPopulator<AnyObjectTO>> item,
                    final String componentId,
                    final IModel<AnyObjectTO> model) {

                ChoreographyActionsPanel choreographyActionsPanel = new ChoreographyActionsPanel(componentId);

                choreographyActionsPanel.addAction(new IndicatingAjaxLink<Void>("link") {

                    private static final long serialVersionUID = 4787462946145290675L;

                    @Override
                    public void onClick(final AjaxRequestTarget target) {

                    }
                }, ChoreographyActionsPanel.ChoreographyActionType.SHOW);
                item.add(choreographyActionsPanel);
            }
        });

        return columns;
    }

    public static class Builder extends AnyObjectDirectoryPanel.Builder {

        private static final long serialVersionUID = 1903764789236792302L;

        public Builder(final String type, final PageReference pageRef) {
            super(new ArrayList<>(), type, pageRef);
            setFiltered(true);
            setShowResultPage(true);
        }
    }
}
