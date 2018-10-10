package com.flowingcode.vaadin.addons;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.DomEvent;
import com.vaadin.flow.component.EventData;
import com.vaadin.flow.component.HasStyle;
import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.component.ItemLabelGenerator;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.data.binder.HasDataProvider;
import com.vaadin.flow.data.binder.HasItemsAndComponents;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.provider.Query;
import com.vaadin.flow.shared.Registration;

import elemental.json.JsonObject;
import elemental.json.impl.JreJsonArray;
import elemental.json.impl.JreJsonFactory;

@SuppressWarnings("serial")
@Tag("paper-chip-input-autocomplete")
@HtmlImport("bower_components/paper-chip/paper-chip-input-autocomplete.html")
public class ChipField<T> extends AbstractField<ChipField<T>, List<T>>
implements HasStyle, HasItemsAndComponents<T>, HasDataProvider<T> {

	private DataProvider<T, ?> availableItems = DataProvider.ofCollection(new ArrayList<T>());
//	private List<T> selectedItems = new ArrayList<>();
	private ItemLabelGenerator<T> itemLabelGenerator;
	private Function<String,T> newItemHandler;

	@SafeVarargs
	public ChipField(String label, ItemLabelGenerator<T> itemLabelGenerator, T... availableItems) {
		super(new ArrayList<>());
		setChipLabelGenerator(itemLabelGenerator);
		setLabel(label);
		this.availableItems = DataProvider.ofCollection(Arrays.asList(availableItems));
	}

	@SafeVarargs
	public ChipField(String label, T... availableItems) {
		this(label,item->item.toString(),availableItems);
	}

	private void configure() {
		configureItems();
		getElement().addEventListener("chip-created", e->{
			JsonObject eventData = e.getEventData();
			String chipLabel = eventData.get("event.detail.chipLabel").asString();
			Stream<T> streamItems = availableItems.fetch(new Query());
			Optional<T> newItem = streamItems.filter(item->itemLabelGenerator.apply(item).equals(chipLabel)).findFirst();
			if (newItem.isPresent()) {
				ArrayList<T> newValue = new ArrayList<>(getValue());
				newValue.add(newItem.get());
				this.setValue(newValue);
			} else {
				if (isAllowAdditionalItems()) {
					if (newItemHandler==null) throw new IllegalStateException("You need to setup a NewItemHandler");
					T item = this.newItemHandler.apply(chipLabel);
					ArrayList<T> newValue = new ArrayList<>(getValue());
					newValue.add(item);
					this.setValue(newValue);
				} else throw new IllegalStateException("Adding new items is not allowed, but still receiving new items from client-side. Probably wrong configuration.");
			}
		}).addEventData("event.detail.chipLabel");
		getElement().addEventListener("chip-removed", e->{
			JsonObject eventData = e.getEventData();
			String chipLabel = eventData.get("event.detail.chipLabel").asString();
			Stream<T> streamItems = availableItems.fetch(new Query());
			Optional<T> itemToRemove = streamItems.filter(item->itemLabelGenerator.apply(item).equals(chipLabel)).findFirst();
			if (itemToRemove.isPresent()) {
				System.out.println(this.getValue().remove(itemToRemove.get())); 
			}
			ArrayList<T> newValue = new ArrayList<>(getValue());
			this.setValue(newValue);
		}).addEventData("event.detail.chipLabel");
	}
	
	private void configureItems() {
		Stream<T> streamItems = availableItems.fetch(new Query());
		JreJsonArray array = (JreJsonArray) new JreJsonFactory().createArray();
		AtomicInteger ai = new AtomicInteger(0);
		streamItems.forEach(item->{
			JsonObject object = new JreJsonFactory().createObject();
			object.put("text",itemLabelGenerator.apply(item));
			object.put("value",itemLabelGenerator.apply(item));
			array.set(ai.getAndIncrement(),object);
		});
		this.getElement().setPropertyJson("source", array);
	}

	@Override
	protected void onAttach(AttachEvent attachEvent) {
		configure();
	}
	
	private List<Chip> generateSelectedChips(List<T> itemsToGenerate) {
		return itemsToGenerate.stream().map(item->generateChip(item)).collect(Collectors.toList());
	}

	private Chip generateChip(T item) {
		return new Chip(itemLabelGenerator.apply(item));
	}

	
	private void appendClientChipWithoutEvent(Chip chip) {
		String function = "			(function _appendChipWithoutEvent() {" +
				"				if ($0.allowDuplicates) {" + 
				"					$0.push('items', $1);" + 
				"				} else if ($0.items.indexOf($1) == -1) {" + 
				"					$0.push('items', $1);" + 
				"				}" + 
				"				$0.required = false;" + 
				"				$0.autoValidate = false;" + 
				"				$0._value = '';" + 
				"			})()";
		UI.getCurrent().getPage().executeJavaScript(function, this.getElement(), chip.getLabel());
	}

	private void appendClientChip(Chip chip) {
		String function = "			(function _appendChip() {" + 
				"				$0._saveTag($1);" + 
				"				$0.dispatchEvent(new CustomEvent('chip-created', {" + 
				"					detail: {" + 
				"						chipLabel: $1" + 
				"					}" + 
				"				}));" + 
				"				$0.required = false;" + 
				"				$0.autoValidate = false;" + 
				"				$0._value = '';" + 
				"			})()";
		UI.getCurrent().getPage().executeJavaScript(function, this.getElement(), chip.getLabel());
	}

	private void removeClientChipWithoutEvent(Chip chip) {
		String function = "			(function _removeChipByLabel() {" + 
				"				const index = $0.items.indexOf($1);" + 
				"				if (index != -1) {" + 
				"					$0.items.splice('availableItems', index, 1);" + 
				"				}" + 
				"			})()";
		UI.getCurrent().getPage().executeJavaScript(function, this.getElement(), chip.getLabel());
	}

	private void removeAllClientChipsWithoutEvent() {
		this.generateSelectedChips(getValue()).forEach(chip -> removeClientChipWithoutEvent(chip));
	}

	public void setAvailableItems(List<T> items) {
		this.availableItems = DataProvider.ofCollection(items);
	}
	
	public String getLabel() {
		return this.getElement().getProperty("label");
	}

	public void setLabel(String label) {
		this.getElement().setProperty("label", label);
	}

	public String[] getChipsAsStrings() {
		return this.generateSelectedChips(getValue()).stream().map(Chip::getLabel).collect(Collectors.toList())
				.toArray(new String[this.generateSelectedChips(getValue()).size()]);
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
		this.getElement().setProperty("additionalItems", allowAdditionalItems);
	}
	
	public boolean isAllowAdditionalItems() {
		return getElement().getProperty("additionalItems", false);
	}

	public void validate() {
		getElement().callFunction("validate");
	}
	
	// EVENTS
	@DomEvent("chip-removed")
	static public class ChipRemovedEvent<T> extends ComponentEvent<ChipField<T>> {

		private String chipLabel;
		
		public ChipRemovedEvent(ChipField<T> source, boolean fromClient, @EventData("event.detail.chipLabel") String chipLabel) {
			super(source, fromClient);
			this.chipLabel = chipLabel;
		}

		public String getChipLabel() {
			return chipLabel;
		}
	}

	@DomEvent("chip-created")
	static public class ChipCreatedEvent<T> extends ComponentEvent<ChipField<T>> {
		
		private String chipLabel;
		
		public ChipCreatedEvent(ChipField<T> source, boolean fromClient, @EventData("event.detail.chipLabel") String chipLabel) {
			super(source, fromClient);
			this.chipLabel = chipLabel;
		}

		public String getChipLabel() {
			return chipLabel;
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Registration addChipRemovedListener(ComponentEventListener<ChipRemovedEvent<T>> listener) {
		return addListener(ChipRemovedEvent.class, (ComponentEventListener)listener);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Registration addChipCreatedListener(ComponentEventListener<ChipCreatedEvent<T>> listener) {
		return addListener(ChipCreatedEvent.class, (ComponentEventListener)listener);
	}

    public void setChipLabelGenerator(
            ItemLabelGenerator<T> itemLabelGenerator) {
        this.itemLabelGenerator = itemLabelGenerator;
    }
    
    public void setNewItemHandler(Function<String,T> handler) {
    	this.newItemHandler = handler;
    	this.setAllowAdditionalItems(true);
    }

	@Override
	protected void setPresentationValue(List<T> newPresentationValue) {
//		this.setSelectedItems(newPresentationValue);
//		this.removeAllClientChipsWithoutEvent();
//		this.generateSelectedChips(newPresentationValue).forEach(chip -> appendClientChipWithoutEvent(chip));
	}

//	private void setSelectedItems(List<T> newPresentationValue) {
//		this.selectedItems.clear();
//		newPresentationValue.forEach(item->this.selectedItems.add(item));
//		showSelectedChips();
//	}

	@Override
	public void setDataProvider(DataProvider<T, ?> dataProvider) {
		this.availableItems = dataProvider;
	}

	public void addSelectedItem(T newItem) {
		if (!availableItems.fetch(new Query()).anyMatch(item->item.equals(newItem))) {
			throw new UnsupportedOperationException("Cannot select item '" + newItem + "', because is not present in DataProvider");
		} else {
			this.getValue().add(newItem);
			this.appendClientChipWithoutEvent(generateChip(newItem));
			this.fireEvent(new ChipCreatedEvent<>(this, false, itemLabelGenerator.apply(newItem)));
		}
	}
	
	
}
