package ys.undertow;

import io.undertow.Handlers;
import io.undertow.Undertow;
import io.undertow.server.handlers.PathHandler;
import io.undertow.servlet.Servlets;
import io.undertow.servlet.api.*;
import io.undertow.servlet.util.ImmediateInstanceFactory;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.ServletException;

public class UndertowMain {
    private static final String LISTENER_HOST = "localhost";
    private static final int LISTENER_PORT = 8080;
    private static final String PKG_NAME = "myapp.war";
    private static final String CONTEXT_PATH = "/myapp";
    private static final String WELCOME_PAGE = CONTEXT_PATH + "/static/hello.html";
    private static final String SPRING_DISPATCHER_MAPPING_URL = "/*";
    private static final String SPRING_CONFIG_LOCATION = "ys.rest.config";

    public static void main(String[] args) throws ServletException, InterruptedException {
        Undertow server = configureUndertow();
        server.start();
        //noinspection SynchronizationOnLocalVariableOrMethodParameter
        synchronized (server) {
            server.wait(); // block indefinitely
        }
    }

    private static Undertow configureUndertow() throws ServletException {

        WebApplicationContext context = createSpringWebAppContext(SPRING_CONFIG_LOCATION);

        DeploymentInfo servletBuilder = Servlets.deployment()
                .setClassLoader(UndertowMain.class.getClassLoader())
                .setContextPath(CONTEXT_PATH).setDeploymentName(PKG_NAME)
                .addServlet(createDispatcherServlet(context))
                .addListener(createContextLoaderListener(context));

        DeploymentManager manager = Servlets.defaultContainer().addDeployment(servletBuilder);
        manager.deploy();

        PathHandler path = Handlers.path(Handlers.redirect(WELCOME_PAGE))
                .addPrefixPath(CONTEXT_PATH, manager.start());

        return Undertow.builder()
                .addHttpListener(LISTENER_PORT, LISTENER_HOST)
                .setHandler(path)
                .build();
    }

    private static WebApplicationContext createSpringWebAppContext(String configLocation) {
        AnnotationConfigWebApplicationContext context = new AnnotationConfigWebApplicationContext();
        context.setConfigLocation(configLocation);
        return context;
    }

    private static ListenerInfo createContextLoaderListener(WebApplicationContext context) {
        InstanceFactory<ContextLoaderListener> factory = new ImmediateInstanceFactory<>(new ContextLoaderListener(context));
        return new ListenerInfo(ContextLoaderListener.class, factory);
    }

    private static ServletInfo createDispatcherServlet(WebApplicationContext context) {
        InstanceFactory<DispatcherServlet> factory = new ImmediateInstanceFactory<>(new DispatcherServlet(context));
        return Servlets.servlet("DispatcherServlet", DispatcherServlet.class, factory)
                .addMapping(SPRING_DISPATCHER_MAPPING_URL)
                .setLoadOnStartup(1);
    }
}
