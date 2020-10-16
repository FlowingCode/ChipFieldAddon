package com.flowingcode.vaadin.addons.chipfield;

import com.flowingcode.vaadin.addons.DemoLayout;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.html.IFrame;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout.Orientation;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.Route;

@SuppressWarnings("serial")
@StyleSheet("context://frontend/styles/demo-styles.css")
@Route(value = "chipfield", layout = DemoLayout.class)
public class ChipfieldDemoView extends VerticalLayout {

	private static final String DATAPROVIDER_DEMO = "Data Provider";
	private static final String RESTRICTED_DEMO = "Restricted";
	private static final String DISABLED_DEMO = "Disabled";
	private static final String BINDER_DEMO = "Binder";
	private static final String DATAPROVIDER_SOURCE = "https://github.com/FlowingCode/ChipFieldAddon/blob/master/src/test/java/com/flowingcode/vaadin/addons/chipfield/DataProviderDemo.java";
	private static final String RESTRICTED_SOURCE = "https://github.com/FlowingCode/ChipFieldAddon/blob/master/src/test/java/com/flowingcode/vaadin/addons/chipfield/RestrictedDemo.java";
	private static final String DISABLED_SOURCE = "https://github.com/FlowingCode/ChipFieldAddon/blob/master/src/test/java/com/flowingcode/vaadin/addons/chipfield/DisabledDemo.java";
	private static final String BINDER_SOURCE = "https://github.com/FlowingCode/ChipFieldAddon/blob/master/src/test/java/com/flowingcode/vaadin/addons/chipfield/BinderDemo.java";

	public ChipfieldDemoView() {
		SplitLayout layout = new SplitLayout();
		layout.setOrientation(Orientation.HORIZONTAL);
		layout.addToPrimary(new DataProviderDemo());
		layout.setSizeFull();
		IFrame iframe = new IFrame();
		iframe.getElement().setAttribute("frameborder", "0");
		iframe.getElement().setAttribute("srcdoc", getSrcdoc(DATAPROVIDER_SOURCE));
		iframe.setSizeFull();
		layout.addToSecondary(iframe);

		Tabs tabs = new Tabs();
		Tab demo1 = new Tab(DATAPROVIDER_DEMO);
		Tab demo2 = new Tab(RESTRICTED_DEMO);
		Tab demo3 = new Tab(DISABLED_DEMO);
		Tab demo4 = new Tab(BINDER_DEMO);
		tabs.setWidthFull();
		tabs.add(demo1, demo2, demo3, demo4);

		Checkbox orientationCB = new Checkbox("Toggle Orientation");
		orientationCB.setValue(true);
		orientationCB.addClassName("smallcheckbox");
		orientationCB.addValueChangeListener(cb -> {
			if (cb.getValue()) {
				layout.setOrientation(Orientation.HORIZONTAL);
			} else {
				layout.setOrientation(Orientation.VERTICAL);
			}
			layout.setSplitterPosition(50);
			layout.getPrimaryComponent().getElement().setAttribute("style", "width: 100%; height: 100%");
			iframe.setSizeFull();
		});
		Checkbox codeCB = new Checkbox("Show Source Code");
		codeCB.setValue(true);
		codeCB.addClassName("smallcheckbox");
		codeCB.addValueChangeListener(cb -> {
			if (cb.getValue()) {
				layout.setSplitterPosition(50);
				orientationCB.setEnabled(true);
			} else {
				layout.setSplitterPosition(100);
				orientationCB.setEnabled(false);
			}
		});
		HorizontalLayout footer = new HorizontalLayout();
		footer.setWidthFull();
		footer.setJustifyContentMode(JustifyContentMode.END);
		footer.add(codeCB, orientationCB);
		add(tabs, layout, footer);

		setSizeFull();
		
		tabs.addSelectedChangeListener(e -> {
			removeAll();
			switch (e.getSelectedTab().getLabel()) {
			case DATAPROVIDER_DEMO:
				iframe.getElement().setAttribute("srcdoc", getSrcdoc(DATAPROVIDER_SOURCE));
				layout.addToPrimary(new DataProviderDemo());
				layout.addToSecondary(iframe);
				add(tabs, layout, footer);
				break;
			case RESTRICTED_DEMO:
				iframe.getElement().setAttribute("srcdoc", getSrcdoc(RESTRICTED_SOURCE));
				layout.addToPrimary(new RestrictedDemo());
				layout.addToSecondary(iframe);
				add(tabs, layout, footer);
				break;
			case DISABLED_DEMO:
				iframe.getElement().setAttribute("srcdoc", getSrcdoc(DISABLED_SOURCE));
				layout.addToPrimary(new DisabledDemo());
				layout.addToSecondary(iframe);
				add(tabs, layout, footer);
				break;
			case BINDER_DEMO:
				iframe.getElement().setAttribute("srcdoc", getSrcdoc(BINDER_SOURCE));
				layout.addToPrimary(new BinderDemo());
				layout.addToSecondary(iframe);
				add(tabs, layout, footer);
				break;
			default:
				iframe.getElement().setAttribute("srcdoc", getSrcdoc(DATAPROVIDER_SOURCE));
				layout.addToPrimary(new DataProviderDemo());
				layout.addToSecondary(iframe);
				add(tabs, layout, footer);
				break;
			}
		});
	}

	private String getSrcdoc(String sourceUrl) {
		return "<html style=\"overflow-y:hidden; height:100%;\"><body style=\"overflow-y: scroll; height:100%;\"><script src=\"https://gist-it.appspot.com/"
				+ sourceUrl + "\"></script></body></html>";
	}
}
