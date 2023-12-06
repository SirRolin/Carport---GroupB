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
    private Status status;
    private String notice;
    private String svg;

    public OrderDTO(int id, int lengthCm, int widthCm, int shedLengthCm, int shedWidthCm, int slopeDegrees, boolean hasAssembler, double price, Status status, String notice, String svg){
        this.id = id;
        this.lengthCm = lengthCm;
        this.widthCm = widthCm;
        this.shedLengthCm = shedLengthCm;
        this.shedWidthCm = shedWidthCm;
        this.slopeDegrees = slopeDegrees;
        this.hasAssembler = hasAssembler;
        this.price = price;
        this.status = status;
        this.notice = notice;
        this.svg = svg;
    }

    public OrderDTO(int lengthCm, int widthCm, int shedLengthCm, int shedWidthCm, int slopeDegrees, boolean hasAssembler, double price, Status status, String notice, String svg){
        this.lengthCm = lengthCm;
        this.widthCm = widthCm;
        this.shedLengthCm = shedLengthCm;
        this.shedWidthCm = shedWidthCm;
        this.slopeDegrees = slopeDegrees;
        this.hasAssembler = hasAssembler;
        this.price = price;
        this.status = status;
        this.notice = notice;
        this.svg = svg;
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

    public Status getStatus() {
        return status;
    }

    public String getNotice() {
        return notice;
    }
    public String getSvg(){return svg;}

    public void setId(int id) {
        this.id = id;
    }
    public void setSvg(String svg){this.svg = svg;};
}
