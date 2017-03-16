package worlds;

import javax.servlet.annotation.WebServlet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.util.HierarchicalContainer;
import com.vaadin.data.util.ObjectProperty;
import com.vaadin.data.util.PropertysetItem;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

import de.syngenio.vaadin.synergy.SynergyView;
import de.syngenio.vaadin.synergy.builder.SynergyBuilder;
import de.syngenio.vaadin.synergy.layout.AbstractSynergyLayoutFactory.Packing;
import de.syngenio.vaadin.synergy.layout.VerticalSynergyLayout;

@Theme("default")
@WorldDescription(prose="Demonstrates the dynamic nature of the navigation by letting the user add new items themselves", tags={"vertical", "hierarchical", "dynamical", "inline"})
public class WorldOfVerticalDynamicalNavigationUI extends WorldUI
{
    @WebServlet(value = "/vertical/dynamical/*", asyncSupported = true)
    @VaadinServletConfiguration(productionMode = false, ui = WorldOfVerticalDynamicalNavigationUI.class)
    public static class Servlet extends VaadinServlet {
    }

    private static final Logger log = LoggerFactory.getLogger(WorldOfVerticalDynamicalNavigationUI.class);
    
    @Override
    protected void init(VaadinRequest request)
    {
        final HierarchicalContainer container = new SynergyBuilder() {{
            addItem(item("names").asGroup().withCaption("Names"));
        }}.build();
        
        VerticalLayout vlayout = new VerticalLayout();
        
        PropertysetItem item = new PropertysetItem();
        final ObjectProperty<String> propertyName = new ObjectProperty<String>("");
        item.addItemProperty("name", propertyName);
        FormLayout form = new FormLayout();
        TextField nameField = new TextField("Name");
        nameField.setImmediate(true);
        form.addComponent(nameField);
        FieldGroup binder = new FieldGroup(item);
        binder.setBuffered(false);
        binder.bind(nameField, "name");
        vlayout.addComponent(form);
        propertyName.addValueChangeListener(new ValueChangeListener() {

            @Override
            public void valueChange(ValueChangeEvent event)
            {
                final String name = propertyName.getValue();
                if (!"".equals(name)) {
                    log.debug("form value change: name="+name);
                    new SynergyBuilder(container, "names") {{
                        addItem(item().withCaption(name));
                    }}.build();
                    propertyName.setValue("");
                }
            }
        });
        
        HorizontalLayout hlayout = new HorizontalLayout();
        hlayout.setSizeFull();
        Panel navpnl = new Panel(); 
        navpnl.setHeight("100%");
        navpnl.setWidthUndefined();
        SynergyView synergyViewSpaceAfter = new SynergyView(new VerticalSynergyLayout.NestedFactory(Packing.SPACE_AFTER), container);
        synergyViewSpaceAfter.setCaption(Packing.SPACE_AFTER.name());
        synergyViewSpaceAfter.setIcon(FontAwesome.ALIGN_LEFT);
        synergyViewSpaceAfter.setWidthUndefined();
        synergyViewSpaceAfter.setHeight("100%");
//        synergyView.setWidth("30%");
        navpnl.setContent(synergyViewSpaceAfter);
        hlayout.addComponent(navpnl);
        hlayout.setExpandRatio(navpnl, 0f);
        
        hlayout.addComponent(panel);
        hlayout.setExpandRatio(panel, 1f);

        vlayout.addComponent(hlayout);

        setContent(vlayout);
        
        setNavigator(new Navigator(this, panel));
        final NavigationView genericView = new NavigationView();
        getNavigator().addView("", genericView);
        getNavigator().addView("view", genericView);
    }
    
    private static class NavigationView extends Label implements View {
        @Override
        public void enter(ViewChangeEvent event)
        {
            setValue(event.getParameters());
        }
    }
}
