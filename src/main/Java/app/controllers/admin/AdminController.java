package app.controllers.admin;

import app.entities.*;
import app.exceptions.DatabaseException;
import app.persistence.ConnectionPool;
import app.persistence.MaterialsMapper;
import app.persistence.OrderMapper;
import app.persistence.VariantsMapper;
import io.javalin.Javalin;
import io.javalin.http.Context;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class AdminController {

    //ADD, EDIT, REMOVE ORDERS VIA ADMIN.
    public static void AddRenders(Javalin app, ConnectionPool connectionPool){
        app.get("/" , ctx -> ctx.redirect("/admin"));
        app.get("/admin", ctx -> AdminController.loadAdminSite(connectionPool, ctx)); // ToDo remove test
        app.post("/chooseVariantOrMaterial", ctx -> AdminController.variantOrMaterial(connectionPool, ctx));
        app.post("/editMaterial", ctx -> AdminController.pickEditableMaterial(connectionPool, ctx));
        app.post("/editVariant", ctx -> AdminController.pickEditableVariant(connectionPool, ctx));
        app.post("/filterMaterials", ctx -> AdminController.filterMaterials(connectionPool, ctx));
    }


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
                String[] editString = pickedEdit.split(" ");
                int id = Integer.parseInt(editString[1]);
                List<MaterialDTO> materials = ctx.sessionAttribute("material_list");
                int finalId = materials.get(id).getMaterialId();
                MaterialDTO pickedMaterial = MaterialsMapper.getMaterialById(connectionPool, finalId);
                String name = userInput(ctx.formParam("edited_material_name"),pickedMaterial.getName());
                String type = userInput(ctx.formParam("edited_material_type"),pickedMaterial.getType().toString());
                int width = Integer.parseInt(userInput(ctx.formParam("edited_material_width"),String.valueOf(pickedMaterial.getWidthMm())));
                int depth = Integer.parseInt(userInput(ctx.formParam("edited_material_depth"),String.valueOf(pickedMaterial.getDepthMm())));
                MaterialDTO newMaterial = null;
                switch(type){
                    case "pillar":
                        newMaterial = new PillarDTO(finalId,name,Mtype.pillar,width,depth);
                        break;
                    case "beam":
                        newMaterial = new BeamDTO(finalId,name,Mtype.beam,width,depth);
                        break;
                    case "roof":
                        newMaterial = new RoofDTO(finalId,name,Mtype.roof,width,depth);
                        break;
                    case "cover planks":
                        newMaterial = new CrossbeamDTO(finalId,name,Mtype.cover_planks,width,depth);
                        break;
                }
                if(newMaterial != null){
                    MaterialsMapper.updateMaterial(connectionPool, newMaterial);
                }
                ctx.sessionAttribute("edit_material",-1);
            }else{
               int pickedEditInt =  Integer.parseInt(pickedEdit);
                ctx.sessionAttribute("edit_material",pickedEditInt);
            }
        }catch(Exception e){
            System.out.println(e);
        }
        loadAdminSite(connectionPool,ctx);
    }

    public static void pickEditableVariant(ConnectionPool connectionPool, Context ctx){
        try{
            String pickedEdit = ctx.formParam("edit_variant");
            if(pickedEdit.contains("done")){
                String[] editString = pickedEdit.split(" ");
                int id = Integer.parseInt(editString[1]);
                List<MaterialVariantDTO> variants = ctx.sessionAttribute("variant_list");
                int finalId = variants.get(id).getMvId();
                MaterialVariantDTO pickedVariant = VariantsMapper.getVariantById(connectionPool,finalId);
                int length = Integer.parseInt((ctx.formParam("edited_variant_length") != null || !ctx.formParam("edited_variant_length").isEmpty() || Integer.parseInt(ctx.formParam("edited_variant_length")) > 0) ? ctx.formParam("edited_variant_length") : String.valueOf(pickedVariant.getLengthCm()));
                double price = Double.parseDouble((ctx.formParam("edited_variant_price") != null || !ctx.formParam("edited_variant_price").isEmpty() || Double.parseDouble(ctx.formParam("edited_variant_price")) > 0) ? ctx.formParam("edited_variant_price") : String.valueOf(pickedVariant.getPrice()));
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

    private static String userInput(String formParam, String alt){
        if(validateInput(formParam)){
           return formParam;
        }
        return alt;
    }

    private static boolean validateInput(String str){
        if(str == null || str.isEmpty()){
            return false;
        }

        if(containsNumber(str)){
            int number = Integer.parseInt(str);
            if(number <= 0){
                return false;
            }
        }

        return true;
    }

    private static boolean containsNumber(String str){
        for(char c : str.toCharArray()){
            if(Character.isDigit(c)){
                return true;
            }
        }
        return false;
    }

    //TODO YOU HAVE ALL THESE METHODS LEFT!
    public static boolean addNewMaterial(ConnectionPool connectionPool, Context ctx){
        return true;
    }

    public static boolean addNewVariant(ConnectionPool connectionPool, Context ctx){
        return true;
    }

    public static boolean removeMaterial(ConnectionPool connectionPool, Context ctx){
        return true;
    }

    public static boolean removeVariant(ConnectionPool connectionPool, Context ctx){
        return true;
    }
}
