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
package com.flowingcode.vaadin.addons.chipfield.integration;

import com.flowingcode.vaadin.testbench.rpc.JsonArrayList;

public interface IntegrationViewCallables {

  void addValueChangeListener();

  void addItemRemovedListener();

  void addItemClickedListener();

  void addItemCreatedListener();

  JsonArrayList<String> getLastValueChange();

  String getLastClickedItem();

  String getLastRemovedItem();

  String getLastCreatedItem();

  void allowAdditionalItems(boolean value);

  void setFieldReadOnly(boolean value);

  void setFieldEnabled(boolean value);

  void setValue(String... items);

  void useNewItemHandler(boolean useHandler);

  JsonArrayList<String> getValue();

  void setClosable(boolean closable);

  boolean isClosable();

  void setAllowedPattern(String pattern);

  void removeSelectedItem(String itemToRemove);

  boolean hasItemWithLabel(String label);
}
