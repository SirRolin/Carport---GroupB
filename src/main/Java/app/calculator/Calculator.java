package app.calculator;

import app.entities.*;
import app.exceptions.DatabaseException;
import app.persistence.ConnectionPool;
import app.persistence.MaterialsMapper;
import app.persistence.OrderItemMapper;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Calculator {

    public static List<MaterialDTO> generateBillOfMaterials(OrderDTO order, ConnectionPool connectionPool) throws SQLException {
        List<MaterialDTO> billOfMaterials = new ArrayList<>();
        // find the different materials we currently have available based on type.
        try {
            billOfMaterials.add((MaterialDTO) getPillers(order.getLengthCm(), order.getWidthCm(), MaterialsMapper.getMaterialInfoByType(connectionPool, "pillar")));
            billOfMaterials.add((MaterialDTO) getBeams(order.getLengthCm(), order.getWidthCm(), MaterialsMapper.getMaterialInfoByType(connectionPool, "beam")));
            billOfMaterials.add((MaterialDTO) getCrossbeams(order.getLengthCm(), order.getWidthCm(), MaterialsMapper.getMaterialInfoByType(connectionPool, "cover_planks")));
            OrderItemMapper.saveBillOfMaterials(billOfMaterials, order.getId(), connectionPool);
        } catch (DatabaseException e) {
            // TODO add logic incase of database exception
        }
        return billOfMaterials;
    }

    // below code should be complete, until we decide to add a shed.
    private static List<MaterialDTO> getPillers(int carportLength, int carportWidth, List<MaterialDTO> pillerOptions) {
        List<MaterialDTO> neededPillers = new ArrayList<>();
        int maxDistanceBetweenPillars = 310; // Maximum distance between pillars
        MaterialDTO choosenPillar = null;
        if(!pillerOptions.isEmpty()){
            choosenPillar = pillerOptions.remove(0);
        }
        int totalPillars = 0; // starting amount
        while(totalPillars*maxDistanceBetweenPillars < carportLength*2){ // carportLength times two because there is two sides.
            totalPillars++;
        }
        choosenPillar.setAmount(totalPillars);
        neededPillers.add(choosenPillar);
        return neededPillers;
    }

    // below code is done, atleast until we decide to add a shed!
    private static List<MaterialDTO> getBeams(int carportLength, int carportWidth, List<MaterialDTO> beamOptions) {
        List<MaterialDTO> neededBeams = new ArrayList<>();
        // sorts the beam Options array by length, longest last, if there is more than 1 pillar.
        if(beamOptions.size() > 1){
            Collections.sort(beamOptions, Comparator.naturalOrder());
        }
        int remainingLength = carportLength*2; // because there is two sides.
        // starts the counting process
        // takes the longest option first and checks. and works its way though the list.
        for (int i = beamOptions.size()-1; i >= 0; i--) {
            if(!beamOptions.isEmpty()) {
                int totalBeams = 0;
                MaterialDTO currentBeam = beamOptions.get(i);
                System.out.println(currentBeam.getLength());
                // counts the amount if we need the currently choosen beam.
                while (currentBeam.getLength() <= remainingLength && remainingLength > 0) {
                    remainingLength -= currentBeam.getLength();
                    totalBeams++;
                }
                // if there's only 1 option left and there's still remaining length, it adds one.
                if (remainingLength > 0 && beamOptions.size() == 1) {
                    totalBeams++;
                }
                // sets the amount and removes the beam from the list and goes again.
                currentBeam.setAmount(totalBeams);
                beamOptions.remove(i);
                // a check to make sure it only adds a beam if it's needed.
                if (currentBeam.getAmount() != 0) {
                    neededBeams.add(currentBeam);
                }
            }
        }
        return neededBeams;
    }

    // below logic is done, unless we decide to do the nice to have TO_DO.
    private static List<MaterialDTO> getCrossbeams(int carportLength, int carportWidth, List<MaterialDTO> crossbeamOptions) {
        List<MaterialDTO> neededCrossbeams = new ArrayList<>();
        int distanceBetweenCrossBeams = 55; // Maximum distance between pillars
        int totalCrossBeams = 0; // starting amount
        MaterialDTO choosenCrossBeam = null;
        // sorts the beam Options array by length, shortest last, if there is more than 1 pillar.
        if(crossbeamOptions.size() > 1){
            Collections.sort(crossbeamOptions, Comparator.reverseOrder());
        }
        // if the length of the crossbeam is long enough to cover carport width, it is chosen.
        for(int i = crossbeamOptions.size()-1;i >=0;i--){
            if(crossbeamOptions.get(i).getLength() >= carportWidth){
                choosenCrossBeam = crossbeamOptions.get(i);
            }
        }
        // adds the amount need of the chosen crossbeam to out needCrossBeam array if one was chosen.
        if(!choosenCrossBeam.equals(null)) {
            while (totalCrossBeams * distanceBetweenCrossBeams <= carportLength) {
                totalCrossBeams++;
            }
            choosenCrossBeam.setAmount(totalCrossBeams);
            neededCrossbeams.add(choosenCrossBeam);
        }

        if(choosenCrossBeam.equals(null)){
            // TODO NICE TO HAVE: add logic if no single CrossBeam is long enough to cover the width of the carport on its own.
            // choose the longest beam available. subtract its length from width. and do new check all over again with the
            // the remaining width. now find the crossbeam with the sortes length that can cover that remaining width.
            // and start the count. if the length of the shortest crossbeam is more than twice the remaining length divide
            // the shortest crossbeam count by 2.
        }
        return neededCrossbeams;
    }
}
