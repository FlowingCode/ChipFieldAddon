package com.flowingcode.vaadin.addons.chipfield;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;

@SuppressWarnings("serial")
public class DisabledDemo extends VerticalLayout {
	public DisabledDemo() {
		ChipField<String> chf = new ChipField<>("Disabled", "Mercury", "Venus", "Earth");
		chf.setWidthFull();
		chf.addSelectedItem("Mercury");
		chf.addSelectedItem("Venus");
		chf.setDisabled(true);

		add(chf);
	}
}
