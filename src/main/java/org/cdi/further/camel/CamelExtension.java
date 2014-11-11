package org.cdi.further.camel;


import org.apache.camel.BeanInject;
import org.apache.camel.CamelContext;
import org.apache.camel.Consume;
import org.apache.camel.EndpointInject;
import org.apache.camel.Produce;
import org.apache.camel.PropertyInject;
import org.apache.camel.RoutesBuilder;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.AfterBeanDiscovery;
import javax.enterprise.inject.spi.AfterDeploymentValidation;
import javax.enterprise.inject.spi.AnnotatedType;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.BeforeShutdown;
import javax.enterprise.inject.spi.Extension;
import javax.enterprise.inject.spi.ProcessAnnotatedType;
import javax.enterprise.inject.spi.ProcessInjectionTarget;
import javax.enterprise.inject.spi.WithAnnotations;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Antonin Stefanutti
 */
public class CamelExtension implements Extension {

    private final Set<AnnotatedType<?>> camelBeans = new HashSet<>();

    private DefaultCamelContext context;

    void camelAnnotations(@Observes @WithAnnotations({BeanInject.class, Consume.class, EndpointInject.class, Produce.class, PropertyInject.class}) ProcessAnnotatedType<?> pat) {
        camelBeans.add(pat.getAnnotatedType());
    }

    <T> void camelBeansPostProcessor(@Observes ProcessInjectionTarget<T> pit, BeanManager bm) {
        if (camelBeans.contains(pit.getAnnotatedType()))
            pit.setInjectionTarget(new CamelInjectionTarget<>(pit.getInjectionTarget(), bm));
    }

    void addCamelContextBean(@Observes AfterBeanDiscovery abd, BeanManager bm) {
        Set<Bean<?>> beans = bm.getBeans(CamelContext.class);
        if (beans.isEmpty())
            abd.addBean(new CamelContextBean(bm));
    }

    void configureAndStartContext(@Observes AfterDeploymentValidation adv, BeanManager bm) throws Exception {
        context = getReference(bm, DefaultCamelContext.class);
        context.setRegistry(new CdiCamelRegistry(bm));

        for (Bean<?> bean : bm.getBeans(RoutesBuilder.class))
            context.addRoutes(getReference(bm, RouteBuilder.class, bean));

        context.start();
    }

    void stopCamelContext(@Observes BeforeShutdown bs, BeanManager bm) throws Exception {
        context.stop();
    }

    static <T> T getReference(BeanManager bm, Class<T> type) {
        return getReference(bm, type, bm.resolve(bm.getBeans(type)));
    }

    @SuppressWarnings("unchecked")
    static <T> T getReference(BeanManager bm, Class<T> type, Bean<?> bean) {
        return (T) bm.getReference(bean, type, bm.createCreationalContext(bean));
    }
}
