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
package com.flowingcode.vaadin.addons.chipfield.integration;

import com.flowingcode.vaadin.addons.chipfield.ChipField;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.router.Route;
import java.util.Arrays;

/** Test class for fix for issue https://github.com/FlowingCode/ChipFieldAddon/issues/33 */
@Route("remove-add-test")
public class RemoveAndReAddTestView extends Div {

  public RemoveAndReAddTestView() {
    super.getElement().getStyle().set("padding", "5px");

    setId("remove-add-test");

    ChipField<String> chf = new ChipField<>("Planets", "Mercury", "Venus", "Earth");
    chf.setId("chipfield");
    chf.setWidthFull();

    chf.addSelectedItem("Mercury");
    chf.addSelectedItem("Venus");
    chf.setValue(Arrays.asList("Mercury", "Venus"));

    Button toggle =
        new Button(
            "Toggle chipfield",
            e -> {
              if (getChildren().anyMatch(chf::equals)) {
                remove(chf);
              } else {
                add(chf);
              }
            });

    toggle.setId("toggle");

    add(toggle, chf);
  }
}
