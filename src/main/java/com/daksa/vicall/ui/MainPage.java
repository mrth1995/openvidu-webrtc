package com.daksa.vicall.ui;

import com.daksa.vicall.model.JoinSession;
import com.daksa.vicall.model.JoinSessionResponse;
import com.daksa.vicall.model.SessionData;
import com.daksa.vicall.service.SessionService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.vaadin.cdi.annotation.RouteScoped;
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

	private JoinSessionResponse joinSessionResponse;
	private JoinSession joinSession;
	private JoinPanel joinPanel;
	private CallPanel callPanel;

	@PostConstruct
	public void init() {
		joinPanel = new JoinPanel(e -> join());
		add(joinPanel);
	}

	private void join() {
		joinSession = new JoinSession();
		if (joinPanel.getJoinSessionBinder().writeBeanIfValid(joinSession)) {
			removeAll();
			callPanel = new CallPanel(e -> leave());
			add(callPanel);
			LOG.info("Joining session");
			joinSessionResponse = sessionService.join(joinSession);
			SessionData data = new SessionData(joinSessionResponse.getToken(), joinSession.getSessionName(), joinSession.getName());
			try {
				String dataString = Json.getWriter().writeValueAsString(data);
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
		removeAll();
		joinPanel = new JoinPanel(e -> join());
		add(joinPanel);
		LOG.info("leaving session token: {} {}", joinSessionResponse.getToken(), joinSession.getSessionName());
	}
}
