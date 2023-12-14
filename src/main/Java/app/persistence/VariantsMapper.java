package app.persistence;


import app.entities.MaterialVariantDTO;
import app.exceptions.DatabaseException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class VariantsMapper {



    public static List<MaterialVariantDTO> getVariantInfo(ConnectionPool connectionPool) throws DatabaseException{
        List<MaterialVariantDTO> variants = new ArrayList<>();
        String sql = "select * from material_variant";

        try(Connection connection = connectionPool.getConnection()){
            try(PreparedStatement ps = connection.prepareStatement(sql)){
                ResultSet rs = ps.executeQuery();
                while(rs.next()){
                    int variantId = rs.getInt("mvID");
                    int materialId = rs.getInt("materialID");
                    int lengthCm = rs.getInt("length_cm");
                    double price = rs.getDouble("price");
                    variants.add(new MaterialVariantDTO(variantId,materialId,lengthCm,price));
                }

            }catch (Exception e){
                throw new DatabaseException("Error while getting variants: "+e.getMessage());
            }
        }catch (Exception e){
            throw new DatabaseException("Error while connecting to database: "+e.getMessage());
        }
        return variants;
    }

}
