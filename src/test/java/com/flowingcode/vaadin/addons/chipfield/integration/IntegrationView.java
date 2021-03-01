package com.flowingcode.vaadin.addons.chipfield.integration;

import java.util.Arrays;

import com.flowingcode.vaadin.addons.chipfield.ChipField;
import com.vaadin.flow.component.ClientCallable;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.router.Route;

@Route("it")
public class IntegrationView extends Div {

	private ChipField<String> field;

	public IntegrationView() {
		setId("view");
		add(field = new ChipField<>("Field"));
		field.setItems("Lorem", "Ipsum");
	}

	@ClientCallable
	private void testCallable(boolean arg) {
		if (!arg) {
			throw new IllegalArgumentException();
		}
	}

}
