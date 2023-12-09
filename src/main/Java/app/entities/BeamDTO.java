package app.entities;

public class BeamDTO extends MaterialDTO {
    int length;
    public BeamDTO(String info, int amount, String unitType, String costumInfo, int length) {
        super(info, amount, unitType, costumInfo);
        this.length = length;
    }
    public int getLength() {
        return length;
    }
}
