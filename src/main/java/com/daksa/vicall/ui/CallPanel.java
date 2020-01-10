package com.daksa.vicall.ui;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class CallPanel extends VerticalLayout {

	public CallPanel(ComponentEventListener<ClickEvent<Button>> eventListener) {
		HorizontalLayout session = new HorizontalLayout();
		session.setId("session");
		Div sessionHeader = new Div();
		sessionHeader.setId("session-header");
		H3 sessionTitle = new H3();
		sessionTitle.setId("session-title");
		sessionHeader.add(sessionTitle);
		session.add(sessionHeader);
		Div mainVideo = new Div();
		mainVideo.setId("main-video");
		session.add(mainVideo);
		Div videoContainer = new Div();
		videoContainer.setId("video-container");
		session.add(videoContainer);
		Button leave = new Button("Leave");
		leave.addClickListener(eventListener);
		VerticalLayout verticalLayout = new VerticalLayout(session, leave);
		add(verticalLayout);
	}
}
