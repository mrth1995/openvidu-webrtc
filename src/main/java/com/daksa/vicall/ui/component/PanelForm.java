package com.daksa.vicall.ui.component;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;

import java.util.List;

public class PanelForm extends Panel {

	private FormLayout formLayout;
	private HorizontalLayout formControl;

	public PanelForm() {
		init();
	}

	public PanelForm(String title) {
		super(title);
		init();
	}

	private void init() {
		formLayout = new FormLayout();
		formLayout.setResponsiveSteps(
				new FormLayout.ResponsiveStep("0", 1, FormLayout.ResponsiveStep.LabelsPosition.TOP),
				new FormLayout.ResponsiveStep("700px", 2, FormLayout.ResponsiveStep.LabelsPosition.TOP)
		);
		getBody().add(formLayout);

		formControl = new HorizontalLayout();
		formControl.addClassName("form-control");
		getFooter().add(formControl);
	}

	public FormLayout getForm() {
		return formLayout;
	}

	public HorizontalLayout getFormControl() {
		return formControl;
	}

	public FormLayout.FormItem addFormValue(Component component, String label) {
		Div div = new Div(component);
		div.addClassName("form-value");
		return formLayout.addFormItem(div, label);
	}

	public FormLayout.FormItem addFormItem(Component component, String label) {
		return formLayout.addFormItem(component, label);
	}

	public void addSubtitle(String subtitle) {
		addFormItem(new H3(subtitle), null);
		addFormItem(new Hr(), null);
	}

	public void addSeparator() {
		addFormItem(new Hr(), null);
	}

	public void setResponsiveStep(FormLayout.ResponsiveStep responsiveStep) {
		formLayout.setResponsiveSteps(responsiveStep);
	}

	public void setResponsiveStep(List<FormLayout.ResponsiveStep> responsiveStep) {
		formLayout.setResponsiveSteps(responsiveStep);
	}

	public void addFormItem(Component component, String label, int colspan) {
		FormLayout.FormItem formItem = formLayout.addFormItem(component, label);
		formItem.getElement().setAttribute("colspan", String.valueOf(colspan));
	}

	public void addFormControlItem(Component... components) {
		formControl.add(components);
	}

	public void clear() {
		formLayout.getChildren().forEach(component -> {
			if (component instanceof FormLayout.FormItem) {
				FormLayout.FormItem formItem = (FormLayout.FormItem) component;
				formItem.getChildren().forEach(component1 -> {
					if (component1 instanceof HasValue) {
						((HasValue) component1).clear();
					}
				});
			}
		});
	}
}
