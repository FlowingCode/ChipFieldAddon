package com.flowingcode.vaadin.addons.chipfield.integration;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeDiagnosingMatcher;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.openqa.selenium.chrome.ChromeDriver;

import com.vaadin.testbench.ScreenshotOnFailureRule;
import com.vaadin.testbench.TestBench;
import com.vaadin.testbench.TestBenchElement;
import com.vaadin.testbench.parallel.ParallelTest;

import io.github.bonigarcia.wdm.WebDriverManager;

/**
 * Base class for ITs. The tests use Chrome driver to run integration tests on a
 * headless Chrome.
 */
public abstract class AbstractViewTest extends ParallelTest {

	private static final int SERVER_PORT = 8080;

	private final String route;

	protected static final Matcher<TestBenchElement> hasBeenUpgradedToCustomElement = new TypeSafeDiagnosingMatcher<TestBenchElement>() {

		@Override
		public void describeTo(Description description) {
			description.appendText("a custom element");
		}

		@Override
		protected boolean matchesSafely(TestBenchElement item, Description mismatchDescription) {
			String script = "let s=arguments[0].shadowRoot; return !!(s&&s.childElementCount)";
			if (!item.getTagName().contains("-")) {
				return true;
			}

			if ((Boolean) item.getCommandExecutor().executeScript(script, item)) {
				return true;
			} else {
				mismatchDescription.appendText(item.getTagName() + " ");
				mismatchDescription.appendDescriptionOf(is(not(this)));
				return false;
			}
		}

	};

	@Rule
	public ScreenshotOnFailureRule rule = new ScreenshotOnFailureRule(this, true);

	protected AbstractViewTest(String route) {
		this.route = route;
	}

	@BeforeClass
	public static void setupClass() {
		WebDriverManager.chromedriver().setup();
	}

	@Override
	@Before
	public void setup() throws Exception {
		setDriver(TestBench.createDriver(new ChromeDriver()));
		getDriver().get(getURL(route));
	}

	/**
	 * Returns deployment host name concatenated with route.
	 *
	 * @return URL to route
	 */
	private static String getURL(String route) {
		return String.format("http://%s:%d/%s", getDeploymentHostname(), SERVER_PORT, route);
	}

	/**
	 * If running on CI, get the host name from environment variable HOSTNAME
	 *
	 * @return the host name
	 */
	private static String getDeploymentHostname() {
		return "localhost";
	}

}
