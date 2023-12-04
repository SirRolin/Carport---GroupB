package app.controllers;

import app.entities.OrderDTO;
import app.exceptions.DatabaseException;
import app.persistence.ConnectionPool;
import app.persistence.OrderMapper;
import io.javalin.http.Context;

public class OrderController {

    public static OrderDTO createOrder(ConnectionPool connectionPool, Context ctx) throws DatabaseException {
        int orderId = OrderMapper.addOrder(connectionPool,100,100,200,200,90,true,10,"paid");
        return null;
    }

}
