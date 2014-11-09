package org.cdi.further.camel;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.camel.PropertyInject;
import org.apache.camel.component.sjms.SjmsComponent;
import org.apache.camel.impl.DefaultComponent;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Named;

/**
 * @author Antonin Stefanutti
 */
class JmsComponentFactoryBean {

    @PropertyInject(value = "jms.queue.maxConnections", defaultValue = "10")
    Integer maxConnections;

    @Produces
    @Named("sjms")
    @ApplicationScoped
    DefaultComponent sjmsComponent() {
        SjmsComponent component = new SjmsComponent();
        component.setConnectionFactory(new ActiveMQConnectionFactory("vm://broker?broker.persistent=false&broker.useJmx=false"));
        component.setConnectionCount(maxConnections);
        return component;
    }
}