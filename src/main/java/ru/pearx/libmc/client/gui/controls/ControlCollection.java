package ru.pearx.libmc.client.gui.controls;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import ru.pearx.lib.collections.event.CollectionEventHandler;
import ru.pearx.lib.collections.event.EventCollection;

import java.util.Collection;
import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by mrAppleXZ on 16.04.17 13:12.
 */
@SideOnly(Side.CLIENT)
public class ControlCollection implements Collection<Control>
{
    public Control parent;
    private EventCollection<Control> lst = new EventCollection<>(new ConcurrentLinkedQueue<>(), new CollectionEventHandler<Control>()
    {
        @Override
        public void onAdd(Control control)
        {
            setParentAndInit(control);
            parent.invokeChildAdd(control);
        }

        @Override
        public void onAdd(Collection<? extends Control> t)
        {
            for(Control c : t)
                onAdd(c);
        }

        @Override
        public void onRemove(Object o)
        {
            if(o instanceof Control)
            {
                Control c = (Control)o;
                removeParentAndClose(c);
                parent.invokeChildRemove(c);
            }
        }

        @Override
        public void onRemove(Collection<Object> col)
        {
            for(Object o : col)
                onRemove(o);
        }

        @Override
        public void onClear(Collection<Control> t)
        {
            for(Control c : t)
                removeParentAndClose(c);
            parent.invokeChildClear(t);
        }

        private void setParentAndInit(Control c)
        {
            c.setParent(parent);
            c.invokeInit();
        }

        private void removeParentAndClose(Control c)
        {
            c.invokeClose();
            c.setParent(null);
        }
    });

    public ControlCollection(Control parent)
    {
        this.parent = parent;
    }

    @Override
    public int size()
    {
        return lst.size();
    }

    @Override
    public boolean isEmpty()
    {
        return lst.isEmpty();
    }

    @Override
    public boolean contains(Object o)
    {
        return lst.contains(o);
    }

    @Override
    public Iterator<Control> iterator()
    {
        return lst.iterator();
    }

    @Override
    public Object[] toArray()
    {
        return lst.toArray();
    }

    @Override
    public <T> T[] toArray(T[] a)
    {
        return lst.toArray(a);
    }

    @Override
    public boolean add(Control control)
    {
        return lst.add(control);
    }

    @Override
    public boolean remove(Object o)
    {
        return lst.remove(o);
    }

    @Override
    public boolean containsAll(Collection<?> c)
    {
        return lst.containsAll(c);
    }

    @Override
    public boolean addAll(Collection<? extends Control> c)
    {
        return lst.addAll(c);
    }

    @Override
    public boolean removeAll(Collection<?> c)
    {
        return lst.removeAll(c);
    }

    @Override
    public boolean retainAll(Collection<?> c)
    {
        return lst.retainAll(c);
    }

    @Override
    public void clear()
    {
        lst.clear();
    }
}