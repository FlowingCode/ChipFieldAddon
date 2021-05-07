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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

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
  private List<T> additionalItems = new ArrayList<>();
  private ItemLabelGenerator<T> itemLabelGenerator;
  private SerializableFunction<String, T> newItemHandler;

  public abstract static class ChipEvent<T> extends ComponentEvent<ChipField<T>> {

    private final String chipLabel;
    private final T item;

    private ChipEvent(ChipField<T> source, boolean fromClient, T item, String chipLabel) {
      super(source, fromClient);
      this.chipLabel = chipLabel;
      this.item = item;
    }

    public ChipEvent(ChipField<T> source, boolean fromClient, String chipLabel) {
      this(source, fromClient, source.findItemByLabel(chipLabel).orElse(null), chipLabel);
    }

    public String getChipLabel() {
      return chipLabel;
    }

    public T getItem() {
      return item;
    }
  }

  public static class ChipRemovedEvent<T> extends ChipEvent<T> {
    public ChipRemovedEvent(ChipField<T> source, boolean fromClient, T item) {
      super(source, fromClient, item, source.itemLabelGenerator.apply(item));
    }
  }

  public static class ChipCreatedEvent<T> extends ChipEvent<T> {
    public ChipCreatedEvent(ChipField<T> source, boolean fromClient, T item) {
      super(source, fromClient, item, source.itemLabelGenerator.apply(item));
    }
  }

  @DomEvent("chip-clicked")
  public static class ChipClickedEvent<T> extends ChipEvent<T> {
    public ChipClickedEvent(
        ChipField<T> source, boolean fromClient, @EventData(CHIP_LABEL) String chipLabel) {
      super(source, fromClient, chipLabel);
    }
  }

  @SafeVarargs
  public ChipField(String label, ItemLabelGenerator<T> itemLabelGenerator, T... availableItems) {
    super(new ArrayList<>());
    setChipLabelGenerator(itemLabelGenerator);
    setLabel(label);
    this.availableItems = DataProvider.ofCollection(Arrays.asList(availableItems));

    addValueChangeListener(
        ev -> {
          for (T t : ev.getOldValue()) {
            if (!ev.getValue().contains(t)) {
              fireEvent(new ChipRemovedEvent<>(this, ev.isFromClient(), t));
            }
          }

          for (T t : ev.getValue()) {
            if (!ev.getOldValue().contains(t)) {
              fireEvent(new ChipCreatedEvent<>(this, ev.isFromClient(), t));
            }
          }
        });
  }

  @SafeVarargs
  public ChipField(String label, T... availableItems) {
    this(label, Object::toString, availableItems);
  }

  private void configure() {
    configureItems();
    getElement()
        .addEventListener(
            "chip-created",
            e -> {
              JsonObject eventData = e.getEventData();
              String chipLabel = eventData.get(CHIP_LABEL).asString();
              T newItem =
                  findItemByLabel(chipLabel)
                      .orElseGet(
                          () -> {
                            if (isAllowAdditionalItems()) {
                              if (newItemHandler == null) {
                                setPresentationValue(getValue());
                                throw new IllegalStateException(
                                    "You need to setup a NewItemHandler");
                              }
                              return this.newItemHandler.apply(chipLabel);
                            } else {
                              setPresentationValue(getValue());
                              throw new IllegalStateException(
                                  "Adding new items is not allowed, but still receiving new items (not present in DataProvider) from client-side. Probably wrong configuration.");
                            }
                          });
              addSelectedItem(newItem, true);
            })
        .addEventData(CHIP_LABEL);

    getElement()
        .addEventListener(
            "chip-removed",
            e -> {
              JsonObject eventData = e.getEventData();
              String chipLabel = eventData.get(CHIP_LABEL).asString();
              findItemByLabel(chipLabel).ifPresent(item -> removeSelectedItem(item, true));
            })
        .addEventData(CHIP_LABEL);
    getElement().addEventListener("chip-clicked", e -> {}).addEventData(CHIP_LABEL);
  }

  private void configureItems() {
    Stream<T> streamItems = availableItems.fetch(new Query<>());
    JsonArray array = new JreJsonFactory().createArray();
    AtomicInteger index = new AtomicInteger(0);
    streamItems.forEach(
        item -> {
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
    setPresentationValue(getValue());
  }

  @Override
  public List<T> getValue() {
    return new ArrayList<>(super.getValue());
  }

  @Override
  protected void setPresentationValue(List<T> newPresentationValue) {
    List<String> labels = new ArrayList<>();

    if (isAllowAdditionalItems()) {
      for (T item : newPresentationValue) {
        String label = itemLabelGenerator.apply(item);
        if (!findItemByLabel(label).isPresent()) {
          addSelectedItemInternal(item, false);
          additionalItems.add(item);
        }
        labels.add(label);
      }
    } else {
      boolean hasChanges = false;
      newPresentationValue = new ArrayList<>(newPresentationValue);
      Iterator<T> it = newPresentationValue.iterator();
      while (it.hasNext()) {
        T item = it.next();
        String label = itemLabelGenerator.apply(item);
        if (findItemByLabel(label).isPresent()) {
          labels.add(label);
        } else {
          it.remove();
          hasChanges = true;
        }
      }
      if (hasChanges) {
        setModelValue(newPresentationValue, false);
      }
    }

    setClientChipWithoutEvent(labels.toArray(new String[labels.size()]));
  }

  private Optional<T> findItemByLabel(String label) {
    return Stream.concat(availableItems.fetch(new Query<>()), additionalItems.stream())
        .filter(item -> itemLabelGenerator.apply(item).equals(label))
        .findFirst();
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

  /** @deprecated use {@link #setEnabled(boolean)} */
  @Deprecated
  public void setDisabled(boolean disabled) {
    getElement().setProperty("disabled", disabled);
  }

  /** @deprecated use {@link #isEnabled()} */
  @Deprecated
  public boolean isDisabled() {
    return getElement().getProperty("disabled", false);
  }

  /** @deprecated use {@link #setReadOnly()} */
  @Deprecated
  public void setReadonly(boolean readonly) {
    setReadOnly(readonly);
  }

  /** @deprecated use {@link #isReadOnly()} */
  @Deprecated
  public boolean isReadonly() {
    return this.isReadonly();
  }

  /** @deprecated use {@link #setRequiredIndicatorVisible(boolean)} */
  @Deprecated
  public void setRequired(boolean required) {
    getElement().setProperty("required", required);
  }

  /** @deprecated use {@link #isRequiredIndicatorVisible()} */
  @Deprecated
  public boolean isRequired() {
    return isRequiredIndicatorVisible();
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

  @SuppressWarnings({"unchecked", "rawtypes"})
  public Registration addChipRemovedListener(ComponentEventListener<ChipRemovedEvent<T>> listener) {
    return addListener(ChipRemovedEvent.class, (ComponentEventListener) listener);
  }

  @SuppressWarnings({"unchecked", "rawtypes"})
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
    addSelectedItem(newItem, false);
  }

  private void addSelectedItem(T newItem, boolean fromClient) {
    String label = itemLabelGenerator.apply(newItem);
    if (isAllowAdditionalItems()) {
      addSelectedItemInternal(
          findItemByLabel(label)
              .orElseGet(
                  () -> {
                    additionalItems.add(newItem);
                    return newItem;
                  }),
          fromClient);
    } else {
      addSelectedItemInternal(
          findItemByLabel(label)
              .orElseThrow(
                  () ->
                      new UnsupportedOperationException(
                          "Cannot select item '"
                              + newItem
                              + "', because is not present in DataProvider, and adding new items is not permitted.")),
          fromClient);
    }
  }

  private void addSelectedItemInternal(T newItem, boolean fromClient) {
    List<T> value = getValue();
    if (!value.contains(newItem)) {
      value = new ArrayList<>(value);
      value.add(newItem);
      setModelValue(value, fromClient);
      if (!fromClient) {
        setPresentationValue(value);
      }
    }
  }

  public void removeSelectedItem(T itemToRemove) {
    removeSelectedItem(itemToRemove, false);
  }

  private void removeSelectedItem(T itemToRemove, boolean fromClient) {
    List<T> value = new ArrayList<>(getValue());
    if (value.remove(itemToRemove)) {
      additionalItems.retainAll(value);
      setModelValue(value, fromClient);
      if (!fromClient) {
        setPresentationValue(value);
      }
    }
  }

  @SuppressWarnings({"unchecked", "rawtypes"})
  public Registration addChipClickedListener(ComponentEventListener<ChipClickedEvent<T>> listener) {
    return addListener(ChipClickedEvent.class, (ComponentEventListener) listener);
  }
}
