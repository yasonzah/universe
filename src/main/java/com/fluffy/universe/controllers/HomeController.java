package com.fluffy.universe.controllers;

import com.fluffy.universe.models.Role;
import io.javalin.Javalin;
import io.javalin.http.Context;

public class HomeController extends Controller {
    public HomeController(Javalin application) {
        super("", application);
    }

    public void homePage(Context context) {
        render(context, "/views/pages/home.vm");
    }

    @Override
    public void registerRoutes(Javalin application) {
        application.get("/", this::homePage, Role.values());
        application.get("/home", this::homePage, Role.values());
    }
}
