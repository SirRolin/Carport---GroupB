package app.controllers.admin;

import app.entities.*;
import app.exceptions.DatabaseException;
import app.persistence.ConnectionPool;
import app.persistence.OrderMapper;
import io.javalin.http.Context;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class AdminController {

    //ADD, EDIT, REMOVE ORDERS VIA ADMIN.

    public static void addOrder(ConnectionPool connectionPool, Context ctx){

    }

    //int id, int lengthCm, int widthCm, int shedLengthCm, int shedWidthCm, int slopeDegrees, boolean hasAssembler, double price, Status status, String notice
    public static boolean editOrder(ConnectionPool connectionPool, Context ctx) throws DatabaseException{
        OrderDTO order = ctx.sessionAttribute("chosen_order");
        OrderDTO newOrder;
        if(order != null){
            int chosenOrderId = order.getId();
            int lengthCm = (ctx.sessionAttribute("length") != null) ? ctx.sessionAttribute("length") : order.getLengthCm();
            int widthCm = (ctx.sessionAttribute("width") != null) ? ctx.sessionAttribute("width") : order.getWidthCm();
            int shedLengthCm = (ctx.sessionAttribute("shed_length_cm") != null) ? ctx.sessionAttribute("shed_length_cm") : order.getShedLengthCm();
            int shedWidthCm = (ctx.sessionAttribute("shed_width_cm") != null) ? ctx.sessionAttribute("shed_width_cm") : order.getShedWidthCm();
            int slopeDegrees = (ctx.sessionAttribute("slope_degrees") != null) ? ctx.sessionAttribute("slope_degrees") : order.getSlopeDegrees();
            boolean hasAssembler = (ctx.sessionAttribute("has_assembler") != null) ? ctx.sessionAttribute("has_assembler") : order.isHasAssembler();
            double price = (ctx.sessionAttribute("price") != null) ? ctx.sessionAttribute("price") : order.getPrice();
            Status status = (ctx.sessionAttribute("status") != null) ? ctx.sessionAttribute("status") : order.getStatus();
            String notice = (ctx.sessionAttribute("notice") != null) ? ctx.sessionAttribute("notice") : order.getNotice();
            String svg = (ctx.sessionAttribute("svg") != null) ? ctx.sessionAttribute("svg") : order.getSvg();
            newOrder = new OrderDTO(chosenOrderId,lengthCm,widthCm,shedLengthCm,shedWidthCm,slopeDegrees,hasAssembler,price,status,notice,svg);
            try{
                OrderMapper.updateOrder(connectionPool,newOrder);
                return true;
            }catch(Exception e){
                throw new DatabaseException("Error updating selected order!");
            }

        }
        return false;
    }

    public static void removeOrder(ConnectionPool connectionPool, Context ctx){

    }

    public static void loadAdminSite(ConnectionPool connectionPool, Context ctx){
        List<MaterialDTO> materials = new ArrayList<>();
        List<MaterialVariantDTO> variants = new ArrayList<>();
        MaterialDTO pillar1 = new PillarDTO(1,"test1", Mtype.pillar,20,20,"test description");
        MaterialDTO pillar2 = new PillarDTO(2,"test2", Mtype.pillar,25,30,"test description2");
        materials.add(pillar1);
        materials.add(pillar2);
        MaterialVariantDTO variant1 = new MaterialVariantDTO(1,1,20,200.0);
        MaterialVariantDTO variant2 = new MaterialVariantDTO(2,2,26,250.0);
        variants.add(variant1);
        variants.add(variant2);

        try{
            //materials = MaterialsMapper.getMaterialList(connectionPool, ctx);
            //variants = MaterialsMapper.getVariantList(connectionPool, ctx);
            ctx.sessionAttribute("material_list",materials);
            ctx.sessionAttribute("variant_list",variants);
        }catch(Exception e){
            e.printStackTrace();
        }
        ctx.render("adminPage.html");
    }

    public static void variantOrMaterial(ConnectionPool connectionPool, Context ctx){
        String picked = ctx.formParam("add_select");
        try{
            if(picked.equalsIgnoreCase("Material")){
                ctx.sessionAttribute("add_material",true);
            } else if (picked.equalsIgnoreCase("Variant")) {
                ctx.sessionAttribute("add_material",false);
            }
            loadAdminSite(connectionPool,ctx);
        }catch(NullPointerException e){
            e.printStackTrace();
        }
    }

    public static void pickEditable(ConnectionPool connectionPool, Context ctx){
        try{
            String pickedEdit = ctx.formParam("edit_material");
            if(pickedEdit.contains("done")){
                //call edit material function
                ctx.sessionAttribute("edit_material",-1);
            }else{
               int pickedEditInt =  Integer.parseInt(pickedEdit);
                ctx.sessionAttribute("edit_material",pickedEdit);
            }
        }catch(Exception e){

        }
        loadAdminSite(connectionPool,ctx);
    }

    public static boolean addNewMaterial(ConnectionPool connectionPool, Context ctx){
        return true;
    }

    public static boolean addNewVariant(ConnectionPool connectionPool, Context ctx){
        return true;
    }

    public static boolean editMaterial(ConnectionPool connectionPool, Context ctx){
        return true;
    }

    public static boolean editVariant(ConnectionPool connectionPool, Context ctx){
        return true;
    }

    public static boolean removeMaterial(ConnectionPool connectionPool, Context ctx){
        return true;
    }

    public static boolean removeVariant(ConnectionPool connectionPool, Context ctx){
        return true;
    }
}
