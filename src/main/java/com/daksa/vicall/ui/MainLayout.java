package com.daksa.vicall.ui;

import com.vaadin.flow.component.HasElement;
import com.vaadin.flow.component.dependency.*;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.page.Viewport;
import com.vaadin.flow.router.RouterLayout;

import javax.annotation.PostConstruct;

@JavaScript("https://cdnjs.cloudflare.com/ajax/libs/jquery/3.4.1/jquery.min.js")
@NpmPackage(value = "openvidu-browser", version = "2.11.0")
@JsModule("./scripts/app.js")
@JsModule("openvidu-browser/static/js/openvidu-browser-2.11.0.js")
@StyleSheet("./styles/style.css")
@CssImport(value = "./styles/custom-form-layout.css", themeFor = "vaadin-form-layout")
@CssImport(value = "./styles/custom-form-item.css", themeFor = "vaadin-form-item")
@Viewport("width=device-width, minimum-scale=1.0, initial-scale=1.0, user-scalable=yes")
public class MainLayout extends Div implements RouterLayout {
	private Div viewContainer;
	private Div contentPanel;

	@PostConstruct
	public void init() {
		addClassName("container");
		viewContainer = new Div();
		viewContainer.addClassName("view-container");
		contentPanel = new Div();
		contentPanel.addClassName("content-panel");
		contentPanel.add(viewContainer);
		add(contentPanel);
	}

	@Override
	public void showRouterLayoutContent(HasElement content) {
		viewContainer.getElement().appendChild(content.getElement());
	}
}
