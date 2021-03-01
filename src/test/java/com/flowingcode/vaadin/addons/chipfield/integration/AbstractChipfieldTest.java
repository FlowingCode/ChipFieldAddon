package com.flowingcode.vaadin.addons.chipfield.integration;

import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Before;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.vaadin.flow.component.ClientCallable;

public class AbstractChipfieldTest extends AbstractViewTest {

	protected ChipFieldElement chipfield;

	protected AbstractChipfieldTest() {
		super("it");
	}

	@Before
	public void before() {
		chipfield = $(ChipFieldElement.class).first();
	}
	/**
	 * Call a {@link ClientCallable} defined on the integration view.
	 *
	 * @param callable  the client callable name
	 * @param arguments arguments to be passed to the callable
	 * @throws TimeoutException if the callable doesn't complete in 2 seconds.
	 * @throws RuntimeException if the callable fails.
	 */
	protected final void call(String callable, Object... arguments) {
		WebElement view = getDriver().findElement(By.id("view"));
		String result = "data-callable-result";

		StringBuilder script = new StringBuilder();
		script.append("var view = arguments[0];");
		script.append("var callable = arguments[1];");
		script.append("var result = arguments[2];");
		script.append("view.removeAttribute(result);");
		script.append("view.$server[callable](...arguments[3])");
		script.append(" .then(()=>view.setAttribute(result,true))");
		script.append(" .catch(()=>view.setAttribute(result,false));");

		((JavascriptExecutor) getDriver()).executeScript(script.toString(), view, callable, result, arguments);

		new WebDriverWait(getDriver(), 2, 100).until(ExpectedConditions.attributeToBeNotEmpty(view, result));
		if (!Boolean.parseBoolean(view.getAttribute(result))) {
			throw new RuntimeException(
					String.format("server call failed: %s(%s)", callable, Stream.of(arguments).map(Object::toString).collect(Collectors.joining(","))));
		}
	}

}
