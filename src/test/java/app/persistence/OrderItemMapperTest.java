package app.persistence;

import app.entities.MaterialDTO;
import app.entities.PillerDTO;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class OrderItemMapperTest {

    @Test
    void saveBillOfMaterials() {
        List<MaterialDTO> billOfMaterials = new ArrayList<>();
        billOfMaterials.add(new PillerDTO(1,"pillar","pillar","sæt pillar 90cm ned i jorden",97,97,1,300,75));
        //OrderItemMapper.saveBillOfMaterials(billOfMaterials,1,);
    }
}