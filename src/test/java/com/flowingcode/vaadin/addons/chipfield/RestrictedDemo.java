package com.flowingcode.vaadin.addons.chipfield;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import java.util.Arrays;

@SuppressWarnings("serial")
public class RestrictedDemo extends VerticalLayout {
	public RestrictedDemo() {
		ChipField<String> chf = new ChipField<>("Select some planets (Restricted input, allowed pattern [a-zA-Z])");
		chf.setWidthFull();
		chf.setAvailableItems(
				Arrays.asList("Mercury", "Venus", "Earth", "Mars", "Jupiter", "Saturn", "Uranus", "Neptune"));
		chf.setAllowedPattern("[a-zA-Z]");
		chf.setClosable(true);

		add(chf);
	}
}
