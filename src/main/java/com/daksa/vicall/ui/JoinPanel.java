package com.daksa.vicall.ui;

import com.daksa.vicall.model.JoinSession;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;

public class JoinPanel extends VerticalLayout {

	private Binder<JoinSession> joinSessionBinder;

	public JoinPanel(ComponentEventListener<ClickEvent<Button>> eventListener) {
		VerticalLayout div = new VerticalLayout();
		div.setId("join-panel");
		joinSessionBinder = new Binder<>();
		TextField name = new TextField();
		name.setId("name");
		TextField sessionName = new TextField();
		sessionName.setId("session-name");
		joinSessionBinder.forField(sessionName)
				.asRequired()
				.bind(JoinSession::getSessionName, JoinSession::setSessionName);
		joinSessionBinder.forField(name)
				.asRequired()
				.bind(JoinSession::getName, JoinSession::setName);
		Button join = new Button("Join");
		join.addClickListener(eventListener);
		div.add(name, sessionName, join);
		add(div);
	}

	public Binder<JoinSession> getJoinSessionBinder() {
		return joinSessionBinder;
	}
}
