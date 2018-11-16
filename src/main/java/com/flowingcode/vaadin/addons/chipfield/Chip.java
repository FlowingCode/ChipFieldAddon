package com.flowingcode.vaadin.addons.chipfield;

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

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;

@SuppressWarnings("serial")
@Tag("paper-chip")
@HtmlImport("bower_components/paper-chip/paper-chip.html")
public class Chip extends Component {
	
	private String label;
	private boolean closable;
	private Icon icon;

    public Chip(String label, boolean closable, Icon icon) {
    	this.setLabel(label);
    	this.setClosable(closable);
    	this.setIcon(icon);
    }

	public Chip(String chipText) {
		this(chipText,true,null);
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
		if (closable)
			this.getElement().setAttribute("closable", closable);
		this.closable = closable;
	}

	public Icon getIcon() {
		return icon;
	}

	public void setIcon(Icon icon) {
		if (icon!=null) {
			Span s = new Span();
			s.setClassName("chip-background");
			s.getElement().setAttribute("slot", "avatar");
			s.add(icon);
			getElement().appendChild(s.getElement());
		}
		this.icon = icon;
	}
    
}
