package app.controllers;

import app.persistence.ConnectionPool;
import io.javalin.http.Context;

public class OrderEditController {
    public static void loadOrderEditSite(Context ctx, ConnectionPool connectionPool){

        ctx.render("orderEditSite.html");
    }
}
