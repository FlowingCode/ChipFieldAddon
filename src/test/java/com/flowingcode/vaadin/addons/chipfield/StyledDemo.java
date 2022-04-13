/*-
 * #%L
 * ChipField Addon
 * %%
 * Copyright (C) 2018 - 2022 Flowing Code
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

import java.util.Arrays;
import com.flowingcode.vaadin.addons.demo.DemoSource;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;

@PageTitle("Styled")
@DemoSource
@CssImport(value = "./styles/paper-chip-styles.css", themeFor = "paper-chip")
@CssImport(value = "./styles/paper-chip-input-autocomplete-styles.css", themeFor = "paper-chip-input-autocomplete")
public class StyledDemo extends VerticalLayout {

  public StyledDemo() {
    ChipField<String> chf =
        new ChipField<>("Select some planets (Allowed pattern [a-zA-Z])");
    chf.setWidth("90%");
    chf.setAvailableItems(
        Arrays.asList(
            "Mercury", "Venus", "Earth", "Mars", "Jupiter", "Saturn", "Uranus", "Neptune"));
    chf.setAllowedPattern("[a-zA-Z]");
    chf.addSelectedItem("Mercury");
    chf.addSelectedItem("Venus");
    chf.setClosable(true);
    
    chf.setThemeName("styled-demo");
    
    add(chf);
  }
  
}
