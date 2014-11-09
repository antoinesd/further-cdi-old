package org.cdi.further.camel;

import org.apache.camel.impl.DefaultCamelContext;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.Any;
import javax.enterprise.inject.Default;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.enterprise.util.AnnotationLiteral;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Antonin Stefanutti
 */
class CamelContextBean implements Bean<DefaultCamelContext> {

    private final Set<Annotation> qualifiers = new HashSet<Annotation>(Arrays.asList(new AnnotationLiteral<Any>() {}, new AnnotationLiteral<Default>() {}));

    private final Set<Type> types;

    public CamelContextBean(BeanManager bm) {
        types = bm.createAnnotatedType(DefaultCamelContext.class).getTypeClosure();
    }

    public Class<? extends Annotation> getScope() {
        return ApplicationScoped.class;
    }

    public Set<Annotation> getQualifiers() {
        return Collections.unmodifiableSet(qualifiers);
    }

    public Set<Type> getTypes() {
        return Collections.unmodifiableSet(types);
    }

    public DefaultCamelContext create(CreationalContext<DefaultCamelContext> creational) {
        return new DefaultCamelContext();
    }

    public void destroy(DefaultCamelContext instance, CreationalContext<DefaultCamelContext> creational) {
    }

    public Class<?> getBeanClass() {
        return DefaultCamelContext.class;
    }

    public Set<InjectionPoint> getInjectionPoints() {
        return Collections.emptySet();
    }

    public String getName() { // Only called for @Named bean
        return "";
    }

    public Set<Class<? extends Annotation>> getStereotypes() {
        return Collections.emptySet();
    }

    public boolean isAlternative() {
        return false;
    }

    public boolean isNullable() { // Deprecated since CDI 1.1
        return false;
    }
}
