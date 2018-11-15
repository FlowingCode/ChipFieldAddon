# Chips Addon

Vaadin Flow integration of https://github.com/ThomasCybulski/paper-chip

## Online demo

Try the add-on demo at http://addonsv10.flowingcode.com/chipfield

## Download release

Official releases of this add-on are available at Vaadin Directory. For Maven instructions, download and reviews, go to https://vaadin.com/directory/component/chipfield-addon

## Building and running demo

git clone https://github.com/FlowingCode/ChipFieldAddon.git
mvn clean install jetty:run

To see the demo, navigate to http://localhost:8080/

## Development with Eclipse IDE

For further development of this add-on, the following tool-chain is recommended:
- Eclipse IDE
- m2e wtp plug-in (install it from Eclipse Marketplace)
- Vaadin Eclipse plug-in (install it from Eclipse Marketplace)
- JRebel Eclipse plug-in (install it from Eclipse Marketplace)
- Chrome browser

### Importing project

Choose File > Import... > Existing Maven Projects

Note that Eclipse may give "Plugin execution not covered by lifecycle configuration" errors for pom.xml. Use "Permanently mark goal resources in pom.xml as ignored in Eclipse build" quick-fix to mark these errors as permanently ignored in your project. Do not worry, the project still works fine. 

### Debugging

To debug project and make code modifications on the fly in the server-side, right-click the project and choose Debug As > Debug on Server. Navigate to http://localhost:8080/ to see the application.
 
## Release notes

### Version 1.0-SNAPSHOT
- First release of the addon

## Roadmap

This component is developed as a hobby with no public roadmap or any guarantees of upcoming releases.

## Issue tracking

The issues for this add-on are tracked on its github.com page. All bug reports and feature requests are appreciated. 

## Contributions

Contributions are welcome, but there are no guarantees that they are accepted as such. Process for contributing is the following:
- Fork this project
- Create an issue to this project about the contribution (bug or feature) if there is no such issue about it already. Try to keep the scope minimal.
- Develop and test the fix or functionality carefully. Only include minimum amount of code needed to fix the issue.
- Refer to the fixed issue in commit
- Send a pull request for the original project
- Comment on the original issue that you have implemented a fix for it

## License & Author

Add-on is distributed under Apache License 2.0. For license terms, see LICENSE.txt.

ChipFieldAddon is written by Flowing Code S.A.

# Developer Guide

## Getting started

Here is a simple example on how to try out the add-on component:

    	List<Planet> availablePlanets = new ArrayList<>(Arrays.asList(new Planet("Mercury"), new Planet("Venus"), new Planet("Earth")));
    	ListDataProvider<Planet> ldp = new ListDataProvider<>(availablePlanets);
    	
    	ChipField<Planet> chf = new ChipField<>("Choose planets", planet->planet.getName());
    	chf.setDataProvider(ldp);
    	chf.setClosable(true);
    	chf.setNewItemHandler(label->new Planet(label));

For a more comprehensive example, see com.flowingcode.vaadin.addons.chipfield.DemoView

## Features

### Fluent API

    	c = new Carousel(s1,s2,s3,s4)
    			.withAutoProgress()
    			.withSlideDuration(4)
    			.withStartPosition(1);

### Server-side listeners

    	c.addChangeListener(e->Notification.show("changed!",1000,Position.BOTTOM_END));

### Server-side methods

    	Button next = new Button(">>");
    	next.addClickListener(e->cf.moveNext());
    	Button prev = new Button("<<");
    	prev.addClickListener(e->cf.movePrev());
    	Button last = new Button(">|");
    	last.addClickListener(e->cf.movePos(3));
    	Button first = new Button("|<");
    	first.addClickListener(e->cf.movePos(0));

