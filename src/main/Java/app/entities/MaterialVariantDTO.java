package app.entities;

public class MaterialVariantDTO {

    private int mvId;
    private int materialId;
    private int lengthCm;
    private double price;

    public MaterialVariantDTO(int mvId, int materialId, int lengthCm, double price) {
        this.mvId = mvId;
        this.materialId = materialId;
        this.lengthCm = lengthCm;
        this.price = price;
    }

    public MaterialVariantDTO(int materialId, int lengthCm, double price) {
        this.materialId = materialId;
        this.lengthCm = lengthCm;
        this.price = price;
    }

    public int getMvId() {
        return mvId;
    }

    public int getMaterialId() {
        return materialId;
    }

    public int getLengthCm() {
        return lengthCm;
    }

    public double getPrice() {
        return price;
    }

    public boolean equals(MaterialVariantDTO other){
        if(this.getLengthCm() != other.getLengthCm()){
            return false;
        } else if (this.getPrice() != other.getPrice()) {
            return false;
        } else if (this.mvId != other.getMvId()){
            return false;
        } else if(this.materialId != other.getMaterialId()){
            return false;
        }
        return super.equals(other);
    }
}
