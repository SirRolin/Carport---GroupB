package app.persistence;

import app.entities.MaterialDTO;
import app.entities.Mtype;
import app.entities.PillarDTO;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

class OrderItemMapperTest {

    @Test
    void saveBillOfMaterials() {
        List<MaterialDTO> billOfMaterials = new ArrayList<>();
        billOfMaterials.add(new PillarDTO(1,"pillar", Mtype.pillar,97,97,1,300,75));
        //OrderItemMapper.saveBillOfMaterials(billOfMaterials,1,);
    }
}