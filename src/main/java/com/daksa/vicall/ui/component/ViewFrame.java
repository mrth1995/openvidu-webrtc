package com.daksa.vicall.ui.component;

import com.vaadin.flow.component.*;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.page.PendingJavaScriptResult;
import com.vaadin.flow.router.HasDynamicTitle;
import com.vaadin.flow.router.RouterLayout;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class ViewFrame extends Composite<Div> implements HasStyle, HasDynamicTitle {
    private final static Logger LOG = LoggerFactory.getLogger(ViewFrame.class);
    private String pageTitle;
    private String menu;
    private String subMenu;
    private Map<String, List<String>> parametersMap;
    public static final String TITLE_SUFFIX = ".title";

    @SuppressWarnings("OverridableMethodCallInConstructor")
    public ViewFrame() {
        this.subMenu = getClass().getSimpleName() + TITLE_SUFFIX;
        setClassName("view-frame");
    }

    @PostConstruct
    public void postConstruct() {
        LOG.info("init {}", getClass().getSimpleName());
    }

    @PreDestroy
    public void preDestroy() {
        LOG.info("close {}", getClass().getSimpleName());
    }


    @Override
    public String getPageTitle() {
        return pageTitle;
    }

    public void setPageTitle(String menu, String subMenu) {
        this.menu = menu;
        this.subMenu = subMenu;
        this.pageTitle = subMenu;
    }

    public void setPageTitle(String pageTitle) {
        this.pageTitle = pageTitle;
    }

    public void add(Component... components) {
        getContent().add(components);
    }

    public void addScript(String script) {
        HtmlComponent scriptElement = new HtmlComponent("script");
        scriptElement.getElement().setAttribute("src", script);
        add(scriptElement);
    }

    public void onPageLoad(RouterLayout layout) {

    }

    public void setHeightFull() {
        getStyle().set("height", "100%");
    }

    public PendingJavaScriptResult executeJs(String expression,
                                             Serializable... parameters) {
        return UI.getCurrent().getPage().executeJs(expression, parameters);
    }

    public String getMenu() {
        return menu;
    }

    public String getSubMenu() {
        return subMenu;
    }

    public void setMenu(String menu) {
        this.menu = menu;
    }

    public void setSubMenu(String subMenu) {
        this.subMenu = subMenu;
    }
}
