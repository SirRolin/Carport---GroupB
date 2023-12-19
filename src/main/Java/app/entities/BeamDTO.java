package app.entities;

import org.jetbrains.annotations.NotNull;

public class BeamDTO extends MaterialDTO implements Comparable<MaterialDTO> {
    private int materialVariantID;
    private int length;
    private int price =0;
    private String unitType = "stk";  // TODO it should not be hard coded

    public BeamDTO(int materialID, String name, Mtype type, int width_mm, int depth_mm, int materialVariantID, int length, int price) {
        super(materialID, name, type, width_mm, depth_mm);
        this.materialVariantID = materialVariantID;
        this.length = length;
        this.price = price;
    }
    public BeamDTO(int materialID, String name, Mtype type, String description, int width_mm, int depth_mm, int materialVariantID, int length, int price) {
        super(materialID, name, type, width_mm, depth_mm, description);
        this.materialVariantID = materialVariantID;
        this.length = length;
        this.price = price;
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
    public int compareTo(@NotNull MaterialDTO o) {
        String strLength = String.valueOf(length);
        return strLength.compareTo(String.valueOf(o.getLength()));
    }
}
