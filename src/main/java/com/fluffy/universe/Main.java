package com.fluffy.universe;

import com.fluffy.universe.controllers.*;
import com.fluffy.universe.exceptions.CSRFTokenVerificationException;
import com.fluffy.universe.exceptions.HttpException;
import com.fluffy.universe.middleware.CSRFFilter;
import com.fluffy.universe.middleware.ModelFilter;
import com.fluffy.universe.models.Role;
import com.fluffy.universe.models.User;
import com.fluffy.universe.utils.Configuration;
import com.fluffy.universe.utils.SessionUtils;
import io.javalin.Javalin;
import io.javalin.http.staticfiles.Location;

import java.io.File;

public class Main {
    public static void main(String[] args) {
        Javalin application = Javalin.create(configuration -> {
            configuration.addStaticFiles("/public", Location.CLASSPATH);
            configuration.accessManager((handler, context, routeRoles) -> {
                if (routeRoles.size() == 0) {
                    handler.handle(context);
                    return;
                }

                Role role = Role.GUEST;
                User user = SessionUtils.getCurrentUser(context);
                if (user != null) {
                    role = Role.getRoleById(user.getRoleId());
                }

                if (routeRoles.contains(role)) {
                    handler.handle(context);
                    return;
                }

                if (role == Role.GUEST) {
                    context.redirect("/sign-in");
                } else {
                    context.redirect("/");
                }
            });

            Configuration.load(new File(args[0]));
        });
        ExceptionHandlerController exceptionHandlerController = new ExceptionHandlerController(application);

        application
                .before(ModelFilter::initializeModel)
                .before(CSRFFilter::verifyToken)
                .before(CSRFFilter::generateToken)
                .exception(HttpException.class, exceptionHandlerController::handleHttpException)
                .error(404, exceptionHandlerController::handlePageNotFoundError)
                .error(500, exceptionHandlerController::handleInternalServerError);

        new HomeController(application);
        new UserController(application);
        new PostController(application);
        new CommentController(application);
        application.start(7000);
    }
}
