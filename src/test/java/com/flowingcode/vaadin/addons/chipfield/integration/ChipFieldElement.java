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

import com.vaadin.testbench.TestBenchElement;
import com.vaadin.testbench.elementsbase.Element;
import java.util.List;
import org.openqa.selenium.Keys;

@Element("paper-chip-input-autocomplete")
public class ChipFieldElement extends TestBenchElement {

  @SuppressWarnings("unchecked")
  List<String> getValue() {
    return (List<String>) getProperty("items");
  }

  @Override
  public void sendKeys(CharSequence... keysToSend) {
    $("paper-input").first().$("input").first().sendKeys(keysToSend);
  }

  public void selectByText(String label) {
    sendKeys(label, Keys.ARROW_DOWN, Keys.ENTER);
  }

  public void click(int index) {
    TestBenchElement chip = $("paper-chip").get(index);
    executeScript("arguments[0].root.querySelector('.chip').click()", chip);
  }

  public String getInputValue() {
    return (String) executeScript("return arguments[0].$.paperInput.value", this);
  }

}
