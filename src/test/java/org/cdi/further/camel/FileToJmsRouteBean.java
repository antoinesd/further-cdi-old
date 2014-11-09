package org.cdi.further.camel;

import org.apache.camel.builder.RouteBuilder;

/**
 * @author Antonin Stefanutti
 */
class FileToJmsRouteBean extends RouteBuilder {

    @Override
    public void configure() {
        from("file:inputDir?delay=1000").to("sjms:queue:outputDest");
    }
}