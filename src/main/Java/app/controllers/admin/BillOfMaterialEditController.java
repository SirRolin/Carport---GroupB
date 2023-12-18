package app.controllers.admin;

import app.controllers.OrderEditController;
import app.persistence.ConnectionPool;
import io.javalin.Javalin;
import io.javalin.http.Context;

public class BillOfMaterialEditController {

    public static void addRenders(Javalin app,ConnectionPool connectionPool){
        app.post("/showBillOfMaterial",ctx -> BillOfMaterialEditController.showBillOfMaterial(ctx, connectionPool));
        app.post("/back_to_order",ctx -> OrderEditController.loadOrderEditSite(ctx,connectionPool));
        app.post("/submitEditedBillOfMaterial", ctx -> editBillOfMaterial(ctx,connectionPool));
    }
    public static void showBillOfMaterial(Context ctx, ConnectionPool connectionPool) {
        ctx.render("billOfMaterialEditSite.html");
    }

    public static void printBillOfMaterial(){

    }
    public static void editBillOfMaterial(Context ctx, ConnectionPool connectionPool){
        // TODO FILL OUT THIS LOGIC:
        ctx.render("billOfMaterialEditSite.html");
    }
}
