package org.cdi.further.camel;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.enterprise.inject.spi.Extension;
import java.io.FileNotFoundException;

/**
 * @author Antonin Stefanutti
 */
@RunWith(Arquillian.class)
public class CamelIntegrationTest {

    @Deployment
    public static Archive<?> createTestArchive() throws FileNotFoundException {
        return ShrinkWrap.create(WebArchive.class, "test.war")
                .addPackage("org.cdi.further.camel")
                .addAsLibraries(Maven.resolver()
                        .loadPomFromFile("pom.xml")
                        .resolve("org.apache.deltaspike.core:deltaspike-core-api",
                                "org.apache.camel:camel-core",
                                "org.apache.camel:camel-sjms",
                                "org.apache.activemq:activemq-core")
                        .withTransitivity().as(JavaArchive.class))
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml")
                .addAsServiceProvider(Extension.class, CamelExtension.class);
    }

    @Test
    public void sendMessage() {

    }
}
