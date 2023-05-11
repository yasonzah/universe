package com.fluffy.universe;

import com.fluffy.universe.controllers.*;
import com.fluffy.universe.exceptions.HttpException;
import com.fluffy.universe.middleware.CSRFFilter;
import com.fluffy.universe.middleware.ModelFilter;
import com.fluffy.universe.utils.ApplicationAccessManager;
import com.fluffy.universe.utils.Configuration;
import io.javalin.Javalin;
import io.javalin.http.staticfiles.Location;
import io.javalin.testing.JavalinTest;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;

import static io.javalin.apibuilder.ApiBuilder.*;
import static io.javalin.testing.TestUtil.*;
import static org.junit.jupiter.api.Assertions.*;

public class MainTest {
    private static Javalin app;

    @BeforeAll
    public static void setUp() {
        JavalinTest.register(app = Javalin.create(configuration -> {
            configuration.addStaticFiles("/public", Location.CLASSPATH);
            configuration.accessManager(new ApplicationAccessManager());
        }));
        app.routes(() -> {
            before(ModelFilter::initializeModel);
            before(CSRFFilter::verifyToken);
            before(CSRFFilter::generateToken);
            exception(HttpException.class, ctx -> ctx.status(((HttpException) ctx.error()).getStatusCode()));
            get("test-route", ctx -> ctx.result("Test"));
        });
        new HomeController(app);
        new UserController(app);
        new PostController(app);
        new CommentController(app);
        app.start(0);
    }

    @AfterAll
    public static void tearDown() {
        app.stop();
    }

    @Test
    public void testExampleRoute() {
                String response = httpGet("/test-route").body();
        assertEquals("Test", response);
    }
}
