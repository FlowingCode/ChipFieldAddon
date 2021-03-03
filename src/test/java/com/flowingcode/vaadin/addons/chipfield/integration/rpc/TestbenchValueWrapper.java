package com.flowingcode.vaadin.addons.chipfield.integration.rpc;

import elemental.json.JsonType;
import elemental.json.JsonValue;

public interface TestbenchValueWrapper extends JsonValue {

	@Override
	default boolean asBoolean() {
		throw new UnsupportedOperationException();
	}

	@Override
	default double asNumber() {
		throw new UnsupportedOperationException();
	}

	@Override
	default String asString() {
		throw new UnsupportedOperationException();
	}

	@Override
	default JsonType getType() {
		throw new UnsupportedOperationException();
	}

	@Override
	default String toJson() {
		throw new UnsupportedOperationException();
	}

	@Override
	default boolean jsEquals(JsonValue value) {
		throw new UnsupportedOperationException();
	}

	@Override
	default Object toNative() {
		throw new UnsupportedOperationException();
	}

}
