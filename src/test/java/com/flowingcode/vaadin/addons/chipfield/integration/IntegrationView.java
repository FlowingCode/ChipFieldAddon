/*-
 * #%L
 * ChipField Addon
 * %%
 * Copyright (C) 2018 - 2021 Flowing Code
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package com.flowingcode.vaadin.addons.chipfield.integration;

import com.flowingcode.vaadin.addons.chipfield.ChipField;
import com.flowingcode.vaadin.addons.chipfield.integration.rpc.JsonArrayList;
import com.vaadin.flow.component.ClientCallable;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.router.Route;
import elemental.json.Json;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Route("it")
public class IntegrationView extends Div implements IntegrationViewCallables {

	public ChipField<String> field;
	private List<String> lastValueChange;
	private String lastClickedItem;
	private String lastRemovedItem;
	private String lastCreatedItem;

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
	public void setClosable(boolean closable) {
		field.setClosable(closable);
	}

	@Override
	@ClientCallable
	public boolean isClosable() {
		return field.isClosable();
	}

	@Override
	@ClientCallable
	public void removeSelectedItem(String itemToRemove) {
		field.removeSelectedItem(itemToRemove);
	}

	@Override
	@ClientCallable
	public void setAllowedPattern(String pattern) {
		field.setAllowedPattern(pattern);
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
	    lastClickedItem=null;
	    lastRemovedItem=null;
	    lastCreatedItem=null;
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

	@Override
	@ClientCallable
	public boolean hasItemWithLabel(String label) {
		try {
			Method method = ChipField.class.getDeclaredMethod("findItemByLabel", String.class);
			method.setAccessible(true);
			return ((Optional<?>) method.invoke(field, label)).isPresent();
		} catch (Exception e) {
			throw new RuntimeException();
		}
	}

	@Override
	@ClientCallable
	public void addValueChangeListener() {
		field.addValueChangeListener(ev -> {
			lastValueChange = ev.getValue();
		});
	}

	@Override
	@ClientCallable
	public void addItemRemovedListener() {
		field.addChipRemovedListener(ev -> {
			lastRemovedItem = ev.getItem();
		});
	}

	@Override
	@ClientCallable
	public void addItemClickedListener() {
		field.addChipClickedListener(ev -> {
			lastClickedItem = ev.getItem();
		});
	}

	@Override
	@ClientCallable
	public void addItemCreatedListener() {
		field.addChipCreatedListener(ev -> {
			lastCreatedItem = ev.getItem();
		});
	}

	@Override
	@ClientCallable
	public JsonArrayList<String> getLastValueChange() {
		return JsonArrayList.fromStringArray(lastValueChange);
	}

	@Override
	@ClientCallable
	public String getLastClickedItem() {
		return lastClickedItem;
	}

	@Override
	@ClientCallable
	public String getLastRemovedItem() {
		return lastRemovedItem;
	}

	@Override
	@ClientCallable
	public String getLastCreatedItem() {
		return lastCreatedItem;
	}

}