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
        billOfMaterials.add((MaterialDTO) getPillers(order.getLengthCm(),order.getWidthCm(),MaterialsMapper.getMaterialInfo(connectionPool,"pillar")));
        billOfMaterials.add((MaterialDTO) getBeams(order.getLengthCm(), order.getWidthCm(), MaterialsMapper.getMaterialInfo(connectionPool,"beam")));
        billOfMaterials.add((MaterialDTO) getCrossbeams(order.getLengthCm(),order.getWidthCm(),MaterialsMapper.getMaterialInfo(connectionPool,"cover_planks")));
        OrderItemMapper.saveBillOfMaterials(billOfMaterials, order.getId(), connectionPool);
        }catch (DatabaseException e){
            // TODO add logic incase of database exception
        }
        return billOfMaterials;
    }
    // below code is not correct, need to take several extra factors into account before it gives the right result.
    private static List<MaterialDTO> getPillers(int carportLength,int carportWidth, List<MaterialDTO> pillerOptions) {
        // TODO add logic that decides which pillar is the best, if there is more than one.
        List<MaterialDTO> neededPillers = new ArrayList<>();
        int maxDistanceBetweenPillars = 310; // Maximum distance between pillars
        int totalPillars = 1; // starting amount.
        int remainingLength = carportLength;
        int remainingWidth = carportWidth;
        while(remainingLength > maxDistanceBetweenPillars){
            remainingLength -= maxDistanceBetweenPillars;
            totalPillars++;
            while(remainingWidth > carportWidth) {
                remainingWidth -= maxDistanceBetweenPillars;
                totalPillars++;
                // TODO add real logic that adds more pillars if the the width is to large.
            }
        }
        totalPillars *=2;
        neededPillers.add(pillerOptions.remove(0));
        neededPillers.get(0).setAmount(totalPillars);
        return neededPillers;
    }

    private static List<MaterialDTO> getBeams(int carportLength,int carportWidth, List<MaterialDTO> beamOptions) {
        List<MaterialDTO> neededBeams = new ArrayList<>();
        // TODO need to add logic
        return neededBeams;
    }
    private static List<MaterialDTO> getCrossbeams(int carportLength, int carportWidth, List<MaterialDTO> crossbeamOptions){
        List<MaterialDTO> neededCrossbeams = new ArrayList<>();
        // TODO find the most suiteble beam. Even though i could only find 1 type on fog homepage.
        int remainingLength = carportLength;
        int remainingWidth = carportWidth;
        int distanceBetweenBeams = 55;
        int totalCrossBeams = 1; // starts with 1 crossbeam
        while (remainingLength > distanceBetweenBeams){ // keeps adding crossbeams as long as there is room.
            remainingLength -= distanceBetweenBeams;
            totalCrossBeams++;
        }
        // divides by 2 if the carportWidth is less than half of the crossbeam length.
        if(carportWidth < (crossbeamOptions.get(0).getLength())/2){
            totalCrossBeams /=2;
        }
        neededCrossbeams.add(crossbeamOptions.remove(0));
        neededCrossbeams.get(0).setAmount(totalCrossBeams);
        return neededCrossbeams;
    }
}
