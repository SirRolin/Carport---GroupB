package app.persistence;

import app.entities.OrderDTO;
import app.exceptions.DatabaseException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class OrderMapper {

    public static OrderDTO addOrder(ConnectionPool connectionPool, OrderDTO order) throws DatabaseException{
        int orderId = 1;
        String sql = "insert into orders (length_cm, width_cm, shed_length_cm, shed_width_cm, slope_degrees, hire_assembler, status) values (?,?,?,?,?,?,?)";
        try(Connection connection = connectionPool.getConnection()){
            try(PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)){
                ps.setInt(1,order.getLengthCm());
                ps.setInt(2,order.getWidthCm());
                ps.setInt(3,order.getShedLengthCm());
                ps.setInt(4,order.getShedWidthCm());
                ps.setInt(5,order.getSlopeDegrees());
                ps.setBoolean(6,order.isHasAssembler());
                ps.setObject(7,order.getStatus().toString());

                int rowsAffected = ps.executeUpdate();
                if(rowsAffected < 1){
                    throw new DatabaseException("Error while creating order");
                }

                try(ResultSet rs = ps.getGeneratedKeys()){
                    while(rs.next()){
                        orderId = rs.getInt("orderID");
                    }
                }
            }
        }catch(Exception e){
            throw new DatabaseException("Error while connecting to database");
        }
        OrderDTO resultDTO = new OrderDTO(orderId,order.getLengthCm(),order.getWidthCm(),order.getShedLengthCm(),order.getShedWidthCm(),order.getSlopeDegrees(),order.isHasAssembler(),0, order.getStatus(),order.getNotice());
        return resultDTO;
    }

}
