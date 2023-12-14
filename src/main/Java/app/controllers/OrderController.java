package app.controllers;

import app.entities.OrderDTO;
import app.entities.Status;
import app.exceptions.DatabaseException;
import app.persistence.ConnectionPool;
import app.persistence.OrderMapper;
import io.javalin.http.Context;

public class OrderController {


    // TODO THIS NEEDS TO BE ALTERED WHEN WE ACTUALLY CREATE THE ORDER.
    public static boolean createOrder(ConnectionPool connectionPool, Context ctx) throws DatabaseException{
        try{

            int lengthCm = ctx.sessionAttribute("length");
            int widthCm = ctx.sessionAttribute("width");
            int shedLengthCm = ctx.sessionAttribute("shed_length");
            int shedWidthCm = ctx.sessionAttribute("shed_width");
            int slopeDegrees = ctx.sessionAttribute("roof_slope");
            boolean hasAssembler = ctx.sessionAttribute("has_assembler");
            String notice = ctx.sessionAttribute("special_wishes_or_notices");

            /*int lengthCm = Integer.parseInt(ctx.formParam("length"));
            int widthCm = Integer.parseInt(ctx.formParam("width"));
            int shedLengthCm = Integer.parseInt(ctx.formParam("shed_length"));
            int shedWidthCm = Integer.parseInt(ctx.formParam("shed_width"));
            int slopeDegrees = Integer.parseInt(ctx.formParam("roof_slope"));
            boolean hasAssembler = Boolean.parseBoolean(ctx.formParam("has_assembler"));
            String notice = ctx.formParam("special_wishes_or_notices");*/
    //AdminSiteEditRemoveAdd
            OrderDTO order = new OrderDTO(lengthCm,widthCm,shedLengthCm,shedWidthCm,slopeDegrees,hasAssembler,0.0, Status.initialised,notice);
            OrderMapper.addOrder(connectionPool,order);
            return true;
        }catch(Exception e){
            throw new DatabaseException("Error");
        }
    }




}
