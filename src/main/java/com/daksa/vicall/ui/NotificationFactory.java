package com.daksa.vicall.ui;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;

public class NotificationFactory {

	public enum Type {
		ERROR, INFO
	}

	public static Notification create(String message, Type type) {
		Div content = new Div();
		content.addClassName("notification");
		switch (type) {
			case ERROR:
				content.addClassName("error");
				break;
			case INFO:
				content.addClassName("info");
				break;
		}
		content.add(new Paragraph(message));

		Button closeButton = new Button();
		closeButton.setIcon(new Icon(VaadinIcon.CLOSE));
		closeButton.addThemeVariants(ButtonVariant.LUMO_ICON, ButtonVariant.LUMO_SMALL);
		content.add(closeButton);

		Notification notification = new Notification(content);
		notification.setDuration(5_000);
		notification.setPosition(Notification.Position.BOTTOM_CENTER);

		closeButton.addClickListener(e -> notification.close());

		return notification;
	}

	public static Notification info(String message) {
		return create(message, Type.INFO);
	}

	public static Notification error(String message) {
		return create(message, Type.ERROR);
	}
}
