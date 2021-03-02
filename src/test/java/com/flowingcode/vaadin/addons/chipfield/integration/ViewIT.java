package com.flowingcode.vaadin.addons.chipfield.integration;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertThrows;

import java.util.Arrays;
import java.util.Collection;

import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.openqa.selenium.Keys;

public class ViewIT extends AbstractChipfieldTest {

	private static final String LOREM = "Lorem";
	private static final String IPSUM = "Ipsum";
	private static final String ADDITIONAL = "Additional";

	IntegrationViewCallables $server = createCallableProxy(IntegrationViewCallables.class);

    @Test
	public void testUpgradedToCustomElement() {
		ChipFieldElement chipfield = $(ChipFieldElement.class).first();
		assertThat(chipfield, hasBeenUpgradedToCustomElement);
	}

	@Test
	public void testCallableSuccess() {
		// test that the callable mechanism works
		$server.testCallable(true);
	}

	@Test
	public void testCallableFailure() {
		// test that the callable mechanism detect failures
		assertThrows(RuntimeException.class, () -> $server.testCallable(false));
	}

	private Matcher<Collection<String>> isEqualTo(String... values) {
		return Matchers.equalTo(Arrays.asList(values));
	}

	@Test
	public void testSelectByText() {
		chipfield.selectByText(LOREM);
		assertThat(chipfield.getValue(), isEqualTo(LOREM));
		assertThat($server.getValue(), isEqualTo(LOREM));

		chipfield.selectByText(IPSUM);
		assertThat(chipfield.getValue(), isEqualTo(LOREM, IPSUM));
		assertThat($server.getValue(), isEqualTo(LOREM, IPSUM));

		chipfield.sendKeys(Keys.BACK_SPACE);
		assertThat(chipfield.getValue(), isEqualTo(LOREM));
		assertThat($server.getValue(), isEqualTo(LOREM));

		chipfield.sendKeys(Keys.BACK_SPACE);
		assertThat(chipfield.getValue(), Matchers.empty());
		assertThat($server.getValue(), Matchers.empty());
    }

	@Test
	public void testAdditionalItemWithNoHandler() {
		$server.allowAdditionalItems(true);
		chipfield.sendKeys(ADDITIONAL, Keys.ENTER);
		assertThat(chipfield.getValue(), Matchers.empty());
		assertThat($server.getValue(), Matchers.empty());
	}

	@Test
	public void testAdditionalItemEnabled() {
		$server.allowAdditionalItems(true);
		$server.useNewItemHandler(true);

		chipfield.sendKeys(ADDITIONAL, Keys.ENTER);
		assertThat(chipfield.getValue(), isEqualTo(ADDITIONAL));
		assertThat($server.getValue(), isEqualTo(ADDITIONAL));

		chipfield.sendKeys(LOREM, Keys.ENTER);
		assertThat(chipfield.getValue(), isEqualTo(ADDITIONAL, LOREM));
		assertThat($server.getValue(), isEqualTo(ADDITIONAL, LOREM));
	}

	@Test
	public void testAdditionalItemDisabled() {
		chipfield.sendKeys("Additional", Keys.ENTER);
		assertThat(chipfield.getValue(), Matchers.empty());
		assertThat($server.getValue(), Matchers.empty());
	}

	@Test
	public void testReadOnly() {
		chipfield.selectByText(LOREM);
		assertThat(chipfield.getValue(), isEqualTo(LOREM));
		assertThat($server.getValue(), isEqualTo(LOREM));

		$server.setFieldReadOnly(true);
		chipfield.sendKeys(Keys.BACK_SPACE);
		assertThat(chipfield.getValue(), isEqualTo(LOREM));
		assertThat($server.getValue(), isEqualTo(LOREM));

		$server.allowAdditionalItems(true);
		chipfield.sendKeys(ADDITIONAL, Keys.ENTER);
		assertThat(chipfield.getValue(), isEqualTo(LOREM));
		assertThat($server.getValue(), isEqualTo(LOREM));
	}

	@Test
	public void testSetValue() {
		$server.setValue(LOREM);
		assertThat(chipfield.getValue(), isEqualTo(LOREM));
		assertThat($server.getValue(), isEqualTo(LOREM));

		$server.setValue(IPSUM);
		assertThat(chipfield.getValue(), isEqualTo(IPSUM));
		assertThat($server.getValue(), isEqualTo(IPSUM));

		$server.setValue(LOREM, IPSUM);
		assertThat(chipfield.getValue(), isEqualTo(LOREM, IPSUM));
		assertThat($server.getValue(), isEqualTo(LOREM, IPSUM));

		$server.setValue(IPSUM, LOREM);
		assertThat(chipfield.getValue(), isEqualTo(IPSUM, LOREM));
		assertThat($server.getValue(), isEqualTo(IPSUM, LOREM));
	}

}
