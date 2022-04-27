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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.vaadin.flow.component.button.testbench.ButtonElement;
import org.junit.Test;

/**
 * Test that shows that removing a chipfield with values from the layout and adding it again doesn't
 * lose the chip values.
 *
 * @see https://github.com/FlowingCode/ChipFieldAddon/issues/33
 */
public class RemoveAndReAddTestIT extends AbstractViewTest {

  public RemoveAndReAddTestIT() {
    super("remove-add-test");
  }

  @Test
  public void toggleChipField() {
    ChipFieldElement chipfield = $(ChipFieldElement.class).id("chipfield");
    assertTrue(chipfield.isDisplayed());
    assertEquals(chipfield.getValue().size(), 2);
    ButtonElement button = $(ButtonElement.class).id("toggle");
    button.click(); // remove chipfield
    assertFalse($(ChipFieldElement.class).exists());
    button.click(); // add chipfield again
    chipfield = $(ChipFieldElement.class).id("chipfield");
    assertTrue(chipfield.isDisplayed());
    assertEquals(chipfield.getValue().size(), 2);
  }
}
