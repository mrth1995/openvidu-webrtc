package com.daksa.vicall.controller;

import com.daksa.vicall.model.SessionData;
import com.daksa.vicall.service.SessionService;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.olivia.webutil.json.Json;
import org.apache.commons.lang3.StringUtils;
import org.primefaces.PrimeFaces;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;

@Named
@ViewScoped
public class IndexController implements Serializable {
	private static final long serialVersionUID = 1L;
	private static final Logger LOG = LoggerFactory.getLogger(IndexController.class);

	@Inject
	private SessionService sessionService;
	@Inject
	private FacesContext facesContext;

	private String name;
	private String roomName;
	private String token;

	public void join() {
		token = sessionService.join(roomName, name);
		SessionData data = new SessionData(token, roomName, name);
		if (StringUtils.isNotEmpty(token)) {
			try {
				String dataRaw = Json.getWriter().writeValueAsString(data);
				PrimeFaces.current().executeScript("joinSession('" + dataRaw + "')");
			} catch (JsonProcessingException e) {
				LOG.error(e.getMessage(), e);
			}
		} else {
			facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Warning", "Token is null"));
		}
	}

	public void leave() {
		if (StringUtils.isNotEmpty(token) && StringUtils.isNotEmpty(roomName)) {
			sessionService.leave(token, roomName);
			PrimeFaces.current().executeScript("leaveSession()");
		}
	}

	public void remote() {
		facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "Clicked!"));
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getRoomName() {
		return roomName;
	}

	public void setRoomName(String roomName) {
		this.roomName = roomName;
	}
}
