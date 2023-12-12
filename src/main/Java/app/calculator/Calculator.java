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

    // below code is not completed, need to take several extra factors into account before it gives the right result.
    private static List<MaterialDTO> getPillers(int carportLength, int carportWidth, List<MaterialDTO> pillerOptions) {
        // TODO add logic that decides which pillar is the best, if there is more than one.
        List<MaterialDTO> neededPillers = new ArrayList<>();
        int maxDistanceBetweenPillars = 310; // Maximum distance between pillars
        int totalPillars = 1; // starting amount
        int remainingLength = carportLength;
        int remainingWidth = carportWidth;
        MaterialDTO choosenPiller = null;
        // sorts the piller Options array by length, longest first, if there is more than 1 pillar.
        if(pillerOptions.size() > 1){
            Collections.sort(pillerOptions,Collections.reverseOrder());
        }
        // we only have one pillar type
        if (choosenPiller == null) {
            choosenPiller = pillerOptions.remove(0);
        }
        while (totalPillars*maxDistanceBetweenPillars <= carportLength) {
            remainingLength -= maxDistanceBetweenPillars;
            totalPillars++;
        }
        while (remainingWidth > carportWidth) {
            remainingWidth -= maxDistanceBetweenPillars;
            totalPillars++;
            // TODO add real logic that adds more pillars if the the width is to large.
        }
        totalPillars *= 2; // double the amount of pillars after it's done counting, because there is two sides.
        // makes sure there is always atleast 4 pillars.
        if (totalPillars < 4) {
            totalPillars = 4;
        }
        choosenPiller.setAmount(totalPillars);
        neededPillers.add(choosenPiller);
        return neededPillers;
    }

    // below code is done!
    private static List<MaterialDTO> getBeams(int carportLength, int carportWidth, List<MaterialDTO> beamOptions) {
        List<MaterialDTO> neededBeams = new ArrayList<>();
        // sorts the piller Options array by length, longest last, if there is more than 1 pillar.
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

    // below logic is outdated and a bit flawed, please ignore.
    private static List<MaterialDTO> getCrossbeams(int carportLength, int carportWidth, List<MaterialDTO> crossbeamOptions) {
        List<MaterialDTO> neededCrossbeams = new ArrayList<>();
        // TODO need to add logic
        int distanceBetweenCrossBeams = 55; // Maximum distance between pillars
        int totalCrossBeams = 1; // starting amount
        int remainingLength = carportLength;
        int remainingWidth = carportWidth;
        Boolean stillCounting = true;
        MaterialDTO choosenCrossBeam = null;
        while (stillCounting) {
            while (remainingLength > distanceBetweenCrossBeams) {
                remainingLength -= distanceBetweenCrossBeams;
                totalCrossBeams++;
            }
            if (totalCrossBeams*distanceBetweenCrossBeams < carportLength) {
                choosenCrossBeam.setAmount(totalCrossBeams);
                 neededCrossbeams.add(choosenCrossBeam);
                stillCounting = false;
            }
        }
        return neededCrossbeams;
    }
}
