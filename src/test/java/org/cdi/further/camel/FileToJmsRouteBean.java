package org.cdi.further.camel;

import org.apache.camel.builder.RouteBuilder;

/**
 * @author Antonin Stefanutti
 */
class FileToJmsRouteBean extends RouteBuilder {

    @Override
    public void configure() {
        from("file:inputDir?initialDelay=1000&delay=1000")
                .convertBodyTo(String.class)
                .to("sjms:queue:outputDest");
    }
}