package app.persistence;

import app.exceptions.DatabaseException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class OrderMapper {

    public static int addOrder(ConnectionPool connectionPool, int lengthCm, int widthCm, int shedLengthCm, int shedWidthCm, int slopeDegrees, boolean hasAssembler, double price, String status) throws DatabaseException {
        //Method returns the ID to the OrderController, to give the DTO an ID.

        int orderId = 0;
        String sql = "insert into orders (length_cm, width_cm, shed_length_cm, shed_width_cm, slope_degrees, hire_assembler, status) values (?,?,?,?,?,?,?)";
        try(Connection connection = connectionPool.getConnection()){
            try(PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)){
                ps.setInt(1,lengthCm);
                ps.setInt(2,widthCm);
                ps.setInt(3,shedLengthCm);
                ps.setInt(4,shedWidthCm);
                ps.setInt(5,slopeDegrees);
                ps.setBoolean(6,hasAssembler);
                ps.setString(7,status);

                int rowsAffected = ps.executeUpdate();
                if(rowsAffected <1){
                    throw new DatabaseException("Error while creating order");
                }

                try(ResultSet rs = ps.executeQuery()){
                    while(rs.next()){
                        orderId = rs.getInt("order_id");
                    }
                }
            }
        }catch(Exception e){
            throw new DatabaseException("Error while connecting");
        }



        return orderId;
    }

}
