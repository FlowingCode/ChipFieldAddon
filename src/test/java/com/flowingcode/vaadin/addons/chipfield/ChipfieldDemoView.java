package com.flowingcode.vaadin.addons.chipfield;

import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.html.IFrame;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout.Orientation;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.Route;

@SuppressWarnings("serial")
@Route("chipfield")
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
		iframe.getElement().setAttribute("srcdoc", getSrcdoc(DATAPROVIDER_DEMO));
		iframe.setSizeFull();
		layout.addToSecondary(iframe);

		Checkbox codeCB = new Checkbox("Show Source Code");
		codeCB.setValue(true);
		codeCB.addValueChangeListener(cb -> {
			if (cb.getValue()) {
				layout.setSplitterPosition(50);
			} else {
				layout.setSplitterPosition(100);
			}
		});
		Tabs tabs = new Tabs();
		Tab demo1 = new Tab(DATAPROVIDER_DEMO);
		Tab demo2 = new Tab(RESTRICTED_DEMO);
		Tab demo3 = new Tab(DISABLED_DEMO);
		Tab demo4 = new Tab(BINDER_DEMO);
		tabs.setWidthFull();
		tabs.add(demo1, demo2, demo3, demo4, codeCB);
		add(tabs, layout);
		
		setSizeFull();
		
		tabs.addSelectedChangeListener(e -> {
			this.removeAll();
			switch (e.getSelectedTab().getLabel()) {
			case DATAPROVIDER_DEMO:
				iframe.getElement().setAttribute("srcdoc", getSrcdoc(DATAPROVIDER_DEMO));
				layout.addToPrimary(new DataProviderDemo());
				layout.addToSecondary(iframe);
				add(tabs, layout);
				break;
			case "Restricted":
				iframe.getElement().setAttribute("srcdoc", getSrcdoc(RESTRICTED_DEMO));
				layout.addToPrimary(new RestrictedDemo());
				layout.addToSecondary(iframe);
				add(tabs, layout);
				break;
			case "Disabled":
				iframe.getElement().setAttribute("srcdoc", getSrcdoc(DISABLED_DEMO));
				layout.addToPrimary(new DisabledDemo());
				layout.addToSecondary(iframe);
				add(tabs, layout);
				break;
			case "Binder":
				iframe.getElement().setAttribute("srcdoc", getSrcdoc(BINDER_DEMO));
				layout.addToPrimary(new BinderDemo());
				layout.addToSecondary(iframe);
				add(tabs, layout);
				break;
			default:
				add(tabs, new DataProviderDemo());
				break;
			}
		});
	}

	private String getSrcdoc(String demo) {
		String response;
		switch (demo) {
		case DATAPROVIDER_DEMO:
			response = "<html style=\"overflow-y:hidden; height:100%;\"><body style=\"overflow-y: scroll; height:100%;\"><script src=\"http://gist-it.appspot.com/"
					+ DATAPROVIDER_SOURCE + "\"></script></body></html>";
			break;
		case RESTRICTED_DEMO:
			response = "<html style=\"overflow-y:hidden; height:100%;\"><body style=\"overflow-y: scroll; height:100%;\"><script src=\"http://gist-it.appspot.com/"
					+ RESTRICTED_SOURCE + "\"></script></body></html>";
			break;
		case DISABLED_DEMO:
			response = "<html style=\"overflow-y:hidden; height:100%;\"><body style=\"overflow-y: scroll; height:100%;\"><script src=\"https://gist-it.appspot.com/"
					+ DISABLED_SOURCE + "\"></script></body></html>";
			break;
		case BINDER_DEMO:
			response = "<html style=\"overflow-y:hidden; height:100%;\"><body style=\"overflow-y: scroll; height:100%;\"><script src=\"https://gist-it.appspot.com/"
					+ BINDER_SOURCE + "\"></script></body></html>";
			break;
		default:
			response = "<html style=\"overflow-y:hidden; height:100%;\"><body style=\"overflow-y: scroll; height:100%;\"><script src=\"https://gist-it.appspot.com/"
					+ DATAPROVIDER_SOURCE + "\"></script></body></html>";
			break;
		}
		return response;
	}
}
