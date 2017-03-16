package worlds;

import helpers.WorldHelper;

import javax.servlet.annotation.WebServlet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.HorizontalLayout;

import de.syngenio.vaadin.synergy.SynergyView;
import de.syngenio.vaadin.synergy.layout.AbstractSynergyLayoutFactory.Packing;
import de.syngenio.vaadin.synergy.layout.VerticalSynergyLayout;

@Theme("default")
@WorldDescription(prose="Demonstrates a single vertical sidebar of large navigation icons", tags={"vertical", "stacked"})
public class WorldOfImageSidebarNavigationUI extends WorldUI
{
    @WebServlet(value = "/vertical/images/*", asyncSupported = true)
    @VaadinServletConfiguration(productionMode = false, ui = WorldOfImageSidebarNavigationUI.class)
    public static class Servlet extends VaadinServlet {
    }

    private static final Logger log = LoggerFactory.getLogger(WorldOfImageSidebarNavigationUI.class);
    
    @Override
    protected void init(VaadinRequest request)
    {
        super.init(request);
        HorizontalLayout hlayout = new HorizontalLayout();
        hlayout.setSizeFull();
        SynergyView synergyView = new SynergyView(new VerticalSynergyLayout.NestedFactory(Packing.EXPAND), WorldHelper.getImageNavigation2());
//        synergyView.setSizeUndefined();
        synergyView.setWidth("200px");
        synergyView.setHeight("100%");
        hlayout.addComponent(synergyView);
        hlayout.setExpandRatio(synergyView, 0f);
        
        hlayout.addComponent(panel);
        hlayout.setExpandRatio(panel, 1f);

        setContent(hlayout);
    }
}
