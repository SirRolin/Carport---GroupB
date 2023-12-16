package app.entities;

public abstract class MaterialDTO implements Comparable<MaterialDTO> {

    private int materialId;
    private String name;
    private Mtype type;
    private int widthMm;
    private int depthMm;
    private String description;
    private int amount;
    private int length;

    public MaterialDTO(int materialId, String name, Mtype type, int widthMm, int depthMm, String description){
        this.materialId = materialId;
        this.name = name;
        this.type = type;
        this.widthMm = widthMm;
        this.depthMm = depthMm;
        this.description = description;
    }

    public MaterialDTO(String name, Mtype type, int widthMm, int depthMm, String description){
        this.name = name;
        this.type = type;
        this.widthMm = widthMm;
        this.depthMm = depthMm;
        this.description = description;
    }

    public MaterialDTO(int materialID, String name, Mtype type, int amount,int width_mm,int depth_mm, String description){
        this.materialId = materialID;
        this.name = name;
        this.type = type;
        this.amount = amount;
        this.widthMm = width_mm;
        this.depthMm = depth_mm;
        this.description = description;
    }

    public int getMaterialId() {
        return materialId;
    }

    public String getName() {
        return name;
    }

    public Mtype getType() {
        return type;
    }

    public int getWidthMm() {
        return widthMm;
    }

    public int getDepthMm() {
        return depthMm;
    }

    public String getDescription() {
        return description;
    }
    public int getLength(){return length;}

    public abstract String getUnitType();

    public abstract int getMaterialVariantID();

    public void setAmount(int amount){this.amount = amount;}

    public int getAmount(){
        return amount;
    };
    public abstract int getPrice();
}
