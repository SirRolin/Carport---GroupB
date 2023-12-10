package app.calculator;

import app.entities.*;
import app.exceptions.DatabaseException;
import app.persistence.ConnectionPool;
import app.persistence.MaterialsMapper;
import app.persistence.OrderItemMapper;

import java.sql.SQLException;
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
    public static List<MaterialDTO> generateBillOfMaterials(OrderDTO order, ConnectionPool connectionPool) throws SQLException {
        List<MaterialDTO> billOfMaterials = new ArrayList<>();
        // find the different materials we currently have available based on type.
        try {
        billOfMaterials.add((MaterialDTO) getPillers(order.getLengthCm(),MaterialsMapper.getMaterialInfo(connectionPool,"pillar")));
        billOfMaterials.add((MaterialDTO) getBeams(order.getLengthCm(), MaterialsMapper.getMaterialInfo(connectionPool,"beam")));
        billOfMaterials.add((MaterialDTO) getCrossbeams(order.getLengthCm(),order.getWidthCm(),MaterialsMapper.getMaterialInfo(connectionPool,"cover_planks")));
        OrderItemMapper.saveBillOfMaterials(billOfMaterials, order.getId(), connectionPool);
        }catch (DatabaseException e){
            // TODO add logic incase of database exception
        }
        return billOfMaterials;
    }
    // below code is not correct, need to take several extra factors into account before it gives the right result.
    // mainly setup for testing right now.
    private static List<MaterialDTO> getPillers(int carportLength, List<MaterialDTO> pillerOptions) {
        List<MaterialDTO> neededPillers = new ArrayList<>();
        int spaceBetweenPillers = 310;
        int basePillarAmountForCarport = 4;
        // Calculate the number of extra pillars needed based on carport length
        int extraPillars = ((carportLength - spaceBetweenPillers) / spaceBetweenPillers) * 2;
        // Total number of pillars needed for the carport
        int pillarAmountForCarport = basePillarAmountForCarport + extraPillars;
        // Ensure there are enough pillar options available
        while (neededPillers.size() < pillarAmountForCarport && !pillerOptions.isEmpty()) {
            neededPillers.add(pillerOptions.remove(0));
        }
        // Set the amount for the first pillar option (if available)
        if (!neededPillers.isEmpty()) {
            neededPillers.get(0).setAmount(pillarAmountForCarport);
        }
        return neededPillers;
    }

    private static List<MaterialDTO> getBeams(int carportLength, List<MaterialDTO> beamOptions){
        List<MaterialDTO> neededBeams = new ArrayList<>();
        // logic for calculation
        for (MaterialDTO m: beamOptions) {

        }
        return neededBeams;
    }
    private static List<MaterialDTO> getCrossbeams(int carportLength, int carportWidth, List<MaterialDTO> crossbeamOptions){
        List<MaterialDTO> neededCrossbeams = new ArrayList<>();
        // Logic for calculation
        return neededCrossbeams;
    }
}
