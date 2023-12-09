package app.calculator;

import app.entities.BeamDTO;
import app.entities.CrossbeamDTO;
import app.entities.MaterialDTO;
import app.entities.PillerDTO;
import app.persistence.ConnectionPool;
import app.persistence.MaterialsMapper;

import java.util.ArrayList;
import java.util.List;

public class Calculator {

    public static List<MaterialDTO> generateBillOfMaterials(int carportLength, int carportWidth, int carportHeight, ConnectionPool connectionPool){
        List<MaterialDTO> billOfMaterials = new ArrayList<>();
        billOfMaterials.add((MaterialDTO) getPillers(carportLength,MaterialsMapper.getPillerInfo(connectionPool)));
        billOfMaterials.add((MaterialDTO) getBeams(carportLength,MaterialsMapper.getBeamInfo(connectionPool)));
        billOfMaterials.add((MaterialDTO) getCrossbeams(carportLength,carportWidth,MaterialsMapper.getCrossbeamInfo(connectionPool)));
        return billOfMaterials;
    }
    public static List<MaterialDTO> generateBillOfMaterials(int carportLength, int carportWidth,ConnectionPool connectionPool){
        return generateBillOfMaterials(carportLength,carportWidth,0,connectionPool);
    }
    public static List<MaterialDTO> getPillers(int carportLength, List<PillerDTO> pillerOptions){
        List<MaterialDTO> neededPillers = new ArrayList<>();
        // logic for calculation
        return neededPillers;
    }
    public static List<MaterialDTO> getBeams(int carportLength, List<BeamDTO> beamOptions){
        List<MaterialDTO> neededBeams = new ArrayList<>();
        // logic for calculation
        return neededBeams;
    }
    public static List<MaterialDTO> getCrossbeams(int carportLength, int carportWidth, List<CrossbeamDTO> crossbeamOptions){
        List<MaterialDTO> neededCrossbeams = new ArrayList<>();
        // Logic for calculation
        return neededCrossbeams;
    }
}
