package org.cdi.further.camel;

import org.apache.camel.spi.Registry;

import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author Antonin Stefanutti
 */
public class CdiCamelRegistry implements Registry {

    private final BeanManager bm;

    public CdiCamelRegistry(BeanManager bm) {
        this.bm = bm;
    }

    @Override
    public <T> T lookupByNameAndType(String name, Class<T> type) {
        return getReference(bm, type, bm.resolve(bm.getBeans(name)));
    }

    @Override
    public <T> Map<String, T> findByTypeWithName(Class<T> type) {
        Map<String, T> references = new HashMap<>();
        for (Bean<?> bean : bm.getBeans(type))
            if (bean.getName() != null)
                references.put(bean.getName(), this.<T>getReference(bm, type, bean));

        return references;
    }

    @Override
    public <T> Set<T> findByType(Class<T> type) {
        return getReference(bm, type, bm.resolve(bm.getBeans(type)));
    }

    @Override
    public Object lookup(String name) {
        return lookupByNameAndType(name, Object.class);
    }

    @Override
    public <T> T lookup(String name, Class<T> type) {
        return lookupByNameAndType(name, type);
    }

    @Override
    public <T> Map<String, T> lookupByType(Class<T> type) {
        return findByTypeWithName(type);
    }

    public Object lookupByName(String name) {
        return lookupByNameAndType(name, Object.class);
    }

    @SuppressWarnings("unchecked")
    <T> T getReference(BeanManager bm, Type type, Bean<?> bean) {
        if (bean != null)
            return (T) bm.getReference(bean, type, bm.createCreationalContext(bean));
        else
            return null;
    }
}