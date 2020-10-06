package com.flowingcode.vaadin.addons.chipfield;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;

@SuppressWarnings("serial")
public class DisabledDemo extends VerticalLayout {
	public DisabledDemo() {
		ChipField<String> chf4 = new ChipField<>("Disabled", "Mercury", "Venus", "Earth");
		chf4.addSelectedItem("Mercury");
		chf4.addSelectedItem("Venus");
		chf4.setDisabled(true);

		add(chf4);
	}
}
