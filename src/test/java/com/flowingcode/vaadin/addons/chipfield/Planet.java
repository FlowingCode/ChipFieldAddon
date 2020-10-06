package com.flowingcode.vaadin.addons.chipfield;

import java.util.ArrayList;
import java.util.List;

public class Planet {
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

