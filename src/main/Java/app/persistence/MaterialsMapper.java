package app.persistence;

import app.entities.BeamDTO;
import app.entities.CrossbeamDTO;
import app.entities.MaterialDTO;
import app.entities.PillerDTO;
import app.exceptions.DatabaseException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MaterialsMapper {
    public static List<MaterialDTO> getMaterialInfoByType(ConnectionPool connectionPool, String type) throws DatabaseException {
        List<MaterialDTO> availableMaterials = new ArrayList<>();
        String sql = "SELECT * FROM public.material_variant AS mv JOIN material AS m ON mv.materialID = m.materialID WHERE type = ?";

        try (Connection connection = connectionPool.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, type);
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
                        case "pillar" -> {
                            availableMaterials.add(new PillerDTO(materialID, name, type, description, width_mm, depth_mm, materialVariantID, length, price));
                        }
                        case "beam" -> {
                            availableMaterials.add(new BeamDTO(materialID, name, type, description, width_mm, depth_mm, materialVariantID, length, price));
                        }
                        case "cover_planks" -> {
                            availableMaterials.add(new CrossbeamDTO(materialID, name, type, description, width_mm, depth_mm, materialVariantID, length, price));
                        }
                        // Add more cases as more material is needed
                        default -> {
                        }
                    }
            }
        }catch (SQLException e){
            throw new DatabaseException("unable to connect to database");
        }
        return availableMaterials;
    }
    public static List<MaterialDTO> getAllMaterialInfo(ConnectionPool connectionPool) throws DatabaseException {
        List<MaterialDTO> availableMaterials = new ArrayList<>();
        String sql = "SELECT * FROM public.material_variant";

        try (Connection connection = connectionPool.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                int materialID = rs.getInt("materialID");
                String name = rs.getString("name");
                String type = rs.getString("type");
                String description = rs.getString("description");
                int width_mm = rs.getInt("width_mm");
                int depth_mm = rs.getInt("depth_mm");
                int materialVariantID = rs.getInt("mvID");
                int length = rs.getInt("length_cm");
                int price = rs.getInt("price");
                switch (type) {
                    case "pillar" -> {
                        availableMaterials.add(new PillerDTO(materialID, name, type, description, width_mm, depth_mm, materialVariantID, length, price));
                    }
                    case "beam" -> {
                        availableMaterials.add(new BeamDTO(materialID, name, type, description, width_mm, depth_mm, materialVariantID, length, price));
                    }
                    case "cover_planks" -> {
                        availableMaterials.add(new CrossbeamDTO(materialID, name, type, description, width_mm, depth_mm, materialVariantID, length, price));
                    }
                    // Add more cases as more material is needed
                    default -> {
                    }
                }
            }
        }catch (SQLException e){
            throw new DatabaseException("unable to connect to database");
        }
        return availableMaterials;
    }
}
