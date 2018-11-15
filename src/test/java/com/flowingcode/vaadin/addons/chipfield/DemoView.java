package com.flowingcode.vaadin.addons.chipfield;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.flowingcode.vaadin.addons.chipfield.ChipField;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.router.Route;

@SuppressWarnings("serial")
@Route("")
public class DemoView extends HorizontalLayout {

    public DemoView() {
    	this.setDefaultVerticalComponentAlignment(Alignment.START);
    	List<Planet> availablePlanets = new ArrayList<>(Arrays.asList(new Planet("Mercury"), new Planet("Venus"), new Planet("Earth")));
    	ListDataProvider<Planet> ldp = new ListDataProvider<>(availablePlanets);
    	
    	ChipField<Planet> chf = new ChipField<>("Choose planets", planet->planet.getName());
    	chf.setDataProvider(ldp);
    	chf.setClosable(true);
    	chf.setNewItemHandler(label->new Planet(label));

    	Button b = new Button("Obtain selected planets");
    	b.addClickListener(event->Notification.show("Planets: " + chf.getValue().stream().map(planet->planet.getName()).collect(Collectors.joining(","))));

    	Button b2 = new Button("Add random planet");
    	b2.addClickListener(event->{
    		Planet p = new Planet("Planet " + Math.random());
    		availablePlanets.add(p);
    		ldp.refreshAll();
    		chf.addSelectedItem(p);
    	});

    	chf.addChipCreatedListener(ev->Notification.show("Chip: " + ev.getChipLabel() + " Created by client: " + ev.isFromClient() + "!"));
    	chf.addChipRemovedListener(ev->Notification.show("Chip: " + ev.getChipLabel() + " Removed by client: " + ev.isFromClient() + "!"));
    	
    	VerticalLayout vl = new VerticalLayout(chf,new HorizontalLayout(b,b2));
        add(vl);

        ChipField<String> chf3 = new ChipField<>("Restricted input (closable)", "Enter only letters");
        chf3.setAvailableItems(Arrays.asList("Mercury", "Venus", "Earth"));
        chf3.setAllowedPattern("[a-zA-Z]");
        chf3.setClosable(true);

    	vl = new VerticalLayout(chf3);
        add(vl);

        ChipField<String> chf4 = new ChipField<>("Disabled", "Mercury", "Venus", "Earth");
        chf4.addSelectedItem("Mercury");
        chf4.addSelectedItem("Venus");
        chf4.setDisabled(true);

    	vl = new VerticalLayout(chf4);
        add(vl);

        Planet p = new Planet("A new planet");
        ChipField<String> chf5 = new ChipField<>("Choose planet features", "Atmosphere");
        chf5.setItems(Arrays.asList("Rings", "Moons", "Water", "Rocks"));
        Binder<Planet> binder = new Binder<>();
        binder.bind(chf5,Planet::getConfiguration,Planet::setConfiguration);
        binder.setBean(p);
        Button show = new Button("Show planet configuration");
        show.addClickListener(event->Notification.show("Planet: " + p.getName() + ", features: " + p.getConfiguration().stream().collect(Collectors.joining(","))));
        chf5.addValueChangeListener(newItem->Notification.show("Items: " + newItem.getValue()));
        
    	vl = new VerticalLayout(chf5,show);
        add(vl);

    }
    
    
    public static class Planet {
    	private String name;
    	private List<String> configuration = new ArrayList<>();
    	
    	public Planet(String name) {
    		this.name = name;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public List<String> getConfiguration() {
			return configuration;
		}

		public void setConfiguration(List<String> configuration) {
			this.configuration = configuration;
		}

    }
    
}
