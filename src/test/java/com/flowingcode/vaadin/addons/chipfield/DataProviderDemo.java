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

import java.util.stream.Collectors;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.provider.ListDataProvider;

@SuppressWarnings("serial")
public class DataProviderDemo extends VerticalLayout {

	public DataProviderDemo() {

		ListDataProvider<Planet> ldp = new ListDataProvider<>(Planet.all());

		ChipField<Planet> chf = new ChipField<>("Select some planets (Mercury, Venus, Earth, etc.)", planet -> planet.getName());
		chf.setWidthFull();
		chf.setDataProvider(ldp);
		chf.setClosable(true);
		chf.setNewItemHandler(label -> new Planet(label));

		HorizontalLayout buttons = new HorizontalLayout();
		buttons.add(new Button("Obtain selected planets", ev -> Notification
				.show("Planets: " + chf.getValue().stream().map(Planet::getName).collect(Collectors.joining(",")), 5000, Position.BOTTOM_START)));

		buttons.add(new Button("Add random planet", ev -> {
			Planet newPlanet = Planet.random();
			ldp.getItems().add(newPlanet);
			ldp.refreshAll();
			chf.addSelectedItem(newPlanet);
		}));

		chf.addChipCreatedListener(
				ev -> Notification.show("Chip: " + ev.getItem() + " Created by client: " + ev.isFromClient() + "!",
						5000, Position.BOTTOM_START));
		chf.addChipRemovedListener(
				ev -> Notification.show("Chip: " + ev.getItem() + " Removed by client: " + ev.isFromClient() + "!",
						5000, Position.BOTTOM_START));
		chf.addChipClickedListener(
				ev -> Notification.show("Chip: " + ev.getItem() + " Clicked!", 5000, Position.BOTTOM_END));

		buttons.add(new Button("All planets", ev -> {
			chf.setValue(Planet.all());
		}));

		buttons.add(new Button("Remove Inner planets", ev -> {
			chf.removeSelectedItem(new Planet("Mercury"));
			chf.removeSelectedItem(new Planet("Venus"));
			chf.removeSelectedItem(new Planet("Earth"));
			chf.removeSelectedItem(new Planet("Mars"));
		}));

		add(new VerticalLayout(chf, buttons));
	}

}
