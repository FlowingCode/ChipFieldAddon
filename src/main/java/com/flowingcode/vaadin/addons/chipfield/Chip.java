/*-
 * #%L
 * ChipField Addon
 * %%
 * Copyright (C) 2018 Flowing Code
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
package com.flowingcode.vaadin.addons.chipfield;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.JavaScript;
import com.vaadin.flow.component.dependency.NpmPackage;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;

@SuppressWarnings("serial")
@Tag("paper-chip")
@NpmPackage(value = "@polymer/iron-a11y-keys", version = "^3.0.1")
@NpmPackage(value = "@polymer/iron-a11y-keys-behavior", version = "^3.0.1")
@NpmPackage(value = "@polymer/iron-icons", version = "^3.0.1")
@NpmPackage(value = "@polymer/paper-icon-button", version = "^3.0.2")
@NpmPackage(value = "@polymer/paper-input", version = "^3.0.1")
@NpmPackage(value = "@polymer/paper-item", version = "^3.0.1")
@NpmPackage(value = "@polymer/paper-listbox", version = "^3.0.1")
@NpmPackage(value = "@polymer/paper-material", version = "^3.0.1")
@NpmPackage(value = "@polymer/paper-ripple", version = "^3.0.1")
@NpmPackage(value = "@polymer/paper-styles", version = "^3.0.1")
@JavaScript("./paper-chip.js")
final class Chip extends Component {

	private String label;
	private boolean closable;
	private Icon icon;

	Chip(String label, boolean closable, Icon icon) {
		this.setLabel(label);
		this.setClosable(closable);
		this.setIcon(icon);
	}

	Chip(String chipText) {
		this(chipText, true, null);
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.getElement().setAttribute("label", label);
		this.label = label;
	}

	public boolean isClosable() {
		return closable;
	}

	public void setClosable(boolean closable) {
		if (closable) {
			this.getElement().setAttribute("closable", closable);
		}
		this.closable = closable;
	}

	public Icon getIcon() {
		return icon;
	}

	public void setIcon(Icon icon) {
		if (icon != null) {
			Span s = new Span();
			s.setClassName("chip-background");
			s.getElement().setAttribute("slot", "avatar");
			s.add(icon);
			this.getElement().appendChild(s.getElement());
		}
		this.icon = icon;
	}

}
