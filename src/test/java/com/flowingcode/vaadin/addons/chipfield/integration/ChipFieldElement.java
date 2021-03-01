package com.flowingcode.vaadin.addons.chipfield.integration;

import java.util.List;

import org.openqa.selenium.Keys;

import com.vaadin.testbench.TestBenchElement;
import com.vaadin.testbench.elementsbase.Element;

@Element("paper-chip-input-autocomplete")
public class ChipFieldElement extends TestBenchElement {

	@SuppressWarnings("unchecked")
	List<String> getValue() {
		return (List<String>) getProperty("items");
	}

	@Override
	public void sendKeys(CharSequence... keysToSend) {
		$("paper-input").first().$("input").first().sendKeys(keysToSend);
	}

	public void selectByText(String label) {
		sendKeys(label, Keys.ARROW_DOWN, Keys.ENTER);
	}

}