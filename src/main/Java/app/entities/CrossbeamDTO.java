package app.entities;

public class CrossbeamDTO extends MaterialDTO {

    int length;
    public CrossbeamDTO(String info, int amount, String unitType, String costumInfo, int length) {
        super(info, amount, unitType, costumInfo);
        this.length = length;
    }
    public int getLength() {
        return length;
    }
}
