package de.syngenio.vaadin.synergy.layout;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import com.vaadin.ui.AbstractOrderedLayout;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Layout;

import de.syngenio.vaadin.synergy.SynergyView;
import de.syngenio.vaadin.synergy.SynergyView.ItemComponent;
import de.syngenio.vaadin.synergy.layout.AbstractSynergyLayoutFactory.Packing;

public abstract class SynergyLayout extends CustomComponent implements Layout, Layout.AlignmentHandler
{
    private static final String ITEM_STYLE_NAME = "item";
    private AbstractOrderedLayout layout;
    private Packing packing;

    protected SynergyLayout(Packing packing) {
        super();
        this.packing = packing;
        layout = createLayout();
        setCompositionRoot(layout);
    }

    public Packing getPacking() {
        return packing;
    }
    
    private void layoutComponents()
    {
        int componentCount = getComponentCount();
        if (componentCount > 0) {
            for (int i = 0; i < componentCount; ++i) {
                final Component component = getComponent(i);
                if (componentCount == 1) {
                    layoutSingularComponent(component);
                } else if (i == 0) {
                    layoutFirstComponent(component);
                } else if (i == componentCount - 1) {
                    layoutLastComponent(component);
                } else {
                    layoutIntermediateComponent(component);
                }
            }
        }
    }
    
    abstract protected void layoutSingularComponent(Component component);
    abstract protected void layoutFirstComponent(Component component);
    abstract protected void layoutIntermediateComponent(Component component);
    abstract protected void layoutLastComponent(Component component);
    
    abstract protected AbstractOrderedLayout createLayout();
    
    /**
     * Adds a nested SynergyView at the given position to the layout
     * @param subview subview to be added.
     * @param index position for insertion. The components currently in and after the position are shifted to higher indices.    
     */
    public abstract void addSubview(SynergyView subview, int index);
    
    /**
     * Adds a component representing an item to the layout. 
     * Concrete subclasses should override this method to set width and height of the component
     * before calling super.addItemComponent.  
     * @param itemComponent
     */
    public void addItemComponent(Component itemComponent) {
        itemComponent.addStyleName(ITEM_STYLE_NAME);
        itemComponent.addStyleName(getOrientationStyleName());
        addComponent(itemComponent);
    }
    
    @Override
    public void addComponent(Component c)
    {
        layout.addComponent(c);
        layoutComponents();
    }

    public void addComponent(Component c, int index)
    {
        layout.addComponent(c, index);
        layoutComponents();
    }

    @Override
    public void addComponents(Component... components)
    {
        layout.addComponents(components);
        layoutComponents();
    }

    @Override
    public void removeComponent(Component c)
    {
        layout.removeComponent(c);
        layoutComponents();
    }

    @Override
    public void removeAllComponents()
    {
        layout.removeAllComponents();
        layoutComponents();
    }

    @Override
    public void replaceComponent(Component oldComponent, Component newComponent)
    {
        layout.replaceComponent(oldComponent, newComponent);
        layoutComponents();
    }

    @SuppressWarnings("deprecation")
    @Override
    public Iterator<Component> getComponentIterator()
    {
        return layout.getComponentIterator();
    }

    @Override
    public void moveComponentsFrom(ComponentContainer source)
    {
        layout.moveComponentsFrom(source);
        layoutComponents();
    }

    @Deprecated
    @Override
    public void addListener(ComponentAttachListener listener)
    {
        layout.addListener(listener);
    }

    @Deprecated
    @Override
    public void removeListener(ComponentAttachListener listener)
    {
        layout.removeListener(listener);
    }

    @Deprecated
    @Override
    public void addListener(ComponentDetachListener listener)
    {
        layout.addListener(listener);
    }

    @Deprecated
    @Override
    public void removeListener(ComponentDetachListener listener)
    {
        layout.removeListener(listener);
    }

    @Override
    public void addComponentAttachListener(ComponentAttachListener listener)
    {
        layout.addComponentAttachListener(listener);
    }

    @Override
    public void removeComponentAttachListener(ComponentAttachListener listener)
    {
        layout.removeComponentAttachListener(listener);
    }

    @Override
    public void addComponentDetachListener(ComponentDetachListener listener)
    {
        layout.addComponentDetachListener(listener);
    }

    @Override
    public void removeComponentDetachListener(ComponentDetachListener listener)
    {
        layout.removeComponentDetachListener(listener);
    }

    public int getComponentIndex(Component c) {
        return layout.getComponentIndex(c);
    }

    public void setExpandRatio(Component component, float ratio) {
        layout.setExpandRatio(component, ratio);
    }

    public float getExpandRatio(Component component) {
        return layout.getExpandRatio(component);
    }
    
    @Override
    public void setComponentAlignment(Component childComponent, Alignment alignment) {
        layout.setComponentAlignment(childComponent, alignment);
    }

    public Component getComponent(int index) {
        return layout.getComponent(index);
    }
    
    @Override
    public int getComponentCount()
    {
        return layout.getComponentCount();
    }

    @Override
    public Alignment getComponentAlignment(Component childComponent)
    {
        return layout.getComponentAlignment(childComponent);
    }

    @Override
    public void setDefaultComponentAlignment(Alignment defaultComponentAlignment)
    {
        layout.setDefaultComponentAlignment(defaultComponentAlignment);
    }

    @Override
    public Alignment getDefaultComponentAlignment()
    {
        return layout.getDefaultComponentAlignment();
    }

    /**
     * @return the style name (CSS class) that indicates the layout orientation. It will be set on the SynergyView
     */
    public abstract String getOrientationStyleName();
}
