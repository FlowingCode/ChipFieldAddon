/*-
 * #%L
 * ChipField Addon
 * %%
 * Copyright (C) 2018 - 2022 Flowing Code
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
