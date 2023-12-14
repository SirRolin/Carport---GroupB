package app.controllers.admin;

import app.entities.*;
import app.exceptions.DatabaseException;
import app.persistence.ConnectionPool;
import app.persistence.MaterialsMapper;
import app.persistence.OrderMapper;
import app.persistence.VariantsMapper;
import io.javalin.http.Context;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class AdminController {

    //ADD, EDIT, REMOVE ORDERS VIA ADMIN.

    public static void addOrder(ConnectionPool connectionPool, Context ctx){

    }

    //TODO: NEEDS REFACTORING FOR WHEN WE KNOW WHAT TO EDIT IN THE ORDER.
    /*
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
    }*/

    public static void removeOrder(ConnectionPool connectionPool, Context ctx){

    }

    public static void loadAdminSite(ConnectionPool connectionPool, Context ctx){
        List<MaterialDTO> materials = new ArrayList<>();
        List<MaterialVariantDTO> variants = new ArrayList<>();

        try{
            materials = MaterialsMapper.getAllMaterialInfo(connectionPool);
            variants = VariantsMapper.getVariantInfo(connectionPool);
            if(ctx.sessionAttribute("modified_list")==null){
                ctx.sessionAttribute("material_list",materials);
            }else {
                ctx.sessionAttribute("material_list",ctx.sessionAttribute("modified_list"));
            }
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

    public static void pickEditableMaterial(ConnectionPool connectionPool, Context ctx){
        try{
            String pickedEdit = ctx.formParam("edit_material");
            if(pickedEdit.contains("done")){
                //call edit material function //TODO EDIT MATERIAL FUNCTION REFERENCE
                ctx.sessionAttribute("edit_material",-1);
            }else{
               int pickedEditInt =  Integer.parseInt(pickedEdit);
                ctx.sessionAttribute("edit_material",pickedEditInt);
            }
        }catch(Exception e){

        }
        loadAdminSite(connectionPool,ctx);
    }

    public static void pickEditableVariant(ConnectionPool connectionPool, Context ctx){
        try{
            String pickedEdit = ctx.formParam("edit_variant");
            if(pickedEdit.contains("done")){
                //call edit variant function //TODO EDIT VARIANT FUNCTION REFERENCE.
                ctx.sessionAttribute("edit_variant",-1);
            }else {
                int pickedEditInt = Integer.parseInt(pickedEdit);
                ctx.sessionAttribute("edit_variant",pickedEditInt);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        loadAdminSite(connectionPool,ctx);
    }

    public static void filterMaterials(ConnectionPool connectionPool, Context ctx){
        String filter = ctx.formParam("filter");
        switch(filter){
            case "all":
                //getAllMaterialInfo //TODO FILTER MATERIALS
                break;
            case "pillar":
                //getAllMaterialInfoByType
                break;
            case "beam":
                break;
            case "roof":
                break;
            case "cover_planks":
                break;
        }
    }

    //TODO YOU HAVE ALL THESE METHODS LEFT!
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
