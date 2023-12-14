package app.persistence;

import app.entities.OrderDTO;
import app.entities.Status;
import app.exceptions.DatabaseException;

import java.sql.*;
import java.util.Date;


public class OrderMapper {

    public static OrderDTO addOrder(ConnectionPool connectionPool, OrderDTO order) throws DatabaseException{
        int orderId = 0;
        String sql = "insert into orders (length_cm,width_cm,shed_length_cm,shed_width_cm,slope_degrees,hire_assembler,price,status,svg_text,name,email,date,notice) values (?,?,?,?,?,?,?,?,?,?,?,?,?)";
        try(Connection connection = connectionPool.getConnection()){
            try(PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)){
                ps.setInt(1,order.getLengthCm());
                ps.setInt(2,order.getWidthCm());
                ps.setInt(3,order.getShedLengthCm());
                ps.setInt(4,order.getShedWidthCm());
                ps.setInt(5,order.getSlopeDegrees());
                ps.setBoolean(6,order.isHasAssembler());
                ps.setDouble(7,order.getPrice());
                ps.setObject(8,order.getStatus(), Types.OTHER);
                ps.setString(9,order.getSvg());
                ps.setString(10,order.getName());
                ps.setString(11,order.getEmail());
                java.util.Date javaDate = new Date();
                java.sql.Date sqlDate = new java.sql.Date(javaDate.getTime());
                order.setDate(javaDate);
                ps.setDate(12,sqlDate);
                ps.setString(13,order.getNotice());


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
            throw new DatabaseException("Error while connecting to database: "+e.getMessage());
        }
        OrderDTO resultDTO = new OrderDTO(orderId,order.getLengthCm(),order.getWidthCm(),order.getShedLengthCm(),order.getShedWidthCm(),order.getSlopeDegrees(),order.isHasAssembler(),order.getPrice(),order.getStatus(),order.getSvg(),order.getName(),order.getEmail(),order.getDate(),order.getNotice());
        return resultDTO;
    }

    public static boolean updateOrder(ConnectionPool connectionPool, OrderDTO newOrder) throws DatabaseException{
        String sql = "update orders set length_cm = ?, width_cm = ?, shed_length_cm = ?, shed_width_cm = ?, slope_degrees = ?, hire_assembler = ?, price = ?, status = ?, svg_text = ?, name = ?, email = ?, date = ?, notice = ? where orderID = ?";
        try(Connection connection = connectionPool.getConnection()){
            try(PreparedStatement ps = connection.prepareStatement(sql)){
                ps.setInt(1,newOrder.getLengthCm());
                ps.setInt(2,newOrder.getWidthCm());
                ps.setInt(3,newOrder.getShedLengthCm());
                ps.setInt(4,newOrder.getShedWidthCm());
                ps.setInt(5,newOrder.getSlopeDegrees());
                ps.setBoolean(6,newOrder.isHasAssembler());
                ps.setDouble(7,newOrder.getPrice());
                ps.setObject(8,newOrder.getStatus(), Types.OTHER);
                ps.setString(9,newOrder.getSvg());
                ps.setString(10, newOrder.getName());
                ps.setString(11, newOrder.getEmail());

                java.util.Date date = newOrder.getDate();
                java.sql.Date sqlDate = new java.sql.Date(date.getTime());

                ps.setDate(12,sqlDate);
                ps.setString(13,newOrder.getNotice());
                ps.setInt(14,newOrder.getId());

                int rowsAffected = ps.executeUpdate();
                if(rowsAffected < 1){
                    throw new DatabaseException("Error while updating order! ");
                }
            }
        }catch(Exception e){
            throw new DatabaseException("Error while connecting to database! "+e);
        }


        return true;
    }

}