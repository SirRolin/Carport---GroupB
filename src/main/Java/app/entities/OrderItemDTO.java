package app.entities;

import org.jetbrains.annotations.NotNull;

public class OrderItemDTO extends MaterialDTO {
    private int orderID;
    private int mvID;

    public OrderItemDTO(int materialID, String name, Mtype type, int amount, String description, int orderID, int mvID) {
        super(materialID, name, type, amount, description);
        this.orderID = orderID;
        this.mvID = mvID;
    }

    public int getOrderID() {
        return orderID;
    }

    public void setOrderID(int orderID) {
        this.orderID = orderID;
    }

    public int getMvID() {
        return mvID;
    }

    public void setMvID(int mvID) {
        this.mvID = mvID;
    }

    @Override
    public String getUnitType() {
        return null;
    }

    @Override
    public int getMaterialVariantID() {
        return 0;
    }

    @Override
    public int getPrice() {
        return 0;
    }

    @Override
    public int compareTo(@NotNull MaterialDTO o) {
        return 0;
    }
}
