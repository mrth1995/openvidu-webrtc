package com.daksa.vicall.ui;

import com.daksa.vicall.model.SessionData;
import com.daksa.vicall.service.SessionService;
import com.vaadin.cdi.annotation.RouteScoped;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

@JsModule("./scripts/app2.js")
@JsModule("./scripts/openvidu.browser-2.11.0.js")
@Route(value = "/")
@RouteScoped
public class MainPage extends HorizontalLayout {

	@Inject
	private SessionService sessionService;

	private String sessionName;
	private String username;

	@PostConstruct
	public void init() {
		TextField name = new TextField();
		TextField sessionName = new TextField();
		Button join = new Button("Join");
		join.addClickListener(buttonClickEvent -> {
			join();
		});
		add(name, sessionName, join);
	}

	private void join() {
		String token = sessionService.join(sessionName, username);
		SessionData data = new SessionData(token, sessionName, username);
		getElement().executeJs("joinSession($0)", data);
	}
}
