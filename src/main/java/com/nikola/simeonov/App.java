package com.nikola.simeonov;

import static org.eclipse.jetty.servlet.ServletContextHandler.NO_SESSIONS;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.jaxrs.json.JacksonJaxbJsonProvider;
import com.nikola.simeonov.bindings.PaymentTransferAppBinder;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class App 
{

    private static final Logger logger = LoggerFactory.getLogger(App.class);

    public static void main( String[] args ) {
        Server server = initServer();

        try {
            server.start();
            initStorage();
            server.join();
        } catch (Exception ex) {
            logger.error("Error occurred while starting Jetty", ex);
            System.exit(1);
        }

        finally {
            server.destroy();
        }
    }

    private static void initStorage() {

    }

    private static Server initServer() {
        Server server = new Server(8080);

        ServletContextHandler servletContextHandler = new ServletContextHandler(NO_SESSIONS);
        servletContextHandler.setContextPath("/");
        server.setHandler(servletContextHandler);

        ServletHolder servletHolder = new ServletHolder(new ServletContainer(resourceConfig()));
        servletContextHandler.addServlet(servletHolder, "/api/*");
        servletHolder.setInitOrder(0);
        servletHolder.setInitParameter(
          "jersey.config.server.provider.classnames",
          "org.glassfish.jersey.jackson.JacksonFeature"
        );
        return server;
    }

    private static ResourceConfig resourceConfig() {
        JacksonJaxbJsonProvider provider = new JacksonJaxbJsonProvider();
        provider.setMapper(new ObjectMapper()
          .findAndRegisterModules()
          .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false));

        return new ResourceConfig()
          .register(new PaymentTransferAppBinder())
          .register(provider)
          .packages("com.nikola.simeonov");
    }
}
