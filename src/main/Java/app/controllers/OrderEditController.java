package app.controllers;

import app.entities.OrderDTO;
import app.entities.SearchDTO;
import app.exceptions.DatabaseException;
import app.persistence.ConnectionPool;
import app.persistence.OrderMapper;
import io.javalin.http.Context;

import java.util.List;

public class OrderEditController {
    public static void loadOrderEditSite(Context ctx, ConnectionPool connectionPool){
        ctx.sessionAttribute("chosen_order",null);
        ctx.sessionAttribute("costumer_orders",null);
        ctx.render("orderEditSite.html");
    }

    public static void showChosenOrderByID(Context ctx, ConnectionPool connectionPool) {
        try {
            SearchDTO searchDTO = createSearchDTO(ctx, connectionPool);
            List<OrderDTO> customerOrders = OrderMapper.getOrdersBySearchDTO(searchDTO, connectionPool);
            OrderDTO customerOrder = null;
            if (customerOrders.size() == 1){
                customerOrder = customerOrders.get(0);
            }else{
                ctx.attribute("message","To many orders found, contact admin. there is an issue with duplicate order id's in the database");
                loadOrderEditSite(ctx,connectionPool);
            }
            ctx.sessionAttribute("chosen_order", customerOrder);
            ctx.render("orderEditSite.html");
        } catch (DatabaseException e) {
            ctx.attribute("message", "No Orders matched what you entered");
            loadOrderEditSite(ctx, connectionPool);
        }
    }

    public static void getOrdersByNameOrEmail(Context ctx, ConnectionPool connectionPool){
        try {
            SearchDTO searchDTO = createSearchDTO(ctx,connectionPool);
            List<OrderDTO> customerOrders =  OrderMapper.getOrdersBySearchDTO(searchDTO, connectionPool);
            ctx.sessionAttribute("costumer_orders", customerOrders);
            ctx.render("orderEditSite.html");
        }catch (DatabaseException e) {
            ctx.attribute("message", "No Orders matched what you entered");
            loadOrderEditSite(ctx, connectionPool);
        }
    }

    private static SearchDTO createSearchDTO(Context ctx,ConnectionPool connectionPool) {
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
            loadOrderEditSite(ctx,connectionPool);
        } catch (NullPointerException e){
            ctx.attribute("message","something blew up, contact admin");
            loadOrderEditSite(ctx,connectionPool);
        }
        searchInfo.setName(name);
        searchInfo.setEmail(email);
        searchInfo.setOrderID(orderID);
        return searchInfo;
    }

    public static void UpdateOrder(Context ctx, ConnectionPool connectionPool) {
        // TODO - the last stuff!!
        int newLength = Integer.parseInt(ctx.formParam("new_length_input"));
        System.out.println(newLength);
    }

}
