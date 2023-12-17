package app.entities;

import org.jetbrains.annotations.NotNull;

public class CrossbeamDTO extends MaterialDTO implements Comparable<MaterialDTO> {
    private int materialVariantID;
    private int length;
    private int price = 0;
    private String unitType = "stk"; // TODO it should not be hard coded

   public CrossbeamDTO(int materialID, String name, Mtype type, int amount, int width_mm, int depth_mm, int materialVariantID, int length, int price) {
        super(materialID, name, type, amount, width_mm, depth_mm);
        this.materialVariantID = materialVariantID;
        this.length = length;
        this.price = price;
    }
    public CrossbeamDTO(int materialID, String name, Mtype type, int width_mm, int depth_mm, int materialVariantID, int length, int price) {
        super(materialID, name, type, width_mm, depth_mm);
        this.materialVariantID = materialVariantID;
        this.length = length;
        this.price = price;
    }

    public CrossbeamDTO(int materialID, String name, Mtype type, int width, int depth) {
        super(materialID, name, type,width,depth);
    }

    public CrossbeamDTO(String name, Mtype type, int width, int length) {
        super(name,type,width,length);
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
        super.setAmount(amount);
    }

    @Override
    public boolean equals(MaterialDTO other) {
        if(other instanceof CrossbeamDTO cOther){
            if(getMaterialVariantID() !=  cOther.getMaterialVariantID()){
                return false;
            }else if(getLength() != cOther.getLength()){
                return false;
            }else if(getPrice() != cOther.getPrice()){
                return false;
            }else if(getUnitType() != cOther.getUnitType()){
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
