package org.cdi.further.camel;

import org.apache.camel.CamelContext;
import org.apache.camel.impl.DefaultCamelBeanPostProcessor;
import org.apache.camel.util.ObjectHelper;

import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.enterprise.inject.spi.InjectionTarget;
import java.util.Set;

/**
 * @author Antonin Stefanutti
 */
class CamelInjectionTarget<T> implements InjectionTarget<T> {

    final DefaultCamelBeanPostProcessor processor;

    final InjectionTarget<T> delegate;

    CamelInjectionTarget(InjectionTarget<T> target, final BeanManager bm) {
        delegate = target;
        processor = new DefaultCamelBeanPostProcessor() {
            public CamelContext getOrLookupCamelContext() {
                return CamelExtension.getReference(bm, CamelContext.class);
            }
        };
    }

    @Override
    public void inject(T instance, CreationalContext<T> ctx) {
        try {
            processor.postProcessBeforeInitialization(instance, "");
        } catch (Exception cause) {
            throw ObjectHelper.wrapRuntimeCamelException(cause);
        }
        delegate.inject(instance, ctx);
    }

    @Override
    public void postConstruct(T instance) {
        delegate.postConstruct(instance);
    }

    @Override
    public void preDestroy(T instance) {
        delegate.preDestroy(instance);
    }

    @Override
    public T produce(CreationalContext<T> ctx) {
        return delegate.produce(ctx);
    }

    @Override
    public void dispose(T instance) {
        delegate.dispose(instance);
    }

    @Override
    public Set<InjectionPoint> getInjectionPoints() {
        return delegate.getInjectionPoints();
    }
}