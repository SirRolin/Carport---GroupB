package app.persistence;

import app.entities.*;
import app.exceptions.DatabaseException;
import io.javalin.http.Context;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderItemMapper {
    // saves bill of materials to the order id.
    public static int saveBillOfMaterials(List<MaterialDTO> billOfMaterials,int orderID,ConnectionPool connectionPool) throws DatabaseException {
            String sql = "insert into order_item(\"orderID\",\"mvID\",\"quantity\") values (?,?,?)";
            int totalRowsAffected = 0; // this is used for test purposes only.
            try(Connection connection = connectionPool.getConnection()){
                try(PreparedStatement ps = connection.prepareStatement(sql)){
                    for(int i = 0;i < billOfMaterials.size(); i++){
                        ps.setInt(1,orderID);
                        ps.setInt(2,billOfMaterials.get(i).getMaterialVariantID());
                        ps.setInt(3,billOfMaterials.get(i).getAmount());
                        int rowsAffected = ps.executeUpdate();
                        if(rowsAffected < 1){
                            throw new DatabaseException("Error while creating order");
                        }
                        totalRowsAffected += rowsAffected;
                    }
                }
            }catch (DatabaseException | SQLException e){
                throw new DatabaseException("Error while connecting to database"+e.getMessage());
            }
            return totalRowsAffected;
    }
    public static List<MaterialDTO> getMaterialsByOrder(OrderDTO order) {
        List<MaterialDTO> billOfMaterialsByOrder = new ArrayList<>();
        return  billOfMaterialsByOrder;
    }

    public static List<MaterialDTO> getMaterialsByOrderID(int orderID) {
        List<MaterialDTO> billOfMaterialsByOrderID = new ArrayList<>();
        return  billOfMaterialsByOrderID;
    }

    public static void updateOrderItem(MaterialDTO orderLine, int orderID,ConnectionPool connectionPool) throws DatabaseException {
        String sql = "UPDATE order_item SET quantity = ?, description = ? WHERE \"orderID\" = ? and \"mvID\" = ?;";
        try(Connection connection = connectionPool.getConnection()){
            try(PreparedStatement ps = connection.prepareStatement(sql)){
                ps.setInt(1,orderLine.getAmount());
                ps.setString(2, orderLine.getDescription());
                ps.setInt(3,orderID);
                ps.setInt(4,orderLine.getMaterialVariantID());
                int linesEffected = ps.executeUpdate();
                if(linesEffected < 1){
                    throw new DatabaseException("The database could not save your info");
                }
            }
        }catch (DatabaseException | SQLException e){
            throw new DatabaseException("Error while connecting to database"+e.getMessage());
        }
    }

    public static List<MaterialDTO> getOrderItemsByOrderID(int orderID, ConnectionPool connectionPool) throws DatabaseException {
        List<MaterialDTO> billOfMaterial = new ArrayList<>();
        String sql = "SELECT \"materialID\",name,type,\"mvID\",length_cm,price,\"mvID\",quantity,order_item.description\n" +
                "FROM public.order_item\n" +
                "JOIN material_variant using (\"mvID\")\n" +
                "JOIN material using(\"materialID\") \n" +
                "where \"orderID\" = ?;";
        try (Connection connection = connectionPool.getConnection()) {
            try (PreparedStatement ps = connection.prepareStatement(sql)) {
                ps.setInt(1, orderID);
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                        int materialID = rs.getInt("materialID");
                        String name = rs.getString("name");
                        Object type = rs.getObject("type");
                        int materialVariantID = rs.getInt("mvID");
                        int quantity = rs.getInt("quantity");
                        String description = rs.getString("description");
                        billOfMaterial.add(new OrderItemDTO(materialID,name,Mtype.valueOf(type.toString()),quantity,description,orderID,materialVariantID));
                    }
                }
        }catch (Exception e) {
            throw new DatabaseException("Error while connecting to database: " + e.getMessage());
        }
        return billOfMaterial;
    }
}
