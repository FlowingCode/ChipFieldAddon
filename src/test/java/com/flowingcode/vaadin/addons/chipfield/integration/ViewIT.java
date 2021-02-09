package com.flowingcode.vaadin.addons.chipfield.integration;

import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.Test;

public class ViewIT extends AbstractViewTest {

	public ViewIT() {
		super("it");
	}

    @Test
	public void upgradedToCustomElement() {
		ChipFieldElement chipfield = $(ChipFieldElement.class).first();
		assertThat(chipfield, hasBeenUpgradedToCustomElement);
    }

}
