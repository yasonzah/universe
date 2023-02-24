package com.fluffy.universe.controllers;

import io.javalin.Javalin;
import io.javalin.http.Context;

public class PostController extends Controller {
    public PostController(Javalin application) {
        super("/posts", application);
    }

    public void indexPage(Context context) {
        render(context, "/");
    }

    public void createPage(Context context) {
        render(context, "/views/pages/models/post/create-or-edit.vm");
    }

    public void editPage(Context context) {
        render(context, "/views/pages/models/post/create-or-edit.vm");
    }

    public void show(Context context) {
        render(context, "/views/pages/models/post/show.vm");
    }

    public void store(Context context) {

    }

    public void update(Context context) {

    }

    public void destroy(Context context) {

    }

    @Override
    public void registerRoutes(Javalin application) {
        application.get(baseUrl, this::indexPage);
        application.get(baseUrl + "/create", this::createPage);
        application.get(baseUrl + "/{post}/edit", this::editPage);
        application.get(baseUrl + "/{post}", this::show);
        application.post(baseUrl, this::store);
        application.put(baseUrl + "/{post}", this::update);
        application.post(baseUrl + "/{post}", this::destroy);
    }
}
