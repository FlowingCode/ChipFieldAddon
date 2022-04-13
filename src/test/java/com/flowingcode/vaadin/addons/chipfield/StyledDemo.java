package com.flowingcode.vaadin.addons.chipfield;

import java.util.Arrays;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

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
