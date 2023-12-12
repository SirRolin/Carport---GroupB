package app.entities;

public class CarportChoiceEntity {
    private String roofType;
    public CarportChoiceEntity(String roofType){
        this.roofType = roofType;
    }

    public void setRoofType(String roofType) {
        this.roofType = roofType;
    }

    public String getRoofType(){
        return roofType;
    }

}
