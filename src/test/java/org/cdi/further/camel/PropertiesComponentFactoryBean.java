package org.cdi.further.camel;

import org.apache.camel.component.properties.PropertiesComponent;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Named;

/**
 * @author Antonin Stefanutti
 */
class PropertiesComponentFactoryBean {

    @Produces
    @Named("properties")
    @ApplicationScoped
    PropertiesComponent propertiesComponent() {
        PropertiesComponent pc = new PropertiesComponent();
        pc.setLocation("classpath:camel.properties");
        return pc;
    }
}