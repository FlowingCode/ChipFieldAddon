package com.flowingcode.vaadin.addons.chipfield.integration;

import java.util.Arrays;

import com.flowingcode.vaadin.addons.chipfield.ChipField;
import com.flowingcode.vaadin.addons.chipfield.integration.rpc.JsonArrayList;
import com.vaadin.flow.component.ClientCallable;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.router.Route;

import elemental.json.Json;

@Route("it")
public class IntegrationView extends Div implements IntegrationViewCallables {

	public ChipField<String> field;

	public IntegrationView() {
		setId("view");
		add(field = new ChipField<>("Field"));
		field.setItems("Lorem", "Ipsum");
	}

	@Override
	@ClientCallable
	public void testCallable(boolean arg) {
		if (!arg) {
			throw new IllegalArgumentException();
		}
	}

	@Override
	@ClientCallable
	public void allowAdditionalItems(boolean value) {
		field.setAllowAdditionalItems(value);
	}

	@Override
	@ClientCallable
	public void setFieldReadOnly(boolean value) {
		field.setReadOnly(value);
	}

	@Override
	@ClientCallable
	public void setFieldEnabled(boolean value) {
		field.setEnabled(value);
	}

	@Override
	@ClientCallable
	public void setValue(String... items) {
		field.setValue(Arrays.asList(items));
	}

	@Override
	@ClientCallable
	public void useNewItemHandler(boolean useHandler) {
		field.setNewItemHandler(useHandler ? Object::toString : null);
	}

	@ClientCallable
	public void assertValue(String... items) {
		if (!field.getValue().equals(Arrays.asList(items))) {
			throw new AssertionError();
		}
	}

	@Override
	@ClientCallable
	public JsonArrayList<String> getValue() {
		return JsonArrayList.createArray(field.getValue(), Json::create);
	}

}
