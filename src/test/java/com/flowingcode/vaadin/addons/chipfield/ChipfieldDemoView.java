/*-
 * #%L
 * ChipField Addon
 * %%
 * Copyright (C) 2018 - 2021 Flowing Code
 * %%
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
 * #L%
 */
package com.flowingcode.vaadin.addons.chipfield;

import com.flowingcode.vaadin.addons.DemoLayout;
import com.flowingcode.vaadin.addons.GithubLink;
import com.flowingcode.vaadin.addons.demo.TabbedDemo;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.router.Route;

@SuppressWarnings("serial")
@StyleSheet("context://frontend/styles/demo-styles.css")
@Route(value = "chipfield", layout = DemoLayout.class)
@GithubLink("https://github.com/FlowingCode/ChipFieldAddon")
public class ChipfieldDemoView extends TabbedDemo {

   public ChipfieldDemoView() {
    addDemo(new DataProviderDemo());
    addDemo(new RestrictedDemo());
    addDemo(new DisabledDemo());
    addDemo(new BinderDemo());
    addDemo(new ReadonlyDemo());
    addDemo(new StyledDemo());
  }
}
