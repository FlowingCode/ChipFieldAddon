/*-
 * #%L
 * ChipField Addon
 * %%
 * Copyright (C) 2018 - 2026 Flowing Code
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

import com.flowingcode.vaadin.addons.demo.DemoSource;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route(value = "chipfield/no-label-float", layout = ChipfieldDemoView.class)
@PageTitle("No Label Float")
@DemoSource
@SuppressWarnings("serial")
public class NoLabelFloatDemo extends VerticalLayout {

  public NoLabelFloatDemo() {
    // by default, space is reserved above the input for the floating label,
    // which misaligns the field from the label of the enclosing form item
    ChipField<String> floating = newChipField();

    // when the label does not float, no space is reserved for it above the input
    ChipField<String> notFloating = newChipField();
    notFloating.setNoLabelFloat(true);

    FormLayout formLayout = new FormLayout();
    formLayout.setResponsiveSteps(new FormLayout.ResponsiveStep("0", 1));
    formLayout.setWidth("500px");
    formLayout.addFormItem(floating, "Floating label");
    formLayout.addFormItem(notFloating, "No label float");

    add(formLayout);
  }

  private static ChipField<String> newChipField() {
    ChipField<String> chf = new ChipField<>("", "Mercury", "Venus", "Earth", "Mars", "Jupiter");
    chf.addSelectedItem("Earth");
    chf.setClosable(true);
    chf.setWidthFull();
    return chf;
  }
}
