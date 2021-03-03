package com.flowingcode.vaadin.addons.chipfield.integration;

import com.flowingcode.vaadin.addons.chipfield.integration.rpc.JsonArrayList;

public interface IntegrationViewCallables {

	void testCallable(boolean arg);

	void allowAdditionalItems(boolean value);

	void setFieldReadOnly(boolean value);

	void setFieldEnabled(boolean value);

	void setValue(String... items);

	void useNewItemHandler(boolean useHandler);

	JsonArrayList<String> getValue();

}
