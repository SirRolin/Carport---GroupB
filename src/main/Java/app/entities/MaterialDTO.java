package app.entities;

public abstract class MaterialDTO {
    String info;
    int amount;
    String unitType;
    String costumInfo;
    public MaterialDTO(String info, int amount, String unitType, String costumInfo) {
        this.info = info;
        this.amount = amount;
        this.unitType = unitType;
        this.costumInfo = costumInfo;
    }
    public MaterialDTO(String info, String unitType, String costumInfo) {
        this.info = info;
        this.unitType = unitType;
        this.costumInfo = costumInfo;
    }

    public String getInfo() {
        return info;
    }

    public int getAmount() {
        return amount;
    }

    public String getUnitType() {
        return unitType;
    }

    public String getCostumInfo() {
        return costumInfo;
    }
    public void setAmount(int amount) {
        this.amount = amount;
    }
}
