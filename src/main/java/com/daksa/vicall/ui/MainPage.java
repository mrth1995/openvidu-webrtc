package com.daksa.vicall.ui;

import com.daksa.vicall.model.JoinSession;
import com.daksa.vicall.model.SessionData;
import com.daksa.vicall.service.SessionService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.vaadin.cdi.annotation.RouteScoped;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.Route;
import io.olivia.webutil.json.Json;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

@Route(value = "", layout = MainLayout.class)
@RouteScoped
public class MainPage extends Panel {
	private static final Logger LOG = LoggerFactory.getLogger(MainPage.class);

	@Inject
	private SessionService sessionService;

	private Binder<JoinSession> joinSessionBinder;
	private VerticalLayout joinPanel;
	private VerticalLayout roomPanel;
	private String token;
	private JoinSession joinSession;

	@PostConstruct
	public void init() {
		joinPanel = createJoinPanel();
		add(joinPanel);
	}

	private VerticalLayout createJoinPanel() {
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
		join.addClickListener(buttonClickEvent -> {
			join();
		});
		div.add(name, sessionName, join);
		return div;
	}

	private VerticalLayout createCallPanel() {
		VerticalLayout session = new VerticalLayout();
		session.setId("session");
		Div sessionHeader = new Div();
		sessionHeader.setId("session-header");
		H3 sessionTitle = new H3();
		sessionTitle.setId("session-title");
		sessionHeader.add(sessionTitle);
		session.add(sessionHeader);
		Div mainVideo = new Div();
		mainVideo.setId("main-video");
		Paragraph nickName = new Paragraph();
		nickName.setId("nickName");
		Video video = new Video();
		mainVideo.add(nickName, video);
		session.add(mainVideo);
		Div videoContainer = new Div();
		videoContainer.setId("video-container");
		session.add(videoContainer);
		Button leave = new Button("Leave");
		leave.addClickListener(buttonClickEvent -> {
			leave();
		});
		session.add(leave);
		return session;
	}

	private void join() {
		joinPanel.setVisible(false);
		roomPanel = createCallPanel();
		add(roomPanel);
		LOG.info("Joining session");
		joinSession = new JoinSession();
		joinSessionBinder.writeBeanIfValid(joinSession);
		token = sessionService.join(joinSession);
		SessionData data = new SessionData(token, joinSession.getSessionName(), joinSession.getName());
		try {
			String dataString = Json.getWriter().writeValueAsString(data);
			getElement().executeJs("joinSession($0)", dataString);
		} catch (JsonProcessingException e) {
			LOG.error(e.getMessage(), e);
		}
	}

	private void leave() {
		roomPanel.setVisible(false);
		joinPanel.setVisible(true);
		sessionService.leave(token, joinSession.getSessionName());
	}
}
