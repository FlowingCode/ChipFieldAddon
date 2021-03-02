package com.flowingcode.vaadin.addons.chipfield.integration;

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

//Vaadin constraints:
// - the result type of a @ClientCallable methods must be assignable to JsonValue.
// - the runtime type of the result must be instanceof JreJsonValue.
// - JreJsonValue can only be extended through intermediate classes.

//Testbench constraints:
// - Selenium already converts JSON to a List of objects (thus we have a "T" and not a JsonValue).
// - In the integration test, we are interested on the "T" and not the raw JsonValue.
// - It makes sense to implement Collection<T>, in order to facilitate the use of hamcrest Matchers
// - JsonArray cannot implement List it because the return type of get(int) is incompatible.
// - List isn't too helpul, because most Matchers work with Iterable/Collection.
// - JsonValue methods (JS type coercion, etc.) are not needed in integration tests.
@SuppressWarnings("serial")
public interface JsonArrayList<T> extends JsonValue, Collection<T> {
	
	List<T> asList();

	public static JsonArrayList<String> fromStringArray(List<String> list) {
		return createArray(list, Json::create);
	}
	
	public static JsonArrayList<Boolean> fromBooleanArray(List<Boolean> list) {
		return createArray(list, Json::create);
	}
	
	public static JsonArrayList<Double> fromDoubleArray(List<Double> list) {
		return createArray(list, Json::create);
	}
		
	/**
	 * @deprecated. This method should be private.
	 */
	public static <T> JsonArrayList<T> createArray(List<T> list, Function<? super T, JsonValue> mapper) {
		//this is the server-side flavor of JsonArrayList		
		class JreJsonArrayList extends JreJsonArray implements JsonArrayList<T> {

			@Delegate(excludes = JreJsonArray.class)
			private Collection<T> list = new AbstractCollection<T>() {
				//the delegate is only for the purpose of implementing Collection,
				//but the Collection interface is unsupported on instances of JreJsonArrayList
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
				//JsonArrayList#asList is unsupported
				throw new UnsupportedOperationException();
			}
		}
		
		return new JreJsonArrayList(list, mapper);
	}

	public static <T> JsonArrayList<T> wrapForTestbench(List<T> list) {
		class TestbenchJsonArrayList implements JsonArrayList<T>, TestbenchValueWrapper {

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

		return new TestbenchJsonArrayList(list);

	}

}


