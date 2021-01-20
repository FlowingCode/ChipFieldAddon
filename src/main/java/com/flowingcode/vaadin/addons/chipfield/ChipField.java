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
package com.flowingcode.vaadin.addons.chipfield;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.DomEvent;
import com.vaadin.flow.component.EventData;
import com.vaadin.flow.component.HasSize;
import com.vaadin.flow.component.HasStyle;
import com.vaadin.flow.component.ItemLabelGenerator;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.JavaScript;
import com.vaadin.flow.component.dependency.NpmPackage;
import com.vaadin.flow.data.binder.HasDataProvider;
import com.vaadin.flow.data.binder.HasItemsAndComponents;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.provider.Query;
import com.vaadin.flow.function.SerializableFunction;
import com.vaadin.flow.shared.Registration;

import elemental.json.JsonArray;
import elemental.json.JsonObject;
import elemental.json.impl.JreJsonFactory;

@SuppressWarnings("serial")
@Tag("paper-chip-input-autocomplete")
@NpmPackage(value = "@polymer/iron-a11y-keys", version = "^3.0.1")
@NpmPackage(value = "@polymer/iron-a11y-keys-behavior", version = "^3.0.1")
@NpmPackage(value = "@polymer/iron-icons", version = "^3.0.1")
@NpmPackage(value = "@polymer/paper-icon-button", version = "^3.0.2")
@NpmPackage(value = "@polymer/paper-input", version = "^3.0.1")
@NpmPackage(value = "@polymer/paper-item", version = "^3.0.1")
@NpmPackage(value = "@polymer/paper-listbox", version = "^3.0.1")
@NpmPackage(value = "@polymer/paper-material", version = "^3.0.1")
@NpmPackage(value = "@polymer/paper-ripple", version = "^3.0.1")
@NpmPackage(value = "@polymer/paper-styles", version = "^3.0.1")
@JavaScript("./paper-chip-input-autocomplete.js")
public class ChipField<T> extends AbstractField<ChipField<T>, List<T>>
		implements HasStyle, HasItemsAndComponents<T>, HasDataProvider<T>, HasSize {

	public static final String CHIP_LABEL = "event.detail.chipLabel";

	private DataProvider<T, ?> availableItems = DataProvider.ofCollection(new ArrayList<T>());
	private ItemLabelGenerator<T> itemLabelGenerator;
	private SerializableFunction<String, T> newItemHandler;

	public abstract static class ChipEvent<T> extends ComponentEvent<ChipField<T>> {

		private final String chipLabel;
		private final T item;

		public ChipEvent(ChipField<T> source, boolean fromClient, String chipLabel) {
			super(source, fromClient);
			this.chipLabel = chipLabel;
			this.item = source.findItemByLabel(chipLabel).orElse(null);
		}

		public String getChipLabel() {
			return chipLabel;
		}

		public T getItem() {
			return item;
		}

	}

	@DomEvent("chip-removed")
	public static class ChipRemovedEvent<T> extends ChipEvent<T> {
		public ChipRemovedEvent(ChipField<T> source, boolean fromClient, @EventData(CHIP_LABEL) String chipLabel) {
			super(source, fromClient, chipLabel);
		}
	}

	@DomEvent("chip-created")
	public static class ChipCreatedEvent<T> extends ChipEvent<T> {
		public ChipCreatedEvent(ChipField<T> source, boolean fromClient, @EventData(CHIP_LABEL) String chipLabel) {
			super(source, fromClient, chipLabel);
		}
	}

	@DomEvent("chip-clicked")
	public static class ChipClickedEvent<T> extends ChipEvent<T> {
		public ChipClickedEvent(ChipField<T> source, boolean fromClient, @EventData(CHIP_LABEL) String chipLabel) {
			super(source, fromClient, chipLabel);
		}
	}

	@SafeVarargs
	public ChipField(String label, ItemLabelGenerator<T> itemLabelGenerator, T... availableItems) {
		super(new ArrayList<>());
		setChipLabelGenerator(itemLabelGenerator);
		setLabel(label);
		this.availableItems = DataProvider.ofCollection(Arrays.asList(availableItems));
	}

	@SafeVarargs
	public ChipField(String label, T... availableItems) {
		this(label, Object::toString, availableItems);
	}

	private void configure() {
		configureItems();
		getElement().addEventListener("chip-created", e -> {
			JsonObject eventData = e.getEventData();
			String chipLabel = eventData.get(CHIP_LABEL).asString();
			T newItem = findItemByLabel(chipLabel).orElseGet(() -> {
				if (isAllowAdditionalItems()) {
					if (newItemHandler == null) {
						throw new IllegalStateException("You need to setup a NewItemHandler");
					}
					return this.newItemHandler.apply(chipLabel);
				} else {
					throw new IllegalStateException(
							"Adding new items is not allowed, but still receiving new items (not present in DataProvider) from client-side. Probably wrong configuration.");
				}
			});
			addSelectedItem(newItem, true);
		}).addEventData(CHIP_LABEL);

		getElement().addEventListener("chip-removed", e -> {
			JsonObject eventData = e.getEventData();
			String chipLabel = eventData.get(CHIP_LABEL).asString();
			findItemByLabel(chipLabel).ifPresent(item -> removeSelectedItem(item, true));
		}).addEventData(CHIP_LABEL);
		getElement().addEventListener("chip-clicked", e -> {
		}).addEventData(CHIP_LABEL);
	}

	private void configureItems() {
		Stream<T> streamItems = availableItems.fetch(new Query<>());
		JsonArray array = new JreJsonFactory().createArray();
		AtomicInteger index = new AtomicInteger(0);
		streamItems.forEach(item -> {
			JsonObject object = new JreJsonFactory().createObject();
			object.put("text", itemLabelGenerator.apply(item));
			object.put("value", itemLabelGenerator.apply(item));
			array.set(index.getAndIncrement(), object);
		});
		getElement().setPropertyJson("source", array);
	}

	@Override
	protected void onAttach(AttachEvent attachEvent) {
		configure();
	}

	@Override
	protected void setPresentationValue(List<T> newPresentationValue) {
		setClientChipWithoutEvent(newPresentationValue.stream().map(itemLabelGenerator).toArray(String[]::new));
	}

	private Optional<T> findItemByLabel(String label) {
		return availableItems.fetch(new Query<>()).filter(item -> itemLabelGenerator.apply(item).equals(label)).findFirst();
	}

	private void setClientChipWithoutEvent(String[] labels) {
		getElement().executeJs("this.splice('items', 0, this.items.length);");
		for (String label : labels) {
			getElement().executeJs("this.push('items', $0);", label);
		}
		getElement().executeJs("this.required = false; this.autoValidate = false; this._value = '';");
	}

	public void setAvailableItems(List<T> items) {
		this.availableItems = DataProvider.ofCollection(items);
	}

	public String getLabel() {
		return getElement().getProperty("label");
	}

	public void setLabel(String label) {
		getElement().setProperty("label", label);
	}

	public String[] getChipsAsStrings() {
		return super.getValue().stream().map(itemLabelGenerator).toArray(String[]::new);
	}

	public void setClosable(boolean closable) {
		getElement().setProperty("closable", closable);
	}

	public boolean isClosable() {
		return getElement().getProperty("closable", false);
	}

	public void setDisabled(boolean disabled) {
		getElement().setProperty("disabled", disabled);
	}

	public boolean isDisabled() {
		return getElement().getProperty("disabled", false);
	}

	public void setReadonly(boolean readonly) {
		getElement().setProperty("readonly", readonly);
	}

	public boolean isReadonly() {
		return getElement().getProperty("readonly", false);
	}

	public void setRequired(boolean required) {
		getElement().setProperty("required", required);
	}

	public boolean isRequired() {
		return getElement().getProperty("required", false);
	}

	public void setValidationPattern(String pattern) {
		getElement().setProperty("pattern", pattern);
	}

	public String getValidationPattern() {
		return getElement().getProperty("pattern");
	}

	public void setValidationErrorMessage(String errorMessage) {
		getElement().setProperty("errorMessage", errorMessage);
	}

	public String getValidationErrorMessage() {
		return getElement().getProperty("errorMessage");
	}

	public void setAllowedPattern(String pattern) {
		getElement().setProperty("allowedPattern", pattern);
	}

	public String getAllowedPattern() {
		return getElement().getProperty("allowedPattern");
	}

	public void setAllowAdditionalItems(boolean allowAdditionalItems) {
		getElement().setProperty("additionalItems", allowAdditionalItems);
	}

	public boolean isAllowAdditionalItems() {
		return getElement().getProperty("additionalItems", false);
	}

	public void validate() {
		getElement().callJsFunction("validate");
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Registration addChipRemovedListener(ComponentEventListener<ChipRemovedEvent<T>> listener) {
		return addListener(ChipRemovedEvent.class, (ComponentEventListener) listener);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Registration addChipCreatedListener(ComponentEventListener<ChipCreatedEvent<T>> listener) {
		return addListener(ChipCreatedEvent.class, (ComponentEventListener) listener);
	}

	public void setChipLabelGenerator(ItemLabelGenerator<T> itemLabelGenerator) {
		this.itemLabelGenerator = itemLabelGenerator;
	}

	public void setNewItemHandler(SerializableFunction<String, T> handler) {
		this.newItemHandler = handler;
		this.setAllowAdditionalItems(true);
	}

	@Override
	public void setDataProvider(DataProvider<T, ?> dataProvider) {
		this.availableItems = dataProvider;
	}

	public void addSelectedItem(T newItem) {
		String label = itemLabelGenerator.apply(newItem);
		if (isAllowAdditionalItems()) {
			addSelectedItem(findItemByLabel(label).orElse(newItem), false);
		} else {
			addSelectedItem(findItemByLabel(label).orElseThrow(() -> new UnsupportedOperationException(
					"Cannot select item '" + newItem + "', because is not present in DataProvider, and adding new items is not permitted.")), false);
		}
	}

	private void addSelectedItem(T newItem, boolean fromClient) {
		List<T> value = getValue();
		if (!value.contains(newItem)) {
			value = new ArrayList<>(value);
			value.add(newItem);
			setModelValue(value, fromClient);
			if (!fromClient) {
				setPresentationValue(value);
				fireEvent(new ChipCreatedEvent<>(this, fromClient, itemLabelGenerator.apply(newItem)));
			}
		}
	}

	public void removeSelectedItem(T itemToRemove) {
		removeSelectedItem(itemToRemove, false);
	}

	private void removeSelectedItem(T itemToRemove, boolean fromClient) {
		List<T> value = new ArrayList<>(getValue());
		if (value.remove(itemToRemove)) {
			setModelValue(value, fromClient);
			if (!fromClient) {
				setPresentationValue(value);
				fireEvent(new ChipRemovedEvent<>(this, fromClient, itemLabelGenerator.apply(itemToRemove)));
			}
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Registration addChipClickedListener(ComponentEventListener<ChipClickedEvent<T>> listener) {
		return addListener(ChipClickedEvent.class, (ComponentEventListener) listener);
	}

}
