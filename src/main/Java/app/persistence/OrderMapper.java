package app.persistence;

import app.entities.OrderDTO;
import app.entities.SearchDTO;
import app.entities.Status;
import app.exceptions.DatabaseException;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


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

    public static List<OrderDTO> getOrdersBySearchDTO(SearchDTO searchDTO, ConnectionPool connectionPool) throws DatabaseException {
        List<OrderDTO> orderList = new ArrayList<>();
        String sql = null;
        String searchParameterString = null;
        int searchParameterInt = 0;
        if (!searchDTO.getName().isEmpty()) {
            sql = "select * from orders where name = ?";
            searchParameterString = searchDTO.getName();
        } else if(!searchDTO.getEmail().isEmpty()) {
            sql = "select * from orders where email = ?";
            searchParameterString = searchDTO.getEmail();
        } else if(searchDTO.getOrderID() > 0){
            sql = "select * from orders where \"orderID\" = ?";
            searchParameterInt = searchDTO.getOrderID();
        }
        try (Connection connection = connectionPool.getConnection()) {
            try (PreparedStatement ps = connection.prepareStatement(sql)) {
                if(searchParameterInt > 0){
                    ps.setInt(1, searchParameterInt);
                } else {
                    ps.setString(1, searchParameterString);
                }
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    int orderID = rs.getInt("orderID");
                    int length = rs.getInt("length_cm");
                    int width = rs.getInt("width_cm");
                    int shedL = rs.getInt("shed_length_cm");
                    int shedW = rs.getInt("shed_width_cm");
                    int slope = rs.getInt("slope_degrees");
                    boolean assembler = rs.getBoolean("hire_assembler");
                    int price = rs.getInt("price");
                    Object status = rs.getObject("status");
                    String svg = rs.getString("svg_text");
                    String nameOfCustomer = rs.getString("name");
                    String emailOfCustomer = rs.getString("email");
                    Date date = rs.getDate("date");
                    java.util.Date javadate = new Date(date.getTime());
                    String notice = rs.getString("notice");
                    switch (status.toString()) {
                        case "initialised" -> {
                            orderList.add(new OrderDTO(orderID, length, width, shedL, shedW, slope, assembler, price, Status.initialised, svg, nameOfCustomer, emailOfCustomer, javadate, notice));
                        }
                        case "processing" -> {
                            orderList.add(new OrderDTO(orderID, length, width, shedL, shedW, slope, assembler, price, Status.processing, svg, nameOfCustomer, emailOfCustomer, javadate, notice));
                        }
                        case "waiting_for_customer" -> {
                            orderList.add(new OrderDTO(orderID, length, width, shedL, shedW, slope, assembler, price, Status.waiting_for_customer, svg, nameOfCustomer, emailOfCustomer, javadate, notice));
                        }
                        case "accepted" -> {
                            orderList.add(new OrderDTO(orderID, length, width, shedL, shedW, slope, assembler, price, Status.accepted, svg, nameOfCustomer, emailOfCustomer, javadate, notice));
                        }
                        case "paid" -> {
                            orderList.add(new OrderDTO(orderID, length, width, shedL, shedW, slope, assembler, price, Status.paid, svg, nameOfCustomer, emailOfCustomer, javadate, notice));
                        }
                    }
                }
                if (orderList.isEmpty()) {
                    throw new DatabaseException("No orders matched what you entered");
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error in database" + e.getMessage());
        }
        return orderList;
    }
}
