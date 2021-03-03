package com.flowingcode.vaadin.addons.chipfield.integration.rpc;

import java.util.AbstractCollection;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import elemental.json.Json;
import elemental.json.JsonValue;
import elemental.json.impl.JreJsonArray;
import lombok.experimental.Delegate;

/** Server-side flavor of {@code JsonArrayList} */
@SuppressWarnings("serial")
class JreJsonArrayList<T> extends JreJsonArray implements JsonArrayList<T> {

	@Delegate(excludes = JreJsonArray.class)
	private Collection<T> list = new AbstractCollection<T>() {
		// the delegate is only for the purpose of implementing Collection,
		// but the Collection interface is unsupported on instances of JreJsonArrayList
		@Override
		public Iterator<T> iterator() {
			throw new UnsupportedOperationException();
		}

		@Override
		public int size() {
			throw new UnsupportedOperationException();
		}
	};

	public JreJsonArrayList(List<T> list, Function<? super T, JsonValue> mapper) {
		super(Json.instance());
		for (T t : list) {
			set(length(), Optional.ofNullable(t).map(mapper).orElseGet(Json::createNull));
		}
	}

	@Override
	public List<T> asList() {
		// JsonArrayList#asList is unsupported
		throw new UnsupportedOperationException();
	}
}