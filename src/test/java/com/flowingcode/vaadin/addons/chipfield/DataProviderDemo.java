package com.flowingcode.vaadin.addons.chipfield;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.provider.ListDataProvider;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@SuppressWarnings("serial")
public class DataProviderDemo extends VerticalLayout {

	public DataProviderDemo() {
		List<Planet> availablePlanets = new ArrayList<>(
				Arrays.asList(new Planet("Mercury"), new Planet("Venus"), new Planet("Earth"), new Planet("Mars"),
						new Planet("Jupiter"), new Planet("Saturn"), new Planet("Uranus"), new Planet("Neptune")));
		ListDataProvider<Planet> ldp = new ListDataProvider<>(availablePlanets);

		ChipField<Planet> chf = new ChipField<>("Select some planets (Mercury, Venus, Earth, etc.)",
				planet -> planet.getName());
		chf.setWidth("100%");
		chf.setDataProvider(ldp);
		chf.setClosable(true);
		chf.setNewItemHandler(label -> new Planet(label));

		Button b = new Button("Obtain selected planets");
		b.addClickListener(event -> Notification.show(
				"Planets: " + chf.getValue().stream().map(planet -> planet.getName()).collect(Collectors.joining(",")),
				5000, Position.BOTTOM_END));

		Button b2 = new Button("Add random planet");
		b2.addClickListener(event -> {
			Planet p = new Planet("Planet" + Math.round(Math.random() * 10000));
			availablePlanets.add(p);
			ldp.refreshAll();
			chf.addSelectedItem(p);
		});

		chf.addChipCreatedListener(
				ev -> Notification.show("Chip: " + ev.getChipLabel() + " Created by client: " + ev.isFromClient() + "!",
						5000, Position.BOTTOM_END));
		chf.addChipRemovedListener(
				ev -> Notification.show("Chip: " + ev.getChipLabel() + " Removed by client: " + ev.isFromClient() + "!",
						5000, Position.BOTTOM_END));
		chf.addChipClickedListener(
				ev -> Notification.show("Chip: " + ev.getChipLabel() + " Clicked!", 5000, Position.BOTTOM_END));

		VerticalLayout vl = new VerticalLayout(chf, new HorizontalLayout(b, b2));
		add(vl);
	}
}
