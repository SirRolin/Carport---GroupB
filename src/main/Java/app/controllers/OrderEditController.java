package app.controllers;

import app.persistence.ConnectionPool;
import io.javalin.http.Context;

public class OrderEditController {
    public static void loadOrderEditSite(Context ctx, ConnectionPool connectionPool){
        ctx.sessionAttribute("chosen_order",null);
        ctx.sessionAttribute("costumer_orders",null);

        ctx.render("orderEditSite.html");
    }
    public static void loadOrderWithID(Context ctx,ConnectionPool connectionPool){

    }
    public static void showOrderWithID(Context ctx,ConnectionPool connectionPool){

    }
}
