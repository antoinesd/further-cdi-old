package org.cdi.further.metrics;

import com.codahale.metrics.annotation.Timed;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.enterprise.inject.spi.AnnotatedConstructor;
import javax.enterprise.inject.spi.AnnotatedField;
import javax.enterprise.inject.spi.AnnotatedMethod;
import javax.enterprise.inject.spi.AnnotatedParameter;
import javax.enterprise.inject.spi.AnnotatedType;
import javax.enterprise.util.AnnotationLiteral;
import javax.enterprise.util.Nonbinding;

/**
 * @author Antoine Sabot-Durand
 */
public class TimedAnnotatedType implements AnnotatedType<Timed> {
    
    private static class TimedAnnotatedMethod implements AnnotatedMethod<Timed> {
        private AnnotatedMethod<Timed> delegate;
        private static Set<Annotation> nonBindingSet;
        {
            nonBindingSet = new HashSet<>();
            nonBindingSet.add(new AnnotationLiteral<Nonbinding>() {});
        }

        TimedAnnotatedMethod(AnnotatedMethod<Timed> delegate) {
            this.delegate = delegate;
        }

        @Override
        public Method getJavaMember() {
            return delegate.getJavaMember();
        }

        @Override
        public List<AnnotatedParameter<Timed>> getParameters() {
            return delegate.getParameters();
        }

        @Override
        public boolean isStatic() {
            return delegate.isStatic();
        }

        @Override
        public AnnotatedType<Timed> getDeclaringType() {
            return delegate.getDeclaringType();
        }

        @Override
        public Type getBaseType() {
            return delegate.getBaseType();
        }

        @Override
        public Set<Type> getTypeClosure() {
            return delegate.getTypeClosure();
        }

        @Override
        public <T extends Annotation> T getAnnotation(Class<T> annotationType) {
            if (annotationType.equals(Nonbinding.class))
                return (T) new AnnotationLiteral<Nonbinding>() {};
            return delegate.getAnnotation(annotationType);
        }

        @Override
        public Set<Annotation> getAnnotations() {
            return nonBindingSet;
        }

        @Override
        public boolean isAnnotationPresent(Class<? extends Annotation> annotationType) {
            if (annotationType.equals(Nonbinding.class))
                return true;
            return delegate.isAnnotationPresent(annotationType);
        }
    }
    

    private AnnotatedType<Timed> delegate;
    private Set<AnnotatedMethod<? super Timed>> methods;

    public TimedAnnotatedType(AnnotatedType<Timed> delegate) {
        this.delegate = delegate;
        methods = new HashSet<>();
        for(AnnotatedMethod<? super Timed> am : delegate.getMethods())
            methods.add(new TimedAnnotatedMethod((AnnotatedMethod<Timed>) am));
    }

    @Override
    public Class<Timed> getJavaClass() {
        return delegate.getJavaClass();
    }

    @Override
    public Set<AnnotatedConstructor<Timed>> getConstructors() {
        return delegate.getConstructors();
    }

    @Override
    public Set<AnnotatedMethod<? super Timed>> getMethods() {
        return methods;
    }

    @Override
    public Set<AnnotatedField<? super Timed>> getFields() {
        return delegate.getFields();
    }

    @Override
    public Type getBaseType() {
        return delegate.getBaseType();
    }

    @Override
    public Set<Type> getTypeClosure() {
        return delegate.getTypeClosure();
    }

    @Override
    public <T extends Annotation> T getAnnotation(Class<T> annotationType) {
        return delegate.getAnnotation(annotationType);
    }

    @Override
    public Set<Annotation> getAnnotations() {
        return delegate.getAnnotations();
    }

    @Override
    public boolean isAnnotationPresent(Class<? extends Annotation> annotationType) {
        return delegate.isAnnotationPresent(annotationType);
    }
}
