package app.controllers;

import app.Main;
import app.engine.Engine;
import app.entities.MaterialDTO;
import app.entities.OrderDTO;
import app.entities.SearchDTO;
import app.entities.Status;
import app.exceptions.DatabaseException;
import app.persistence.ConnectionPool;
import app.persistence.OrderItemMapper;
import app.persistence.OrderMapper;
import io.javalin.Javalin;
import io.javalin.http.Context;
import jakarta.servlet.http.HttpSession;
import org.eclipse.jetty.server.Request;

import java.util.List;

public class ViewCustomerOrdersController {


    public static void addRenders(Javalin app, ConnectionPool connectionPool) {
        app.post("/findCustomerOrderByID", ctx -> showCustomerOrderByID(ctx, connectionPool));
        app.post("//findCostumerOrderByEmail", ctx -> showTheCustomerOrdersByEmail(ctx,connectionPool));
        //app.post("/getBillOfMaterialIfPaid", ctx -> renderBillOfMaterialIfPaid(ctx,connectionPool));
        app.get("/viewOrderCustomer", ctx -> renderOrderViewSite(ctx,connectionPool));
        //app.post("/backToIndex", ctx -> backToIndex(ctx) );
    }
    // found a smarter way, added a methord in main to clear the entire session
/*
    private static void backToIndex(Context ctx) {
        ctx.sessionAttribute("customer_order",null);
        ctx.sessionAttribute("the_costumers_orders",null);
        ctx.sessionAttribute("customerBillOfMaterial",null);
        ctx.sessionAttribute("customer_svg",null);
        ctx.render("index.html");
    }
*/
    private static void renderBillOfMaterialIfPaid(Context ctx, ConnectionPool connectionPool) {
        OrderDTO costumerOrder = ctx.sessionAttribute("customer_order");
        List<MaterialDTO> billOfMaterials = ctx.sessionAttribute("bill_of_materials");
        List<MaterialDTO> billOfMaterialListFromDB = null;
        List<MaterialDTO> listOfMaterials = null;
        if (billOfMaterials == null) {
            try {
                billOfMaterialListFromDB = OrderItemMapper.getOrderItemsByOrderID(costumerOrder.getId(), connectionPool);
                billOfMaterials = billOfMaterialListFromDB;
            } catch (DatabaseException e) {
                ctx.attribute("message", "was unable get to find order items" + e.getMessage());
                ctx.render("billOfMaterialEditSite.html");
                return;
            }

        }
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
        ctx.sessionAttribute("customerBillOfMaterial",null);
        ctx.sessionAttribute("customer_svg",null);
        ctx.render("orderViewSite.html");
    }
    private static void showCustomerOrderByID(Context ctx, ConnectionPool connectionPool) {
        try {
            SearchDTO searchDTO = createSearchDTO(ctx, connectionPool);
            List<OrderDTO> customerOrders = OrderMapper.getOrdersBySearchDTO(searchDTO, connectionPool);
            List<MaterialDTO> billOfMaterialListFromDB = null;
            OrderDTO customerOrder = null;
            if (customerOrders.size() == 1){
                customerOrder = customerOrders.get(0);
            }else{
                ctx.attribute("message","To many orders found, contact admin. there is an issue with duplicate order id's in the database");
                backToOrderViewSite(ctx);
            }
            ctx.sessionAttribute("customer_order", customerOrder);
            ctx.attribute("customer_svg",customerOrder.getSvg());
            try {
                assert customerOrder != null;
                billOfMaterialListFromDB = OrderItemMapper.getOrderItemsByOrderID(customerOrder.getId(), connectionPool);
            } catch (DatabaseException e) {
                ctx.attribute("message", "was unable get to find order items" + e.getMessage());
                ctx.render("billOfMaterialEditSite.html");
                return;
            }
            if(!billOfMaterialListFromDB.isEmpty() && customerOrder.getStatus().equals(Status.paid)){
                ctx.attribute("customerBillOfMaterial",billOfMaterialListFromDB);
            }else {
                ctx.attribute("notPaidMessage","Your bill of material is ready to be delivered. But to get access, you will need to pay for your order. please follow the instructions from the email");
            }
            if(billOfMaterialListFromDB.isEmpty()){
                ctx.attribute("notPaidMessage","Your bill of material has not been generated yet. please contact customer service if you think think is a mistake");
            }
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
