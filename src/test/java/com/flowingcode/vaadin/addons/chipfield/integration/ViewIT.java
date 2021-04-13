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
package com.flowingcode.vaadin.addons.chipfield.integration;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import java.util.Arrays;
import java.util.Collection;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.openqa.selenium.Keys;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ViewIT extends AbstractChipfieldTest {

  private static final String LOREM = "Lorem";
  private static final String IPSUM = "Ipsum";
  private static final String ADDITIONAL = "Additional";

  IntegrationViewCallables $server = createCallableProxy(IntegrationViewCallables.class);

  @Test
  public void testUpgradedToCustomElement() {
    ChipFieldElement chipfield = $(ChipFieldElement.class).first();
    assertThat(chipfield, hasBeenUpgradedToCustomElement);
  }

  @Test
  public void testCallableSuccess() {
    // test that the callable mechanism works
    $server.testCallable(true);
  }

  @Test
  public void testCallableFailure() {
    // test that the callable mechanism detect failures
    assertThrows(RuntimeException.class, () -> $server.testCallable(false));
  }

  private Matcher<Collection<String>> isEqualTo(String... values) {
    return Matchers.equalTo(Arrays.asList(values));
  }

  @Test
  public void testSelectOneByText() {
    chipfield.selectByText(LOREM);
    assertThat(chipfield.getValue(), isEqualTo(LOREM));
    assertThat($server.getValue(), isEqualTo(LOREM));
  }

  @Test
  public void testSelectTwoByText() {
    chipfield.selectByText(LOREM);
    chipfield.selectByText(IPSUM);
    assertThat(chipfield.getValue(), isEqualTo(LOREM, IPSUM));
    assertThat($server.getValue(), isEqualTo(LOREM, IPSUM));

    chipfield.sendKeys(Keys.BACK_SPACE);
    assertThat(chipfield.getValue(), isEqualTo(LOREM));
    assertThat($server.getValue(), isEqualTo(LOREM));

    chipfield.sendKeys(Keys.BACK_SPACE);
    assertThat(chipfield.getValue(), Matchers.empty());
    assertThat($server.getValue(), Matchers.empty());
  }

  @Test
  public void testBackspace() {
    $server.setValue(LOREM, IPSUM);

    chipfield.sendKeys(Keys.BACK_SPACE);
    assertThat(chipfield.getValue(), isEqualTo(LOREM));
    assertThat($server.getValue(), isEqualTo(LOREM));

    chipfield.sendKeys(Keys.BACK_SPACE);
    assertThat(chipfield.getValue(), Matchers.empty());
    assertThat($server.getValue(), Matchers.empty());
  }

  @Test
  public void testAdditionalItemWithNoHandler() {
    $server.allowAdditionalItems(true);
    chipfield.sendKeys(ADDITIONAL, Keys.ENTER);
    assertThat(chipfield.getValue(), Matchers.empty());
    assertThat($server.getValue(), Matchers.empty());
  }

  @Test
  public void testAdditionalItemEnabled() {
    $server.allowAdditionalItems(true);
    $server.useNewItemHandler(true);

    chipfield.sendKeys(ADDITIONAL, Keys.ENTER);
    assertThat(chipfield.getValue(), isEqualTo(ADDITIONAL));
    assertThat($server.getValue(), isEqualTo(ADDITIONAL));

    chipfield.sendKeys(LOREM, Keys.ENTER);
    assertThat(chipfield.getValue(), isEqualTo(ADDITIONAL, LOREM));
    assertThat($server.getValue(), isEqualTo(ADDITIONAL, LOREM));
  }

  @Test
  public void testAdditionalItemDisabled() {
    chipfield.sendKeys("Additional", Keys.ENTER);
    assertThat(chipfield.getValue(), Matchers.empty());
    assertThat($server.getValue(), Matchers.empty());
  }

  @Test
  public void testItemCreatedListenerFromClient() {
    $server.addItemCreatedListener();

    chipfield.selectByText(LOREM);
    assertThat($server.getLastCreatedItem(), Matchers.is(LOREM));
  }

  @Test
  public void testItemCreatedListenerFromServer() {
    $server.addItemCreatedListener();

    $server.setValue(IPSUM);
    assertThat($server.getLastCreatedItem(), Matchers.is(IPSUM));
  }

  @Test
  public void testAdditionalItemCreatedListenerFromClient() {
    $server.allowAdditionalItems(true);
    $server.useNewItemHandler(true);
    $server.addItemCreatedListener();

    chipfield.sendKeys(ADDITIONAL, Keys.ENTER);
    assertThat($server.getLastCreatedItem(), Matchers.is(ADDITIONAL));
  }

  @Test
  public void testAdditionalItemCreatedListenerFromServer() {
    $server.allowAdditionalItems(true);
    $server.useNewItemHandler(true);
    $server.addItemCreatedListener();

    $server.setValue(ADDITIONAL);
    assertThat($server.getLastCreatedItem(), Matchers.is(ADDITIONAL));
  }

  @Test
  public void testItemRemovedListenerFromClient() {
    $server.addItemRemovedListener();

    $server.setValue(LOREM);
    chipfield.sendKeys(Keys.BACK_SPACE);
    assertThat($server.getLastRemovedItem(), Matchers.is(LOREM));
  }

  @Test
  public void testItemRemovedListenerFromServer() {
    $server.addItemRemovedListener();

    $server.setValue(LOREM);
    $server.setValue();
    assertThat($server.getLastRemovedItem(), Matchers.is(LOREM));
  }

  @Test
  public void testAdditionalItemRemovedListenerFromClient() {
    $server.allowAdditionalItems(true);
    $server.useNewItemHandler(true);
    $server.addItemRemovedListener();

    $server.setValue(ADDITIONAL);
    chipfield.sendKeys(Keys.BACK_SPACE);
    assertThat($server.getLastRemovedItem(), Matchers.is(ADDITIONAL));
  }

  @Test
  public void testAdditionalItemRemovedListenerFromServer() {
    $server.allowAdditionalItems(true);
    $server.useNewItemHandler(true);
    $server.addItemRemovedListener();

    $server.setValue(ADDITIONAL);
    $server.setValue();
    assertThat($server.getLastRemovedItem(), Matchers.is(ADDITIONAL));
  }

  @Test
  public void testItemClickedListener() {
    $server.addItemClickedListener();

    $server.setValue(LOREM);
    chipfield.click(0);
    assertThat($server.getLastClickedItem(), Matchers.is(LOREM));
  }

  @Test
  public void testAdditionalItemClickedListener() {
    $server.allowAdditionalItems(true);
    $server.useNewItemHandler(true);
    $server.addItemClickedListener();

    $server.setValue(ADDITIONAL);
    chipfield.click(0);
    assertThat($server.getLastClickedItem(), Matchers.is(ADDITIONAL));
  }

  /**
   * Test that readonly does not allow deleting chips
   *
   * @see https://github.com/FlowingCode/ChipFieldAddon/issues/34
   */
  @Test
  public void testReadOnly() {
    chipfield.selectByText(LOREM);
    assertThat(chipfield.getValue(), isEqualTo(LOREM));
    assertThat($server.getValue(), isEqualTo(LOREM));

    $server.setFieldReadOnly(true);
    chipfield.sendKeys(Keys.BACK_SPACE);
    assertThat(chipfield.getValue(), isEqualTo(LOREM));
    assertThat($server.getValue(), isEqualTo(LOREM));

    $server.allowAdditionalItems(true);
    chipfield.sendKeys(ADDITIONAL, Keys.ENTER);
    assertThat(chipfield.getValue(), isEqualTo(LOREM));
    assertThat($server.getValue(), isEqualTo(LOREM));
  }

  /**
   * Test that setValue changes both server-side and client-side values
   *
   * @see https://github.com/FlowingCode/ChipFieldAddon/issues/25
   */
  @Test
  public void testSetValue() {
    $server.setValue(LOREM);
    assertThat(chipfield.getValue(), isEqualTo(LOREM));
    assertThat($server.getValue(), isEqualTo(LOREM));

    $server.setValue(IPSUM);
    assertThat(chipfield.getValue(), isEqualTo(IPSUM));
    assertThat($server.getValue(), isEqualTo(IPSUM));

    $server.setValue(LOREM, IPSUM);
    assertThat(chipfield.getValue(), isEqualTo(LOREM, IPSUM));
    assertThat($server.getValue(), isEqualTo(LOREM, IPSUM));

    $server.setValue(IPSUM, LOREM);
    assertThat(chipfield.getValue(), isEqualTo(IPSUM, LOREM));
    assertThat($server.getValue(), isEqualTo(IPSUM, LOREM));

    $server.allowAdditionalItems(true);
    $server.setValue(ADDITIONAL);
    assertThat(chipfield.getValue(), isEqualTo(ADDITIONAL));
    assertThat($server.getValue(), isEqualTo(ADDITIONAL));
  }

  /**
   * Test that removeSelectedItem actually removes the item.
   *
   * @see https://github.com/FlowingCode/ChipFieldAddon/issues/27
   */
  @Test
  public void testRemoveSelectedItem() {
    $server.setValue(LOREM, IPSUM);
    $server.removeSelectedItem(LOREM);
    assertThat(chipfield.getValue(), isEqualTo(IPSUM));
    assertThat($server.getValue(), isEqualTo(IPSUM));
  }

  /**
   * Test that valueChangeListener detects when adding a or removing a chip programatically.
   *
   * @see https://github.com/FlowingCode/ChipFieldAddon/issues/32
   */
  @Test
  public void testValueChangeEventOnSetValue() {
    $server.addValueChangeListener();
    $server.allowAdditionalItems(true);
    $server.useNewItemHandler(true);
    $server.setValue(LOREM, ADDITIONAL);
    assertThat($server.getLastValueChange(), isEqualTo(LOREM, ADDITIONAL));
    $server.setValue(LOREM);
    assertThat($server.getLastValueChange(), isEqualTo(LOREM));
  }

  /**
   * Test that valueChangeListener catch the event when adding or removing an additional chip from
   * the UI.
   *
   * @throws InterruptedException
   * @see https://github.com/FlowingCode/ChipFieldAddon/issues/32
   */
  @Test
  public void testValueChangeEventOnAdditionalItem() throws InterruptedException {
    $server.allowAdditionalItems(true);
    $server.useNewItemHandler(true);
    $server.addValueChangeListener();
    $server.setValue(LOREM);
    chipfield.sendKeys(ADDITIONAL, Keys.ENTER);
    assertThat($server.getLastValueChange(), isEqualTo(LOREM, ADDITIONAL));
    chipfield.sendKeys(Keys.BACK_SPACE);
    assertThat($server.getLastValueChange(), isEqualTo(LOREM));
  }

  /**
   * Test that findItemByLabel returns the additional chips
   * https://github.com/FlowingCode/ChipFieldAddon/issues/35
   *
   * @see https://github.com/FlowingCode/ChipFieldAddon/issues/36
   */
  @Test
  public void testFindItemByLabel() {
    $server.useNewItemHandler(true);
    chipfield.sendKeys(ADDITIONAL, Keys.ENTER);
    assertTrue(
        "Additional item was not returned by findItemByLabel",
        $server.hasItemWithLabel(ADDITIONAL));
  }
}
