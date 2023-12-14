package app.persistence;

import app.entities.*;
import app.exceptions.DatabaseException;

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
                    String description = rs.getString("description");
                    int width_mm = rs.getInt("width_mm");
                    int depth_mm = rs.getInt("depth_mm");
                    int materialVariantID = rs.getInt("mvID");
                    int length = rs.getInt("length_cm");
                    int price = rs.getInt("price");
                    switch (type) {
                        case pillar -> {
                            availableMaterials.add(new PillarDTO(materialID, name, type, description, width_mm, depth_mm, materialVariantID, length, price));
                        }
                        case beam -> {
                            availableMaterials.add(new BeamDTO(materialID, name, type, description, width_mm, depth_mm, materialVariantID, length, price));
                        }
                        case cover_planks -> {
                            availableMaterials.add(new CrossbeamDTO(materialID, name, type, description, width_mm, depth_mm, materialVariantID, length, price));
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
                    String description = rs.getString("description");
                    int width_mm = rs.getInt("width_mm");
                    int depth_mm = rs.getInt("depth_mm");
                    int materialVariantID = rs.getInt("mvID");
                    int length = rs.getInt("length_cm");
                    int price = rs.getInt("price");
                    switch (type.toString()) {
                        case "pillar" -> {
                            availableMaterials.add(new PillarDTO(materialID, name, Mtype.pillar, description, width_mm, depth_mm, materialVariantID, length, price));
                        }
                        case "beam" -> {
                            availableMaterials.add(new BeamDTO(materialID, name, Mtype.beam, description, width_mm, depth_mm, materialVariantID, length, price));
                        }
                        case "cover_planks" -> {
                            availableMaterials.add(new CrossbeamDTO(materialID, name, Mtype.cover_planks, description, width_mm, depth_mm, materialVariantID, length, price));
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
}
