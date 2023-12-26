package app.controllers;

import app.entities.OrderDTO;
import app.entities.SearchDTO;
import app.exceptions.DatabaseException;
import app.persistence.ConnectionPool;
import app.persistence.OrderMapper;
import io.javalin.Javalin;
import io.javalin.http.Context;

import java.util.List;

public class ViewCustomerOrdersController {


    public static void addRenders(Javalin app, ConnectionPool connectionPool) {
        app.post("/findCustomerOrderByID", ctx -> showCustomerOrderByID(ctx, connectionPool));
        app.post("//findCostumerOrderByEmail", ctx -> showTheCustomerOrdersByEmail(ctx,connectionPool));
        app.post("/getBillOfMaterialIfPaid", ctx -> renderBillOfMaterialIfPaid(ctx,connectionPool));
        app.get("/viewOrderCustomer", ctx -> renderOrderViewSite(ctx,connectionPool));
    }

    private static void renderBillOfMaterialIfPaid(Context ctx, ConnectionPool connectionPool) {
        // TODO
    }

    private static void showTheCustomerOrdersByEmail(Context ctx, ConnectionPool connectionPool) {
        try {
            SearchDTO searchDTO = createSearchDTO(ctx,connectionPool);
            List<OrderDTO> customerOrders =  OrderMapper.getOrdersBySearchDTO(searchDTO, connectionPool);
            if(customerOrders.size() > 1) {
                ctx.sessionAttribute("the_costumers_orders", customerOrders);
                ctx.render("orderViewSite.html");
            }else {
                ctx.sessionAttribute("customer_order", customerOrders.get(0));
                ctx.render("orderViewSite.html");
            }
        }catch (DatabaseException e) {
            ctx.attribute("message", "No Orders matched what you entered");
            backToOrderViewSite(ctx);
        }
    }

    private static void renderOrderViewSite(Context ctx, ConnectionPool connectionPool) {
        ctx.sessionAttribute("customer_order",null);
        ctx.sessionAttribute("the_costumers_orders",null);
        ctx.render("orderViewSite.html");
    }
    private static void showCustomerOrderByID(Context ctx, ConnectionPool connectionPool) {
        try {
            SearchDTO searchDTO = createSearchDTO(ctx, connectionPool);
            List<OrderDTO> customerOrders = OrderMapper.getOrdersBySearchDTO(searchDTO, connectionPool);
            OrderDTO customerOrder = null;
            if (customerOrders.size() == 1){
                customerOrder = customerOrders.get(0);
            }else{
                ctx.attribute("message","To many orders found, contact admin. there is an issue with duplicate order id's in the database");
                backToOrderViewSite(ctx);
            }
            ctx.sessionAttribute("customer_order", customerOrder);
            ctx.render("orderViewSite.html");
        } catch (DatabaseException e) {
            ctx.attribute("message", "No Orders matched what you entered");
            backToOrderViewSite(ctx);
        }
    }

    private static void backToOrderViewSite(Context ctx) {
        ctx.render("orderViewSite.html");
    }

    private static SearchDTO createSearchDTO(Context ctx, ConnectionPool connectionPool) {
        SearchDTO searchInfo = new SearchDTO();
        String name = "";
        String email = "";
        int orderID = 0;
        if(ctx.formParam("name_input") != null){
            name = ctx.formParam("name_input").toLowerCase();
        }
        if(ctx.formParam("email_input") != null){
            email = ctx.formParam("email_input").toLowerCase();
        }
        try {
            if (ctx.formParam("orderID_input") != null) {
                orderID = Integer.parseInt(ctx.formParam("orderID_input"));
            }
        } catch (NumberFormatException e){
            ctx.attribute("message","You did not enter a number");
            backToOrderViewSite(ctx);
        } catch (NullPointerException e){
            ctx.attribute("message","something blew up, contact admin");
            backToOrderViewSite(ctx);
        }
        searchInfo.setName(name);
        searchInfo.setEmail(email);
        searchInfo.setOrderID(orderID);
        return searchInfo;
    }
}
