package com.flowingcode.vaadin.addons.chipfield;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.binder.Binder;
import java.util.Arrays;
import java.util.stream.Collectors;

@SuppressWarnings("serial")
public class BinderDemo extends VerticalLayout {

	public BinderDemo() {
		Planet p = new Planet("A new planet");
		ChipField<String> chf = new ChipField<>(
				"Choose planet features (Binder demo, try with: 'Rings', 'Moons', 'Water', etc.)");
		chf.setWidthFull();
		chf.setItems(Arrays.asList("Rings", "Moons", "Water", "Rocks", "Lava", "Ice", "Cold", "Heat", "Atmosphere"));
		Binder<Planet> binder = new Binder<>();
		binder.bind(chf, Planet::getConfiguration, Planet::setConfiguration);
		binder.setBean(p);
		Button show = new Button("Show planet configuration");
		show.addClickListener(
				event -> Notification.show(
						"Planet: " + p.getName() + ", features: "
								+ p.getConfiguration().stream().collect(Collectors.joining(",")),
						5000, Position.BOTTOM_START));
		chf.addValueChangeListener(
				newItem -> Notification.show("Items: " + newItem.getValue(), 5000, Position.BOTTOM_START));

		add(chf, show);
	}
}
