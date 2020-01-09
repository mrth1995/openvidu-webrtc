package com.daksa.vicall.ui;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.Div;

public class Panel extends Div {

	public Panel() {
		addClassName("panel");
	}

	public Panel(Component... components) {
		this();
		add(components);
	}
}
