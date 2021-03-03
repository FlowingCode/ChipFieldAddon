/*-
 * #%L
 * ChipField Addon
 * %%
 * Copyright (C) 2018 - 2021 Flowing Code
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
package com.flowingcode.vaadin.addons.chipfield.integration.rpc;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.vaadin.flow.component.ClientCallable;
import com.vaadin.testbench.HasDriver;

/**
 * @author Javier Godoy / Flowing Code
 */
public interface HasRpcSupport extends HasDriver {

	/**
	 * Call a {@link ClientCallable} defined on the integration view.
	 *
	 * @param callable  the client callable name
	 * @param arguments arguments to be passed to the callable
	 * @throws TimeoutException if the callable times out (see
	 *                          {@link WebDriver.Timeouts#setScriptTimeout(long, java.util.concurrent.TimeUnit)
	 *                          WebDriver.Timeouts}).
	 * @throws RuntimeException if the callable fails.
	 */
	default Object call(String callable, Object... arguments) {
		WebElement view = getDriver().findElement(By.id("view"));
		arguments = Optional.ofNullable(arguments).orElse(new Object[0]);

		StringBuilder script = new StringBuilder();
		script.append("var view = arguments[0];");
		script.append("var callable = arguments[1];");
		script.append("var callback = (result,success) => arguments[3]({result, success});");
		script.append("view.$server[callable](...arguments[2])");
		script.append(" .then(result=>callback(result, true))");
		script.append(" .catch(()=>callback(undefined, false));");

		@SuppressWarnings("unchecked")
		Map<String, Object> result = (Map<String, Object>) ((JavascriptExecutor) getDriver()).executeAsyncScript(script.toString(), view, callable, arguments);

		if (!(Boolean) result.get("success")) {
			throw new RuntimeException(
					String.format("server call failed: %s(%s)", callable, Stream.of(arguments).map(Object::toString).collect(Collectors.joining(","))));
		}

		return result.get("result");
	}

	/**
	 * Create a TestBench proxy that invokes methods from the interface through a client {@link #call}.
	 */
	default <T> T createCallableProxy(Class<T> intf) {
		return intf.cast(Proxy.newProxyInstance(intf.getClassLoader(), new Class<?>[] { intf }, new InvocationHandler() {
			@Override
			public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
				Object result = call(method.getName(), args);

				if (result == null || method.getReturnType() == Void.TYPE) {
					return null;
				}

				if (method.getReturnType() == JsonArrayList.class) {
					return JsonArrayList.wrapForTestbench((List<?>) result);
				}

				// this implementation is incomplete.
				// other types that should be supported are: Double, Integer, Boolean, String, JsonValue
				throw new ClassCastException(String.format("%s as %s", result.getClass().getName(), method.getReturnType().getName()));
			}
		}));
	}

}
