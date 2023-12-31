package app.persistence;


import app.entities.MaterialDTO;
import app.entities.MaterialVariantDTO;
import app.exceptions.DatabaseException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Types;
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


    public static MaterialVariantDTO getVariantById(ConnectionPool connectionPool, int id) throws DatabaseException{
        MaterialVariantDTO variant = null;
        String sql = "select * from material_variant where \"mvID\" = ?";

        try(Connection connection = connectionPool.getConnection()){
            try(PreparedStatement ps = connection.prepareStatement(sql)){
                ps.setInt(1,id);

                ResultSet rs = ps.executeQuery();
                while(rs.next()){
                    int variantId = rs.getInt("mvID");
                    int materialID = rs.getInt("materialID");
                    int length = rs.getInt("length_cm");
                    double price = rs.getDouble("price");
                    variant = new MaterialVariantDTO(variantId,materialID,length,price);
                }
                return variant;
            }catch(Exception e){
                throw new DatabaseException("There was no material matching the ID: "+id);
            }
        }catch(Exception e){
            throw new DatabaseException("Failed to connect to database.");
        }
    }

    public static List<MaterialVariantDTO> getVariantsByMaterialId(ConnectionPool connectionPool, int id) throws DatabaseException{
        ArrayList<MaterialVariantDTO> variant = new ArrayList<>();
        String sql = "select * from material_variant where \"materialID\" = ?";

        try(Connection connection = connectionPool.getConnection()){
            try(PreparedStatement ps = connection.prepareStatement(sql)){
                ps.setInt(1,id);

                ResultSet rs = ps.executeQuery();
                while(rs.next()){
                    int variantId = rs.getInt("mvID");
                    int materialID = rs.getInt("materialID");
                    int length = rs.getInt("length_cm");
                    double price = rs.getDouble("price");
                    variant.add(new MaterialVariantDTO(variantId,materialID,length,price));
                }
                return variant;
            }catch(Exception e){
                throw new DatabaseException("There was no material matching the ID: "+id);
            }
        }catch(Exception e){
            throw new DatabaseException("Failed to connect to database.");
        }
    }

    public static List<MaterialVariantDTO> getVariantByMaterialId(ConnectionPool connectionPool, int id) throws DatabaseException{
        List<MaterialVariantDTO> variants = new ArrayList<>();
        String sql = "select * from material_variant where \"materialID\" = ?";

        try(Connection connection = connectionPool.getConnection()){
            try(PreparedStatement ps = connection.prepareStatement(sql)){
                ps.setInt(1,id);

                ResultSet rs = ps.executeQuery();
                while(rs.next()){
                    int variantId = rs.getInt("mvID");
                    int materialID = rs.getInt("materialID");
                    int length = rs.getInt("length_cm");
                    double price = rs.getDouble("price");
                    variants.add(new MaterialVariantDTO(variantId,materialID,length,price));
                }
                return variants;
            }catch(Exception e){
                throw new DatabaseException("There was no material matching the ID: "+id);
            }
        }catch(Exception e){
            throw new DatabaseException("Failed to connect to database.");
        }
    }


    public static boolean updateVariant(ConnectionPool connectionPool, MaterialVariantDTO newVariant,int id) throws DatabaseException{
        if(newVariant != null){
            String sql = "update material_variant set length_cm = ?, price = ? where \"mvID\" = ?";
            try(Connection connection = connectionPool.getConnection()){
                try(PreparedStatement ps = connection.prepareStatement(sql)){
                    ps.setInt(1,newVariant.getLengthCm());
                    ps.setDouble(2,newVariant.getPrice());
                    ps.setInt(3,newVariant.getMvId());

                    int rowsAffected = ps.executeUpdate();
                    if(rowsAffected < 0){
                        throw new DatabaseException("Failed to update material");
                    }
                }catch(Exception e){
                    throw new DatabaseException("Failed to update material: "+e);
                }
            }catch(Exception e){
                throw new DatabaseException("Error while connnecting to database: "+e);
            }
            return true;
        }else{
            return false;
        }
    }


    public static boolean addVariant(ConnectionPool connectionPool, MaterialVariantDTO variant) throws DatabaseException{
        if(variant == null){
            return false;
        }
        String sql = "insert into material_variant (\"materialID\", length_cm, price) values (?,?,?)";
        try(Connection connection = connectionPool.getConnection()){
            try(PreparedStatement ps = connection.prepareStatement(sql)){
                ps.setInt(1,variant.getMaterialId());
                ps.setInt(2,variant.getLengthCm());
                ps.setDouble(3,variant.getPrice());

                int rowsAffected = ps.executeUpdate();
                if(rowsAffected < 1){
                    throw new DatabaseException("Error while adding material");
                }

            }catch(Exception e){
                throw new DatabaseException("Failed to add new variant: "+e);
            }
        }catch(Exception e){
            throw new DatabaseException("Failed to connect to database: "+e);
        }
        return true;
    }


    public static boolean removeVariant(ConnectionPool connectionPool, MaterialVariantDTO variant)throws DatabaseException{
        if(variant != null){
            String sql = "delete from material_variant where \"materialID\" = ? and length_cm = ? and price = ?";
            try(Connection connection = connectionPool.getConnection()){
                try(PreparedStatement ps = connection.prepareStatement(sql)){
                    ps.setInt(1,variant.getMaterialId());
                    ps.setInt(2,variant.getLengthCm());
                    ps.setDouble(3,variant.getPrice());

                    int rowsAffected = ps.executeUpdate();
                    if(rowsAffected < 1){
                        throw new DatabaseException("Failed to remove variant");
                    }
                }catch(Exception e){
                    throw new DatabaseException("Error while deleting variant: "+e);
                }
            }catch(Exception e){
                throw new DatabaseException("Failed to connect to database: "+e);
            }
        }
        return true;
    }

}
