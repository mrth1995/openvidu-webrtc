package com.daksa.vicall.ui;

import com.vaadin.flow.component.HasElement;
import com.vaadin.flow.component.dependency.*;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.router.RouterLayout;

import javax.annotation.PostConstruct;

@JavaScript("https://cdnjs.cloudflare.com/ajax/libs/jquery/3.4.1/jquery.min.js")
@NpmPackage(value = "openvidu-browser", version = "^2.11.0")
@JsModule("./scripts/app.js")
@JsModule("openvidu-browser/static/js/openvidu-browser-2.11.0.js")
@CssImport("./styles/style.css")
public class MainLayout extends Div implements RouterLayout {

	@PostConstruct
	public void init() {
	}

	@Override
	public void showRouterLayoutContent(HasElement content) {
		getElement().appendChild(content.getElement());
	}
}
