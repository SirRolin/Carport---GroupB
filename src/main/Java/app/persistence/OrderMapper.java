package app.persistence;

import app.exceptions.DatabaseException;

public class OrderMapper {

    public static int addOrder(ConnectionPool connectionPool, int lengthCm, int widthCm, int shedLengthCm, int shedWidthCm, int slopeDegrees, boolean hasAssembler, double price, String status) throws DatabaseException {
        //Method returns the ID to the OrderController, to give the DTO an ID.
        return 0;
    }

}
