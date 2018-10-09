package com.flowingcode.vaadin.addons;

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
