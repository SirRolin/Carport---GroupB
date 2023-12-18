package app.entities;

public abstract class MaterialDTO implements Comparable<MaterialDTO> {

    private int materialId;
    private String name;
    private Mtype type;
    private int widthMm;
    private int depthMm;
    private int amount;
    private int length;


    public MaterialDTO(int materialId, String name, Mtype type, int widthMm, int depthMm){
        this.materialId = materialId;
        this.name = name;
        this.type = type;
        this.widthMm = widthMm;
        this.depthMm = depthMm;
    }

    public MaterialDTO(String name, Mtype type, int widthMm, int depthMm){
        this.name = name;
        this.type = type;
        this.widthMm = widthMm;
        this.depthMm = depthMm;
    }

    public MaterialDTO(int materialID, String name, Mtype type, int amount,int width_mm,int depth_mm){
        this.materialId = materialID;
        this.name = name;
        this.type = type;
        this.amount = amount;
        this.widthMm = width_mm;
        this.depthMm = depth_mm;
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


    public abstract String getUnitType();

    public abstract int getMaterialVariantID();

    public void setAmount(int amount){this.amount = amount;}

    public int getAmount(){
        return amount;
    };
    public abstract int getPrice();

    public int getLength(){return length;}
    public boolean equals(MaterialDTO other){
      if(getMaterialId() != other.getMaterialId()){
          return false;
      }else if(!getName().equals(other.getName())){
          return false;
      }else if(getType() != other.getType()){
          return false;
      }else if(getWidthMm() != other.getWidthMm()){
          return false;
      }else if(getDepthMm() != other.getDepthMm()){
          return false;
      }else if(getAmount() != other.getAmount()){
          return false;
      }
      return true;
    };
}
