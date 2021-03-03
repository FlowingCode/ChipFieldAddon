package com.flowingcode.vaadin.addons.chipfield.integration.rpc;

import java.util.List;

import lombok.experimental.Delegate;

/** Testbench-side flavor of {@code JsonArrayList} */
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

