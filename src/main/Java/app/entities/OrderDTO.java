package app.entities;

public class OrderDTO {

    private int id;
    private int lengthCm;
    private int widthCm;
    private int shedLengthCm;
    private int shedWidthCm;
    private int slopeDegrees;
    private boolean hasAssembler;
    private double price;
    private String status;

    public OrderDTO(int id, int lengthCm, int widthCm, int shedLengthCm, int shedWidthCm, int slopeDegrees, boolean hasAssembler, double price, String status){
        this.id = id;
        this.lengthCm = lengthCm;
        this.widthCm = widthCm;
        this.shedLengthCm = shedLengthCm;
        this.slopeDegrees = slopeDegrees;
        this.hasAssembler = hasAssembler;
        this.price = price;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public int getLengthCm() {
        return lengthCm;
    }

    public int getWidthCm() {
        return widthCm;
    }

    public int getShedLengthCm() {
        return shedLengthCm;
    }

    public int getShedWidthCm() {
        return shedWidthCm;
    }

    public int getSlopeDegrees() {
        return slopeDegrees;
    }

    public boolean isHasAssembler() {
        return hasAssembler;
    }

    public double getPrice() {
        return price;
    }

    public String getStatus() {
        return status;
    }




}
