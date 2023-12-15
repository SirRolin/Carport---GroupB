package app.entities;

import org.jetbrains.annotations.NotNull;

public class RoofDTO extends MaterialDTO implements Comparable<MaterialDTO>{

    private int materialVariantID;
    private int length;
    private int price;
    private int amount = 0;
    private String unitType = "stk"; // TODO it should not be hard coded
   public RoofDTO(int materialID, String name, Mtype type, int amount, int width_mm, int depth_mm, int materialVariantID, int length, int price) {
        super(materialID, name, type, amount, width_mm, depth_mm);
        this.materialVariantID = materialVariantID;
        this.length = length;
        this.price = price;
    }
    public RoofDTO(int materialID, String name, Mtype type, int width_mm, int depth_mm, int materialVariantID, int length, int price) {
        super(materialID, name, type, width_mm, depth_mm);
        this.materialVariantID = materialVariantID;
        this.length = length;
        this.price = price;
    }

    public RoofDTO(int materialId, String name, Mtype type, int widthMm, int depthMm){
       super(materialId,name,type,widthMm,depthMm);
    }

    public int getLength() {
        return length;
    }

    public int getPrice() {
        return price;
    }

    @Override
    public String getUnitType() {
        return unitType;
    }

    @Override
    public int getMaterialVariantID() {
        return materialVariantID;
    }

    @Override
    public void setAmount(int amount) {
        this.amount = amount;
    }

    @Override
    public boolean equals(MaterialDTO other) {
        if(other instanceof RoofDTO rOther){
            if(getMaterialVariantID() !=  rOther.getMaterialVariantID()){
                return false;
            }else if(getLength() != rOther.getLength()){
                return false;
            }else if(getPrice() != rOther.getPrice()){
                return false;
            }else if(getUnitType() != rOther.getUnitType()){
                return false;
            }
        }else {
            return false;
        }
        return true;
    }

    @Override
    public int compareTo(@NotNull MaterialDTO o) {
       String strLength = String.valueOf(length);
       return strLength.compareTo(String.valueOf(o.getLength()));
    }
}
