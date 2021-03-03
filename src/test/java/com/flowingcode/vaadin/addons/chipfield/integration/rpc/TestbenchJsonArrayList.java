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

import java.util.List;

import lombok.experimental.Delegate;

/**
 * Testbench-side flavor of {@code JsonArrayList}
 *
 * @author Javier Godoy / Flowing Code
 */
@SuppressWarnings("serial")
public class TestbenchJsonArrayList<T> implements JsonArrayList<T>, TestbenchValueWrapper {

	@Delegate
	private List<T> list;

	public TestbenchJsonArrayList(List<T> list) {
		this.list = list;
	}

	@Override
	public List<T> asList() {
		return list;
	}

	@Override
	public boolean equals(Object obj) {
		return list.equals(obj);
	}

	@Override
	public String toString() {
		return list.toString();
	}
}

