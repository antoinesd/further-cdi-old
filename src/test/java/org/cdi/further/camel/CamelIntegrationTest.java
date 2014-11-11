package org.cdi.further.camel;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.util.CamelContextHelper;
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
import java.io.FileOutputStream;
import java.util.concurrent.TimeUnit;

import static org.apache.camel.component.mock.MockEndpoint.assertIsSatisfied;

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
                        .resolve("org.apache.camel:camel-core",
                                 "org.apache.camel:camel-sjms",
                                 "org.apache.activemq:activemq-core")
                        .withTransitivity().as(JavaArchive.class))
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml")
                .addAsServiceProvider(Extension.class, CamelExtension.class);
    }

    @Test
    public void sendMessage(DefaultCamelContext context) throws Exception {
        MockEndpoint mock = CamelContextHelper.getMandatoryEndpoint(context, "mock:output", MockEndpoint.class);
        mock.expectedMessageCount(1);
        mock.expectedBodiesReceived("test");

        FileOutputStream out = new FileOutputStream("inputDir/message");
        try {
            out.write("test".getBytes());
        } finally {
            out.close();
        }

        assertIsSatisfied(10L, TimeUnit.SECONDS, mock);
    }

    private static class JmsToMockRoute extends RouteBuilder {

        @Override
        public void configure() {
            from("sjms:queue:outputDest").log("Message [${body}] received").to("mock:output");
        }
    }
}
