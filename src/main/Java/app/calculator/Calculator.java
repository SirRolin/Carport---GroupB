package app.calculator;

import app.entities.*;
import app.exceptions.DatabaseException;
import app.persistence.ConnectionPool;
import app.persistence.MaterialsMapper;
import app.persistence.OrderItemMapper;

import java.util.ArrayList;
import java.util.List;

public class Calculator {
    public static void printBillOfMaterialsByOrder(OrderDTO order){
        List<MaterialDTO> billOfMaterials = new ArrayList<>();
        billOfMaterials.add((MaterialDTO) OrderItemMapper.getMaterialsByOrder(order));
    }
    public static void printBillOfMaterialsByOrderID(int orderID){
        List<MaterialDTO> billOfMaterials = new ArrayList<>();
        billOfMaterials.add((MaterialDTO) OrderItemMapper.getMaterialsByOrderID(orderID));
    }
    public static List<MaterialDTO> generateBillOfMaterials(OrderDTO order, ConnectionPool connectionPool){
        List<MaterialDTO> billOfMaterials = new ArrayList<>();
        billOfMaterials.add((MaterialDTO) getPillers(order.getLengthCm(),MaterialsMapper.getPillerInfo(connectionPool)));
        billOfMaterials.add((MaterialDTO) getBeams(order.getLengthCm(), MaterialsMapper.getBeamInfo(connectionPool)));
        billOfMaterials.add((MaterialDTO) getCrossbeams(order.getLengthCm(),order.getWidthCm(),MaterialsMapper.getCrossbeamInfo(connectionPool)));
        try {
            OrderItemMapper.saveBillOfMaterials(billOfMaterials, order.getId(), connectionPool);
        }catch (DatabaseException e){
            // TODO add logic incase of database exception
        }
        return billOfMaterials;
    }
    private static List<MaterialDTO> getPillers(int carportLength, List<PillerDTO> pillerOptions){
        List<MaterialDTO> neededPillers = new ArrayList<>();
        // logic for calculation
        return neededPillers;
    }
    private static List<MaterialDTO> getBeams(int carportLength, List<BeamDTO> beamOptions){
        List<MaterialDTO> neededBeams = new ArrayList<>();
        // logic for calculation
        return neededBeams;
    }
    private static List<MaterialDTO> getCrossbeams(int carportLength, int carportWidth, List<CrossbeamDTO> crossbeamOptions){
        List<MaterialDTO> neededCrossbeams = new ArrayList<>();
        // Logic for calculation
        return neededCrossbeams;
    }
}
