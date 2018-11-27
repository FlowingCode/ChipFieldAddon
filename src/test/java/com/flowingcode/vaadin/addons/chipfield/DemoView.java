package com.flowingcode.vaadin.addons.chipfield;

import java.lang.ref.WeakReference;

/*-
 * #%L
 * ChipField Addon
 * %%
 * Copyright (C) 2018 Flowing Code
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.flowingcode.vaadin.addons.chipfield.ChipField;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.router.Route;

@SuppressWarnings("serial")
@Route("")
public class DemoView extends VerticalLayout {

    public DemoView() {
    	List<Planet> availablePlanets = new ArrayList<>(Arrays.asList(new Planet("Mercury"), new Planet("Venus"), new Planet("Earth")
    			, new Planet("Mars"), new Planet("Jupiter"), new Planet("Saturn"), new Planet("Uranus"), new Planet("Neptune")));
    	ListDataProvider<Planet> ldp = new ListDataProvider<>(availablePlanets);
    	
    	ChipField<Planet> chf = new ChipField<>("Select some planets (Mercury, Venus, Earth, etc.)", planet->planet.getName());
    	chf.setWidth("100%");
    	chf.setDataProvider(ldp);
    	chf.setClosable(true);
    	chf.setNewItemHandler(label->new Planet(label));

    	Button b = new Button("Obtain selected planets");
    	b.addClickListener(event->Notification.show("Planets: " + chf.getValue().stream().map(planet->planet.getName()).collect(Collectors.joining(",")),5000,Position.BOTTOM_END));

    	Button b2 = new Button("Add random planet");
    	b2.addClickListener(event->{
    		Planet p = new Planet("Planet " + Math.random());
    		availablePlanets.add(p);
    		ldp.refreshAll();
    		chf.addSelectedItem(p);
    	});

    	chf.addChipCreatedListener(ev->Notification.show("Chip: " + ev.getChipLabel() + " Created by client: " + ev.isFromClient() + "!",5000,Position.BOTTOM_END));
    	chf.addChipRemovedListener(ev->Notification.show("Chip: " + ev.getChipLabel() + " Removed by client: " + ev.isFromClient() + "!",5000,Position.BOTTOM_END));
    	
    	VerticalLayout vl = new VerticalLayout(chf,new HorizontalLayout(b,b2));
        add(vl);

        ChipField<String> chf3 = new ChipField<>("Select some planets (Restricted input, allowed pattern [a-zA-Z])");
        chf3.setWidth("500px");
        chf3.setAvailableItems(Arrays.asList("Mercury", "Venus", "Earth", "Mars", "Jupiter", "Saturn", "Uranus", "Neptune"));
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
        ChipField<String> chf5 = new ChipField<>("Choose planet features (Binder demo, try with: 'Rings', 'Moons', 'Water', etc.)");
        chf5.setWidth("500px");
        chf5.setItems(Arrays.asList("Rings", "Moons", "Water", "Rocks", "Lava", "Ice", "Cold", "Heat", "Atmosphere"));
        Binder<Planet> binder = new Binder<>();
        binder.bind(chf5,Planet::getConfiguration,Planet::setConfiguration);
        binder.setBean(p);
        Button show = new Button("Show planet configuration");
        show.addClickListener(event->Notification.show("Planet: " + p.getName() + ", features: " + p.getConfiguration().stream().collect(Collectors.joining(",")),5000,Position.BOTTOM_END));
        chf5.addValueChangeListener(newItem->Notification.show("Items: " + newItem.getValue(),5000,Position.BOTTOM_END));
        
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
