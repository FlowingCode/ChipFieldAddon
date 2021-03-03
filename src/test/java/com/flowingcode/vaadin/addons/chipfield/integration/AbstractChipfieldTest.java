package com.flowingcode.vaadin.addons.chipfield.integration;

import com.flowingcode.vaadin.addons.chipfield.integration.rpc.HasRpcSupport;

public abstract class AbstractChipfieldTest extends AbstractViewTest implements HasRpcSupport {

	protected ChipFieldElement chipfield;

	protected AbstractChipfieldTest() {
		super("it");
	}

	@Override
	public void setup() throws Exception {
		super.setup();
		chipfield = $(ChipFieldElement.class).waitForFirst();
	}

}
