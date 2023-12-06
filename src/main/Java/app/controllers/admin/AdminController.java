package app.controllers.admin;

import app.entities.OrderDTO;
import app.entities.Status;
import app.exceptions.DatabaseException;
import app.persistence.ConnectionPool;
import app.persistence.OrderMapper;
import io.javalin.http.Context;

public class AdminController {

    //ADD, EDIT, REMOVE ORDERS VIA ADMIN.

    public static void addOrder(ConnectionPool connectionPool, Context ctx){

    }

    //int id, int lengthCm, int widthCm, int shedLengthCm, int shedWidthCm, int slopeDegrees, boolean hasAssembler, double price, Status status, String notice
    public static boolean editOrder(ConnectionPool connectionPool, Context ctx) throws DatabaseException{
        OrderDTO order = ctx.sessionAttribute("chosen_order");
        OrderDTO newOrder;
        if(order != null){
            int chosenOrderId = order.getId();
            int lengthCm = (ctx.sessionAttribute("length") != null) ? ctx.sessionAttribute("length") : order.getLengthCm();
            int widthCm = (ctx.sessionAttribute("width") != null) ? ctx.sessionAttribute("width") : order.getWidthCm();
            int shedLengthCm = (ctx.sessionAttribute("shed_length_cm") != null) ? ctx.sessionAttribute("shed_length_cm") : order.getShedLengthCm();
            int shedWidthCm = (ctx.sessionAttribute("shed_width_cm") != null) ? ctx.sessionAttribute("shed_width_cm") : order.getShedWidthCm();
            int slopeDegrees = (ctx.sessionAttribute("slope_degrees") != null) ? ctx.sessionAttribute("slope_degrees") : order.getSlopeDegrees();
            boolean hasAssembler = (ctx.sessionAttribute("has_assembler") != null) ? ctx.sessionAttribute("has_assembler") : order.isHasAssembler();
            double price = (ctx.sessionAttribute("price") != null) ? ctx.sessionAttribute("price") : order.getPrice();
            Status status = (ctx.sessionAttribute("status") != null) ? ctx.sessionAttribute("status") : order.getStatus();
            String notice = (ctx.sessionAttribute("notice") != null) ? ctx.sessionAttribute("notice") : order.getNotice();
            String svg = (ctx.sessionAttribute("svg") != null) ? ctx.sessionAttribute("svg") : order.getSvg();
            newOrder = new OrderDTO(chosenOrderId,lengthCm,widthCm,shedLengthCm,shedWidthCm,slopeDegrees,hasAssembler,price,status,notice,svg);
            try{
                OrderMapper.updateOrder(connectionPool,newOrder);
                return true;
            }catch(Exception e){
                throw new DatabaseException("Error updating selected order!");
            }

        }
        return false;
    }

    public static void removeOrder(ConnectionPool connectionPool, Context ctx){

    }



}
