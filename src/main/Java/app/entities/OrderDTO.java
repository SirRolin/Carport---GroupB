package app.entities;


import java.util.Date;

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
    private String svg;
    private String name;
    private String email;
    private Date date;
    private String notice;

    //FULL CONSTRUCTOR FOR FULL ORDER | ONLY NEEDED IN ORDERMAPPER WHEN WRITING TO OR GETTING FROM DATABASE.
    /**
     *
     * @param id
     * @param lengthCm
     * @param widthCm
     * @param shedLengthCm
     * @param shedWidthCm
     * @param slopeDegrees
     * @param hasAssembler
     * @param price
     * @param status
     * @param svg
     * @param name
     * @param email
     * @param date
     * @param notice
     */
    public OrderDTO(int id, int lengthCm, int widthCm, int shedLengthCm, int shedWidthCm, int slopeDegrees, boolean hasAssembler, double price, Status status, String svg, String name, String email, Date date, String notice) {
        this.id = id;
        this.lengthCm = lengthCm;
        this.widthCm = widthCm;
        this.shedLengthCm = shedLengthCm;
        this.shedWidthCm = shedWidthCm;
        this.slopeDegrees = slopeDegrees;
        this.hasAssembler = hasAssembler;
        this.price = price;
        this.status = status;
        this.svg = svg;
        this.name = name;
        this.email = email;
        this.date = date;
        this.notice = notice;
    }

    //PARTIAL CONTRUCTOR FOR DATA TRANSFERRING | ONLY NEEDED IN TESTS>
    public OrderDTO(int lengthCm, int widthCm, int shedLengthCm, int shedWidthCm, int slopeDegrees, boolean hasAssembler, double price, Status status, String svg, String name, String email, String notice) {
        this.lengthCm = lengthCm;
        this.widthCm = widthCm;
        this.shedLengthCm = shedLengthCm;
        this.shedWidthCm = shedWidthCm;
        this.slopeDegrees = slopeDegrees;
        this.hasAssembler = hasAssembler;
        this.price = price;
        this.status = status;
        this.svg = svg;
        this.name = name;
        this.email = email;
        this.notice = notice;
    }


    //PARTIAL CONTRUCTOR FOR DATA TRANSFERRING | ONLY NEEDED IN TRANSFER
    public OrderDTO(int lengthCm, int widthCm, int shedLengthCm, int shedWidthCm, int slopeDegrees, boolean hasAssembler, double price, Status status, String notice) {
        this.lengthCm = lengthCm;
        this.widthCm = widthCm;
        this.shedLengthCm = shedLengthCm;
        this.shedWidthCm = shedWidthCm;
        this.slopeDegrees = slopeDegrees;
        this.hasAssembler = hasAssembler;
        this.price = price;
        this.status = status;
        this.notice = notice;

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

    public String getSvg() {
        return svg;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public Date getDate() {
        return date;
    }

    public String getNotice() {
        return notice;
    }

    public void setId(int id) {
        this.id = id;
    }
    public void setSvg(String svg){this.svg = svg;}
    public void setDate(Date date){this.date = date;}
    public void setPrice(int price){this.price = price;}
}
