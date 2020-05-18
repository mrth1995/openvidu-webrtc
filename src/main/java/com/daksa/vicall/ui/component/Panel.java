package com.daksa.vicall.ui.component;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.Div;

public class Panel extends Div {

	private Div header;
	private Div body;
	private Div footer;

	public Panel() {
		init(null);
	}

	public Panel(String title) {
		init(title);
	}

	public Panel(Component... components) {
		init(null);
		add(components);
	}

	public Panel(String title, Component... components) {
		init(title);
		add(components);
	}

	private void init(String title) {
		addClassName("panel");

		header = new Div();
		header.addClassName("panel-header");
		header.setText(title);
		getElement().appendChild(header.getElement());

		body = new Div();
		body.addClassName("panel-body");
		getElement().appendChild(body.getElement());

		footer = new Div();
		footer.addClassName("panel-footer");
		getElement().appendChild(footer.getElement());
	}

	public void setTitle(String title) {
		header.setText(title);
	}

	public void add(Component... components) {
		body.add(components);
	}

	public Div getHeader() {
		return header;
	}

	public Div getBody() {
		return body;
	}

	public Div getFooter() {
		return footer;
	}

	@Override
	protected void onAttach(AttachEvent attachEvent) {
		if (header.getElement().getChildCount() == 0) {
			header.setVisible(false);
		}
		if (footer.getElement().getChildCount() == 0) {
			footer.setVisible(false);
		}
	}
}
