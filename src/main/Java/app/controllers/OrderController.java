package app.controllers;

import app.entities.OrderDTO;
import app.entities.Status;
import app.exceptions.DatabaseException;
import app.persistence.ConnectionPool;
import app.persistence.OrderMapper;
import io.javalin.http.Context;

public class OrderController {


    public static boolean createOrder(ConnectionPool connectionPool, Context ctx) throws DatabaseException{
        try{
            int lengthCm = ctx.sessionAttribute("length");
            int widthCm = ctx.sessionAttribute("width");
            int shedLengthCm = ctx.sessionAttribute("shed_length");
            int shedWidthCm = ctx.sessionAttribute("shed_width");
            int slopeDegrees = ctx.sessionAttribute("roof_slope");
            boolean hasAssembler = ctx.sessionAttribute("has_assembler");
            String notice = ctx.sessionAttribute("special_wishes_or_notices");
            OrderDTO order = new OrderDTO(lengthCm,widthCm,shedLengthCm,shedWidthCm,slopeDegrees,hasAssembler,0.0, Status.initialised,notice,"");
            OrderMapper.addOrder(connectionPool,order);
            return true;
        }catch(Exception e){
            throw new DatabaseException("Error");
        }
    }




}
