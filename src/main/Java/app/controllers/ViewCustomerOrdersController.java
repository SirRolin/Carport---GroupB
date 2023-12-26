package app.controllers;

import app.persistence.ConnectionPool;
import io.javalin.Javalin;

public class ViewCustomerOrdersController {


    public static void addRenders(Javalin app, ConnectionPool connectionPool) {
        app.get("/findCustomerOrderByID", ctx -> )
    }
}
