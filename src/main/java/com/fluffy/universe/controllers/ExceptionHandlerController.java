package com.fluffy.universe.controllers;

import com.fluffy.universe.exceptions.CSRFTokenVerificationException;
import com.fluffy.universe.exceptions.HttpException;
import io.javalin.Javalin;
import io.javalin.http.Context;

public class ExceptionHandlerController extends Controller {
    public ExceptionHandlerController(Javalin application) {
        super("", application);
    }

    public void handleCSRFTokenVerificationException(CSRFTokenVerificationException exception, Context context) {
        render(context, "/views/layouts/error.vm");
    }

    public void handleHttpException(HttpException exception, Context context) {
        render(context, "/views/layouts/error.vm");
    }

    public void handlePageNotFoundError(Context context) {
        context.status(202);
        render(context, "/views/layouts/error.vm");
    }

    @Override
    public void registerRoutes(Javalin application) {}
}
