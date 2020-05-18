package com.daksa.vicall.ui;

import com.daksa.vicall.model.JoinSession;
import com.daksa.vicall.model.JoinSessionResponse;
import com.daksa.vicall.model.SessionData;
import com.daksa.vicall.service.SessionService;
import com.daksa.vicall.ui.component.Panel;
import com.daksa.vicall.ui.component.PanelForm;
import com.daksa.vicall.ui.component.ViewFrame;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.vaadin.cdi.annotation.RouteScoped;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Div;
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
public class MainPage extends ViewFrame {
	private static final Logger LOG = LoggerFactory.getLogger(MainPage.class);

	@Inject
	private SessionService sessionService;

	private JoinSessionResponse joinSessionResponse;
	private JoinSession joinSession;
	private PanelForm mainPanel;
	private Panel callPanel;
	private Binder<JoinSession> binder;

	@PostConstruct
	public void init() {
		setPageTitle("Video Call");
		addClassName("container");
		binder = new Binder<>();
		createMainPanel();
		createCallPanel();
	}

	private void createMainPanel() {
		mainPanel = new PanelForm();
		mainPanel.setTitle("DAKSA Video Call");

		TextField name = new TextField();
		binder.forField(name)
				.asRequired("Name cannot be empty")
				.bind(JoinSession::getName, JoinSession::setName);
		TextField roomName = new TextField();
		binder.forField(roomName)
				.asRequired("Name cannot be empty")
				.bind(JoinSession::getSessionName, JoinSession::setSessionName);

		mainPanel.addFormItem(name, "Name");
		mainPanel.addFormItem(roomName, "Room");

		Button join = new Button("Join", e -> join());
		join.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

		mainPanel.addFormControlItem(join);
		add(mainPanel);
	}

	private void createCallPanel() {
		callPanel = new Panel();
		callPanel.setVisible(false);

		Div mainVideo = new Div();
		mainVideo.setId("main-video");
		callPanel.add(mainVideo);
		Div videoContainer = new Div();
		videoContainer.setId("video-container");
		callPanel.add(videoContainer);

		Button leave = new Button("Leave", e -> leave());
		leave.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
		callPanel.getFooter().add(leave);
		add(callPanel);
	}

	private void join() {
		joinSession = new JoinSession();
		if (binder.writeBeanIfValid(joinSession)) {
			LOG.info("Joining session");
			joinSessionResponse = sessionService.join(joinSession);
			SessionData data = new SessionData(joinSessionResponse.getToken(), joinSession.getSessionName(), joinSession.getName());
			try {
				String dataString = Json.getWriter().writeValueAsString(data);
				callPanel.setVisible(true);
				mainPanel.setVisible(false);
				getElement().executeJs("joinSession($0)", dataString);
				LOG.info("Data string {}", dataString);
				LOG.info("Join session response {}", Json.getWriter().withDefaultPrettyPrinter().writeValueAsString(joinSessionResponse));
			} catch (JsonProcessingException e) {
				LOG.error(e.getMessage(), e);
			}
		} else {
			NotificationFactory.info("Mandatory field empty").open();
		}
	}

	private void leave() {
		sessionService.leave(joinSessionResponse.getToken(), joinSession.getSessionName());
		getElement().executeJs("leaveSession()");
		LOG.info("leaving session token: {} {}", joinSessionResponse.getToken(), joinSession.getSessionName());
		callPanel.setVisible(false);
		mainPanel.setVisible(true);
	}
}
