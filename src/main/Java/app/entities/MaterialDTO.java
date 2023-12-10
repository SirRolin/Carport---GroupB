package app.entities;

public abstract class MaterialDTO {

    private int materialId;
    private String name;
    private Mtype type;
    private int widthMm;
    private int depthMm;
    private String description;

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
}
