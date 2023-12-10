package app.persistence;

import app.entities.MaterialDTO;
import app.entities.OrderDTO;
import app.exceptions.DatabaseException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OrderItemMapper {
    // saves bill of materials to the order id.
    public static int saveBillOfMaterials(List<MaterialDTO> billOfMaterials,int orderID,ConnectionPool connectionPool) throws DatabaseException {
            String sql = "insert into order_items(orderID,mvID,quantity)";
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
}
