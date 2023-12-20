package app.persistence;

import app.entities.*;
import app.exceptions.DatabaseException;
import io.javalin.http.Context;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MaterialsMapper {
    public static List<MaterialDTO> getMaterialInfoByType(ConnectionPool connectionPool, Mtype type) throws DatabaseException {
        List<MaterialDTO> availableMaterials = new ArrayList<>();
        String sql = "SELECT * FROM public.material_variant JOIN material using (\"materialID\") WHERE type = ?";

        try (Connection connection = connectionPool.getConnection()){
             try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setObject(1, type, Types.OTHER);
            ResultSet rs = ps.executeQuery();
                while (rs.next()){
                    int materialID = rs.getInt("materialID");
                    String name = rs.getString("name");
                    int width_mm = rs.getInt("width_mm");
                    int depth_mm = rs.getInt("depth_mm");
                    int materialVariantID = rs.getInt("mvID");
                    int length = rs.getInt("length_cm");
                    int price = rs.getInt("price");
                    switch (type) {
                        case pillar -> {
                            availableMaterials.add(new PillarDTO(materialID, name, type, width_mm, depth_mm, materialVariantID, length, price));
                        }
                        case beam -> {
                            availableMaterials.add(new BeamDTO(materialID, name, type, width_mm, depth_mm, materialVariantID, length, price));
                        }
                        case cover_planks -> {
                            availableMaterials.add(new CrossbeamDTO(materialID, name, type, width_mm, depth_mm, materialVariantID, length, price));
                        }
                        // Add more cases as more material is needed
                        default -> {
                        }
                    }
            }
        }catch (SQLException e){
            throw new DatabaseException("unable to connect to database: "+e.getMessage());
        }
        return availableMaterials;
    }catch (Exception e){
            throw new DatabaseException("Error");
        }
    }
    public static List<MaterialDTO> getAllMaterialInfo(ConnectionPool connectionPool) throws DatabaseException {
        List<MaterialDTO> availableMaterials = new ArrayList<>();
        String sql = "SELECT * FROM material_variant JOIN material using(\"materialID\")";
        try (Connection connection = connectionPool.getConnection()) {
            try (PreparedStatement ps = connection.prepareStatement(sql)) {
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    int materialID = rs.getInt("materialID");
                    String name = rs.getString("name");
                    Object type = rs.getObject("type");
                    int width_mm = rs.getInt("width_mm");
                    int depth_mm = rs.getInt("depth_mm");
                    int materialVariantID = rs.getInt("mvID");
                    int length = rs.getInt("length_cm");
                    int price = rs.getInt("price");
                    switch (type.toString()) {
                        case "pillar" -> {
                            availableMaterials.add(new PillarDTO(materialID, name, Mtype.pillar, width_mm, depth_mm, materialVariantID, length, price));
                        }
                        case "beam" -> {
                            availableMaterials.add(new BeamDTO(materialID, name, Mtype.beam, width_mm, depth_mm, materialVariantID, length, price));
                        }
                        case "cover_planks" -> {
                            availableMaterials.add(new CrossbeamDTO(materialID, name, Mtype.cover_planks, width_mm, depth_mm, materialVariantID, length, price));
                        }
                        // Add more cases as more material is needed
                        default -> {
                        }
                    }
                }
            } catch (SQLException e) {
                throw new DatabaseException("unable to connect to get materials: " + e.getMessage());
            }
            return availableMaterials;
        }catch(Exception e) {
            throw new DatabaseException("Error while connecting to database: "+ e.getMessage());
        }

    }

    public static void deleteOrderItemsByOrderID(int id, ConnectionPool connectionPool) throws DatabaseException {
        String sql = "DELETE FROM order_item WHERE \"orderID\" = ?";
        try (Connection connection = connectionPool.getConnection()) {
            try (PreparedStatement ps = connection.prepareStatement(sql)) {
                ps.setInt(1,id);
                int rowsEffected = ps.executeUpdate();
                if(rowsEffected < 1){
                    // ignore this case
                }
            }
        }catch (SQLException e) {
            throw new DatabaseException("unable to connect to delete the materials: " + e.getMessage());
        }
    }

    public static List<MaterialDTO> getALlMaterialInfo(Context ctx, ConnectionPool connectionPool) throws DatabaseException {
        List<MaterialDTO> availableMaterials = new ArrayList<>();
        String sql = "SELECT * FROM material_variant JOIN material";
        try (Connection connection = connectionPool.getConnection()) {
            try (PreparedStatement ps = connection.prepareStatement(sql)) {
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    int materialID = rs.getInt("materialID");
                    String name = rs.getString("name");
                    Object type = rs.getObject("type");
                    int width_mm = rs.getInt("width_mm");
                    int depth_mm = rs.getInt("depth_mm");
                    int materialVariantID = rs.getInt("mvID");
                    int length = rs.getInt("length_cm");
                    int price = rs.getInt("price");
                    switch (type.toString()) {
                        case "pillar" -> {
                            availableMaterials.add(new PillarDTO(materialID, name, Mtype.pillar, width_mm, depth_mm, materialVariantID, length, price));
                        }
                        case "beam" -> {
                            availableMaterials.add(new BeamDTO(materialID, name, Mtype.beam, width_mm, depth_mm, materialVariantID, length, price));
                        }
                        case "cover_planks" -> {
                            availableMaterials.add(new CrossbeamDTO(materialID, name, Mtype.cover_planks, width_mm, depth_mm, materialVariantID, length, price));
                        }
                        case "roof" -> {
                            availableMaterials.add(new RoofDTO(materialID,name,Mtype.roof,width_mm,depth_mm,materialVariantID,length,price));
                        }
                        // Add more cases as more material is needed
                        default -> {
                        }
                    }
                }
            } catch (SQLException e) {
                throw new DatabaseException("unable to connect to get materials: " + e.getMessage());
            }
        }catch(Exception e) {
            throw new DatabaseException("Error while connecting to database: "+ e.getMessage());
        }
        return availableMaterials;
    }

    public static List<MaterialDTO> getAllMaterials(ConnectionPool connectionPool) throws DatabaseException{
        List<MaterialDTO> materials = new ArrayList<>();
        String sql = "select * from material";

        try(Connection connection = connectionPool.getConnection()){
            try(PreparedStatement ps = connection.prepareStatement(sql)){
                ResultSet rs = ps.executeQuery();
                while (rs.next()){
                    int materialId = rs.getInt("materialID");
                    String name = rs.getString("name");
                    Object type = rs.getObject("type");
                    int width = rs.getInt("width_mm");
                    int depth = rs.getInt("depth_mm");

                    switch(type.toString()){
                        case "beam":
                            materials.add(new BeamDTO(materialId,name,Mtype.valueOf(type.toString()),width,depth));
                            break;
                        case "pillar":
                            materials.add(new PillarDTO(materialId,name,Mtype.valueOf(type.toString()),width,depth));
                            break;
                        case "roof":
                            materials.add(new RoofDTO(materialId,name,Mtype.valueOf(type.toString()),width,depth));
                            break;
                        case "cover_planks":
                            materials.add(new CrossbeamDTO(materialId,name,Mtype.valueOf(type.toString()),width,depth));
                            break;
                    }
                }
            }catch(Exception e){
                throw new DatabaseException("Error while fetching materials: "+e);
            }
        }catch(Exception e){
            throw new DatabaseException("Error while connecting to database: "+e);
        }
        return materials;
    }


    public static List<MaterialDTO> getAllMaterialsByType(ConnectionPool connectionPool, Mtype filter) throws DatabaseException{
        List<MaterialDTO> materials = new ArrayList<>();
        String sql = "select * from material where type = ?";

        try(Connection connection = connectionPool.getConnection()){
            try(PreparedStatement ps = connection.prepareStatement(sql)){
                ps.setObject(1,filter, Types.OTHER);
                ResultSet rs = ps.executeQuery();
                while (rs.next()){
                    int materialId = rs.getInt("materialID");
                    String name = rs.getString("name");
                    Object type = rs.getObject("type");
                    int width = rs.getInt("width_mm");
                    int depth = rs.getInt("depth_mm");

                    switch(type.toString()){
                        case "beam":
                            materials.add(new BeamDTO(materialId,name,Mtype.valueOf(type.toString()),width,depth));
                            break;
                        case "pillar":
                            materials.add(new PillarDTO(materialId,name,Mtype.valueOf(type.toString()),width,depth));
                            break;
                        case "roof":
                            materials.add(new RoofDTO(materialId,name,Mtype.valueOf(type.toString()),width,depth));
                            break;
                        case "cover_planks":
                            materials.add(new CrossbeamDTO(materialId,name,Mtype.valueOf(type.toString()),width,depth));
                            break;
                    }
                }
            }catch(Exception e){
                throw new DatabaseException("Error while fetching materials: "+e);
            }
        }catch(Exception e){
            throw new DatabaseException("Error while connecting to database: "+e);
        }
        return materials;
    }



    public static boolean updateMaterial(ConnectionPool connectionPool, MaterialDTO newMaterial) throws DatabaseException{
        if(newMaterial != null){
            String sql = "update material set name = ?, type = ?, width_mm = ?,  depth_mm = ? where \"materialID\" = ?";
            try(Connection connection = connectionPool.getConnection()){
                try(PreparedStatement ps = connection.prepareStatement(sql)){
                    ps.setString(1, newMaterial.getName());
                    ps.setObject(2,newMaterial.getType(), Types.OTHER);
                    ps.setInt(3,newMaterial.getWidthMm());
                    ps.setInt(4,newMaterial.getDepthMm());
                    ps.setInt(5,newMaterial.getMaterialId());

                    int rowsAffected = ps.executeUpdate();
                    if(rowsAffected < 0){
                        throw new DatabaseException("Error while updating material");
                    }
                }catch(Exception e){
                    throw new DatabaseException("Error while updating material: "+e);
                }
            }catch(Exception e){
                throw new DatabaseException("Error while connecting database: "+e);
            }
            return true;
        }else {
            return false;
        }
    }

    public static MaterialDTO getMaterialById(ConnectionPool connectionPool, int id) throws DatabaseException{
        MaterialDTO material = null;

        if(id > 0){
            String sql = "select * from material where \"materialID\" = ?";
            try(Connection connection = connectionPool.getConnection()){
                try(PreparedStatement ps = connection.prepareStatement(sql)){
                    ps.setInt(1,id);

                    ResultSet rs = ps.executeQuery();
                    while(rs.next()){
                        int materialId = rs.getInt("materialID");
                        String name = rs.getString("name");
                        Object type = rs.getObject("type");
                        int width = rs.getInt("width_mm");
                        int depth = rs.getInt("depth_mm");

                        switch(type.toString()){
                            case "pillar":
                                material = new PillarDTO(materialId,name,Mtype.pillar,width,depth);
                                break;
                            case "roof":
                                material = new RoofDTO(materialId,name,Mtype.roof,width,depth);
                                break;
                            case "cover_planks":
                                material = new CrossbeamDTO(materialId,name,Mtype.cover_planks,width,depth);
                                break;
                            case "beam":
                                material = new BeamDTO(materialId,name,Mtype.beam,width,depth);
                                break;
                        }
                    }

                }catch(Exception e){
                    throw new DatabaseException("Error while fetching material: "+e);
                }
            }catch(Exception e){
                throw new DatabaseException("Error while connecting to database: "+e);
            }
        }else {
            throw new DatabaseException("The id: "+id+" does not exist in the materials database");
        }


        return material;
    }

    public static boolean addMaterial(ConnectionPool connectionPool, MaterialDTO material) throws DatabaseException{
        if(material != null){
            String sql = "insert into material (name, type, width_mm, depth_mm) values (?,?,?,?)";
            try(Connection connection = connectionPool.getConnection()){
                try(PreparedStatement ps = connection.prepareStatement(sql)){
                    ps.setString(1,material.getName());
                    ps.setObject(2, material.getType(), Types.OTHER);
                    ps.setInt(3,material.getWidthMm());
                    ps.setInt(4,material.getDepthMm());

                    int rowsAffected = ps.executeUpdate();
                    if(rowsAffected < 1 ){
                        throw new DatabaseException("Failed to add material");
                    }
                }catch(Exception e){
                    throw new DatabaseException("Error while adding material: "+e);
                }
            }catch(Exception e){
                throw new DatabaseException("Error while connecting to database:" +e);
            }
            return true;
        }else{
            return false;
        }
    }
    public static boolean removeMaterial(ConnectionPool connectionPool, MaterialDTO material)throws DatabaseException{
        if(material != null){
            String sql = "delete from material where name = ? and type = ? and width_mm = ? and depth_mm = ?";
            try(Connection connection = connectionPool.getConnection()){
                try(PreparedStatement ps = connection.prepareStatement(sql)){
                    ps.setString(1,material.getName());
                    ps.setObject(2,material.getType(), Types.OTHER);
                    ps.setInt(3,material.getWidthMm());
                    ps.setInt(4,material.getDepthMm());

                    int rowsAffected = ps.executeUpdate();
                    if(rowsAffected < 1){
                        throw new DatabaseException("Failed to remove material");
                    }
                }catch(Exception e){
                    throw new DatabaseException("Error while deleting material: "+e);
                }
            }catch(Exception e){
                throw new DatabaseException("Failed to connect to database: "+e);
            }
        }
        return true;
    }
}
