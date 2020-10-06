package com.flowingcode.vaadin.addons.chipfield;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import java.util.Arrays;

@SuppressWarnings("serial")
public class RestrictedDemo extends VerticalLayout {
	public RestrictedDemo() {
		ChipField<String> chf3 = new ChipField<>("Select some planets (Restricted input, allowed pattern [a-zA-Z])");
		chf3.setWidth("500px");
		chf3.setAvailableItems(
				Arrays.asList("Mercury", "Venus", "Earth", "Mars", "Jupiter", "Saturn", "Uranus", "Neptune"));
		chf3.setAllowedPattern("[a-zA-Z]");
		chf3.setClosable(true);

		add(chf3);
	}
}
