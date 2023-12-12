package app.entities;

public abstract class MaterialDTO implements Comparable<MaterialDTO> {
    int materialID;
    String name;
    String type;
    int amount;
    String costumInfo;
    int width_mm;
    int depth_mm;

    public MaterialDTO(int materialID, String name, String type, int amount, String costumInfo, int width_mm, int depth_mm) {
        this.materialID = materialID;
        this.name = name;
        this.type = type;
        this.amount = amount;
        this.costumInfo = costumInfo;
        this.width_mm = width_mm;
        this.depth_mm = depth_mm;
    }
    public MaterialDTO(int materialID, String name, String type, String costumInfo, int width_mm, int depth_mm) {
        this.materialID = materialID;
        this.name = name;
        this.type = type;
        this.costumInfo = costumInfo;
        this.width_mm = width_mm;
        this.depth_mm = depth_mm;
    }

    public int getMaterialID() {
        return materialID;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public int getAmount() {
        return amount;
    }

    public abstract String getUnitType();

    public String getCostumInfo() {
        return costumInfo;
    }

    public int getWidth_mm() {
        return width_mm;
    }

    public int getDepth_mm() {
        return depth_mm;
    }
    public  abstract int getMaterialVariantID();
    public abstract void setAmount(int amount);
    public abstract int getLength();
}
