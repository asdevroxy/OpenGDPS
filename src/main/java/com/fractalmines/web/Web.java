package com.fractalmines.web;

import com.fractalmines.config.Config;
import com.sun.net.httpserver.HttpServer;
import lombok.Getter;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;
import org.reflections.util.ConfigurationBuilder;

import java.lang.reflect.Constructor;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

@Getter
@SuppressWarnings("FieldMayBeFinal")
public class Web {
    private HttpServer server;
    private Map<String, RequestHandler> registeredHandlers = new HashMap<>();


    public void init() {
        Config.Networking networking = Config.getInstance().getNetworking();

        try {
            server = HttpServer.create(new InetSocketAddress(networking.getIp(), networking.getPort()), networking.getCapacity());
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }

        detectWebContexts();
        server.start();
    }

    public void close() {
        server.stop(5);
    }

    private void detectWebContexts() {
        server.createContext("/", exchange -> {
            System.out.println("=== INCOMING REQUEST ===");
            System.out.println("Method: " + exchange.getRequestMethod());
            System.out.println("Path: " + exchange.getRequestURI());
            System.out.println("Headers: " + exchange.getRequestHeaders());
            System.out.println("Remote: " + exchange.getRemoteAddress());
            System.out.println("Body: " + new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8));
            System.out.println("========================");

            exchange.sendResponseHeaders(404, -1);
        });

        loadGameHandlers();
        //loadDashboardHandlers();

        System.out.printf("Registered %d handlers total.%n", registeredHandlers.size());
    }

    private void createContext(String path, RequestHandler handler) {
        server.createContext(path, (exchange) -> {
            try {
                handler.handle(exchange);
            } catch (Exception e) {
                e.printStackTrace();
                System.err.println("Failed to handle request %s, sending -1 (generic failure): " + e.getMessage());

                handler.sendResponse(exchange, "-1");
            }
        });
    }

    private void loadGameHandlers() {
        System.out.println("Loading game server handlers...");
        Reflections reflections = new Reflections(
                new ConfigurationBuilder().forPackages("com.fractalmines.web.game.handlers")
        );

        Set<Class<?>> rawHandlers = reflections.get(Scanners.TypesAnnotated.with(WebContext.class).asClass());
        rawHandlers.forEach(rawHandler -> {
            WebContext webContext = rawHandler.getDeclaredAnnotation(WebContext.class);

            if (RequestHandler.class.isAssignableFrom(rawHandler)) {
                try {
                    Constructor<?> ctor = rawHandler.getDeclaredConstructor(WebContext.class);
                    RequestHandler handler = (RequestHandler) ctor.newInstance(webContext);

                    String contextPath = "/%s.php".formatted(webContext.path());
                    createContext(contextPath, handler);
                    registeredHandlers.put(contextPath, handler);
                    Stream.of(webContext.alternatePaths()).forEach(alt -> {
                        String path = "/%s.php".formatted(alt);
                        createContext(path, handler);
                        registeredHandlers.put(path, handler);
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                    throw new RuntimeException(e);
                }
            }
        });
    }

    private void loadDashboardHandlers() {
        System.out.println("Loading dashboard handlers...");
        Reflections reflections = new Reflections(
                new ConfigurationBuilder().forPackages("com.fractalmines.web.dashboard.handlers")
        );

        Set<Class<?>> rawHandlers = reflections.get(Scanners.TypesAnnotated.with(WebContext.class).asClass());
        rawHandlers.forEach(rawHandler -> {
            WebContext webContext = rawHandler.getDeclaredAnnotation(WebContext.class);

            if (RequestHandler.class.isAssignableFrom(rawHandler)) {
                try {
                    Constructor<?> ctor = rawHandler.getDeclaredConstructor(WebContext.class);
                    RequestHandler handler = (RequestHandler) ctor.newInstance(webContext);

                    String contextPath = "/%s".formatted(webContext.path());
                    createContext(contextPath, handler);
                    registeredHandlers.put(contextPath, handler);
                    Stream.of(webContext.alternatePaths()).forEach(alt -> {
                        String path = "/%s".formatted(alt);
                        createContext(path, handler);
                        registeredHandlers.put(path, handler);
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                    throw new RuntimeException(e);
                }
            }
        });
    }

}
