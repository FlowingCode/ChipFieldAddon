package com.flowingcode.vaadin.addons.chipfield.integration;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertThrows;

import java.util.Arrays;

import org.hamcrest.Matchers;
import org.junit.Test;

public class ViewIT extends AbstractChipfieldTest {

    @Test
	public void testUpgradedToCustomElement() {
		ChipFieldElement chipfield = $(ChipFieldElement.class).first();
		assertThat(chipfield, hasBeenUpgradedToCustomElement);
	}

	@Test
	public void testCallableSuccess() {
		// test that the callable mechanism worls
		call("testCallable", true);
	}

	@Test
	public void testCallableFailure() {
		// test that the callable mechanism detect failures
		assertThrows(RuntimeException.class, () -> call("testCallable", false));
	}

	@Test
	public void testCallableFailure2() {
		// test that the callable mechanism detect failures
		assertThrows(RuntimeException.class, () -> call("testCallable"));
	}

}
