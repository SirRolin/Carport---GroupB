package app.entities;

public enum Mtype {
    roof("roof"),
    pillar("pillar"),
    beam("beam"),
    cover_planks("cover planks");
    String name;
    Mtype(String name){
        this.name = name;
    }

    String getName(){
        return name;
    }

}
