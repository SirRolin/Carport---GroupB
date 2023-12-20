package app.entities;

import org.jetbrains.annotations.NotNull;

public class OrderItemDTO extends MaterialDTO {
    private int orderID;
    private int mvID;
    private int length;
    private int price;

    public OrderItemDTO(int materialID, String name, Mtype type, int amount, String description, int orderID, int mvID,int length,int price) {
        super(materialID, name, type, amount, description);
        this.orderID = orderID;
        this.mvID = mvID;
        this.length = length;
        this.price = price;
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
    public int getLength() {
        return length;
    }

    @Override
    public String getUnitType() {
        return null;
    }

    @Override
    public int getMaterialVariantID() {
        return mvID;
    }

    @Override
    public int getPrice() {
        return price;
    }

    @Override
    public int compareTo(@NotNull MaterialDTO o) {
        String strLength = String.valueOf(length);
        return strLength.compareTo(String.valueOf(o.getLength()));
    }
}
