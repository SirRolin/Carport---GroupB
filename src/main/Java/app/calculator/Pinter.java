package app.calculator;

import app.entities.MaterialDTO;
import app.entities.OrderDTO;
import app.persistence.OrderItemMapper;

import java.util.ArrayList;
import java.util.List;

public class Pinter {
        public static void printBillOfMaterialsByOrder(OrderDTO order) {
            List<MaterialDTO> billOfMaterials = new ArrayList<>();
            billOfMaterials.add((MaterialDTO) OrderItemMapper.getMaterialsByOrder(order));
        }

        public static void printBillOfMaterialsByOrderID(int orderID) {
            List<MaterialDTO> billOfMaterials = new ArrayList<>();
            billOfMaterials.add((MaterialDTO) OrderItemMapper.getMaterialsByOrderID(orderID));
        }
}
