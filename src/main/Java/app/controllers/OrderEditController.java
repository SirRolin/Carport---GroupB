package app.controllers;

import app.calculator.Calculator;
import app.entities.MaterialDTO;
import app.entities.OrderDTO;
import app.entities.SearchDTO;
import app.entities.Status;
import app.exceptions.DatabaseException;
import app.persistence.ConnectionPool;
import app.persistence.MaterialsMapper;
import app.persistence.OrderItemMapper;
import app.persistence.OrderMapper;
import io.javalin.Javalin;
import io.javalin.http.Context;

import java.util.List;

public class OrderEditController {

    public static void addRenders(Javalin app, ConnectionPool connectionPool){
        app.post("/submitOrderID", ctx -> OrderEditController.showChosenOrderByID(ctx,connectionPool));
        app.get("/submitOrderID", ctx -> OrderEditController.loadOrderEditSite(ctx,connectionPool));
        app.post("/submitCostumerName", ctx -> OrderEditController.getOrdersByNameOrEmail(ctx,connectionPool));
        app.post("/submitCostumerEmail", ctx -> OrderEditController.getOrdersByNameOrEmail(ctx,connectionPool));
        app.post("/updateOrder",ctx -> OrderEditController.UpdateOrder(ctx,connectionPool));
        //app.post("/generateBillOfMaterial",ctx -> OrderEditController.generateBillOfMaterial(ctx,connectionPool));
    }
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
                backToOrderSite(ctx);
            }
            ctx.sessionAttribute("chosen_order", customerOrder);
            ctx.render("orderEditSite.html");
        } catch (DatabaseException e) {
            ctx.attribute("message", "No Orders matched what you entered");
            backToOrderSite(ctx);
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
            backToOrderSite(ctx);
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
            backToOrderSite(ctx);
        } catch (NullPointerException e){
            ctx.attribute("message","something blew up, contact admin");
            backToOrderSite(ctx);
        }
        searchInfo.setName(name);
        searchInfo.setEmail(email);
        searchInfo.setOrderID(orderID);
        return searchInfo;
    }

    public static void UpdateOrder(Context ctx, ConnectionPool connectionPool) {
        // takes all the potentially changed info about the current order and makes a new orderDTO objects and sets it as the new session attribute:
        OrderDTO currentOrder = ctx.sessionAttribute("chosen_order");
        List<MaterialDTO> billOfMaterials = ctx.sessionAttribute("bill_of_materials");
        int newLength = Integer.parseInt(ctx.formParam("new_length_input"));
        int newWidth = Integer.parseInt(ctx.formParam("new_width_input"));
        int newShedLength = Integer.parseInt(ctx.formParam("new_shed_length_input"));
        int newShedWidth = Integer.parseInt(ctx.formParam("new_shed_width_input"));
        int newSlopedegrees = Integer.parseInt(ctx.formParam("new_slopedegrees_input"));
        String hasAssemblerTmp = ctx.formParam("new_has_assembler_input");
        boolean newHasAssembler = currentOrder.isHasAssembler();
        if(hasAssemblerTmp != null && hasAssemblerTmp.equals("on")) {
            newHasAssembler = true;
        }else{
            newHasAssembler = false;
        }
        Double newPrice = Double.parseDouble(ctx.formParam("new_price_input"));
        String editedStatus = ctx.formParam("edited_order_status");
        Status currentStatus = Status.valueOf(editedStatus);
        /*
            switch (editedStatus){
            case "initialised" -> currentStatus = Status.initialised;
            case "processing" -> currentStatus = Status.processing;
            case "accepted" -> currentStatus = Status.accepted;
            case "waiting_for_customer" -> currentStatus = Status.waiting_for_customer;
            case "paid" -> currentStatus = Status.paid;
            }
        */
        String newNotice = ctx.formParam("new_notice_input");
        OrderDTO currentOrderEdited = new OrderDTO(currentOrder.getId(),newLength,newWidth,newShedLength,newShedWidth,newSlopedegrees,newHasAssembler,newPrice,currentStatus,currentOrder.getSvg(),currentOrder.getName(),currentOrder.getEmail(),currentOrder.getDate(),newNotice);
        try {
            OrderMapper.updateOrder(connectionPool, currentOrderEdited);
            /*if(billOfMaterials != null) {
                MaterialsMapper.deleteOrderItemsByOrderID(currentOrder.getId(), connectionPool);
                OrderItemMapper.saveBillOfMaterials(billOfMaterials, currentOrder.getId(), connectionPool);
            }*/
            ctx.sessionAttribute("chosen_order",currentOrderEdited);
            ctx.attribute("saved_message", "Order got updated");
            ctx.render("orderEditSite.html");  // TODO make sure to change path when this code is part of the entire program. IT SHOULD NOT BE ROOT.
        }catch (DatabaseException e){
            ctx.attribute("message", "Something went wrong when editing the order: "+e.getMessage());
            ctx.render("orderEditSite.html");
        }
    }


    // TODO remove the bolow code when done.
    public static void updateOrderWithBillOfMaterial(Context ctx, ConnectionPool connectionPool){
        OrderDTO currentOrder = ctx.sessionAttribute("chosen_order");

        List<MaterialDTO> billOfMaterials = ctx.sessionAttribute("bill_of_materials");
        // calculates a bill of materials based on the specs of the current order, deletes existing ones first.
        try {
            if(billOfMaterials != null) {
                MaterialsMapper.deleteOrderItemsByOrderID(currentOrder.getId(), connectionPool);
            }
            billOfMaterials = Calculator.generateBillOfMaterials(currentOrder, connectionPool);
            OrderItemMapper.saveBillOfMaterials(billOfMaterials,currentOrder.getId(),connectionPool);
            ctx.attribute("message", "Bill of material got updated");
            ctx.sessionAttribute("bill_of_materials",billOfMaterials);
        }catch (DatabaseException e){
            ctx.attribute("message", "Something went wrong when editing the order: "+e.getMessage());
            ctx.render("orderEditSite.html");
            return;
        }
        // checks if the bill of material is empty or still null and handles the 2 situations.
        // if there's something in the bill of materials it will calculate the total price of everyting inside and set the total price on the order
        // incase nothing is in the bill of material it will send out a message to the user, saying so.
        if(billOfMaterials != null && !billOfMaterials.isEmpty()) {
            int totalPrice = 0;
            for (MaterialDTO m : billOfMaterials) {
                totalPrice += (m.getAmount() * m.getPrice());
            }
            currentOrder.setPrice(totalPrice);
            ctx.sessionAttribute("chosen_order",currentOrder);
            ctx.attribute("price_message", "Price has been updated with the cost of the needed materials, now consider other factors that should influence the price, remember to save when the price is decided.");
            ctx.render("orderEditSite.html");
        }else{
            ctx.attribute("message","It seems that nothing was added to the bill of materials with the current dimensions/setup of the carport, please consider changes");
            ctx.render("orderEditSite.html");
        }
    }
    public static void printSVGtext(){

    }

    public static void backToOrderSite(Context ctx) {
        ctx.render("orderEditSite.html");
    }
}
