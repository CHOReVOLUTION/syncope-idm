/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.syncope.client.console.widgets;

import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;

public class ResourceWidget extends BaseWidget {

    private static final long serialVersionUID = -816175678514035085L;

    public ResourceWidget(
            final String id,
            final String bgColor,
            final int usageResource,
            final int totalResource,
            final String label,
            final double percentageUsage,
            final String unit,
            final String icon) {

        super(id);
        setOutputMarkupId(true);

        WebMarkupContainer box = new WebMarkupContainer("box");
        add(box);

        box.add(new AttributeAppender("class", " " + bgColor));

        box.add(new Label("usageResource", usageResource));

        if (totalResource != 0) {
           box.add(new Label("totalResource", "/" + totalResource)); 
        } else {
            box.add(new Label("totalResource", "")); 
        }

        if (unit != null && !unit.equals("")) {
            box.add(new Label("unit", unit));
        } else {
            box.add(new Label("unit", ""));
        }

        box.add(new Label("label", label));

        WebMarkupContainer progress = new WebMarkupContainer("progress");
        progress.add(new AttributeAppender("style", " width: " + percentageUsage + "%"));
        box.add(progress);

        Label iconLabel = new Label("icon");
        iconLabel.add(new AttributeAppender("class", icon));
        box.add(iconLabel);
    }

    public ResourceWidget(
            final String id,
            final String bgColor,
            final int usageResource,
            final String label,
            final String unit,
            final String icon) {
        this(id, bgColor, usageResource, 0, label, 0, unit, icon);
    }

    public ResourceWidget(
            final String id,
            final String bgColor,
            final int usageResource,
            final String label,
            final String icon) {
        this(id, bgColor, usageResource, label, "", icon);
    }

    public ResourceWidget(
            final String id,
            final String bgColor,
            final String label,
            final double percentageUsage,
            final String icon) {
        this(id, bgColor, (int) Math.floor(percentageUsage), 0, label, percentageUsage, "%", icon);
    }
}
