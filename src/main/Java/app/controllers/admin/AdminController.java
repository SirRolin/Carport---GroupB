package app.controllers.admin;

import app.entities.*;
import app.exceptions.DatabaseException;
import app.persistence.ConnectionPool;
import app.persistence.MaterialsMapper;
import app.persistence.VariantsMapper;
import app.validators.Validator;
import io.javalin.Javalin;
import io.javalin.http.Context;

import javax.xml.crypto.Data;
import java.util.ArrayList;
import java.util.List;

public class AdminController {

    //ADD, EDIT, REMOVE ORDERS VIA ADMIN.
    public static void AddRenders(Javalin app, ConnectionPool connectionPool){
        app.get("/" , ctx -> ctx.redirect("/admin"));
        app.get("/admin", ctx -> AdminController.loadAdminSite(connectionPool, ctx)); // ToDo remove test
        app.post("/chooseAddVariantOrMaterial", ctx -> AdminController.addVariantOrMaterial(connectionPool, ctx));
        app.post("/chooseRemoveVariantOrMaterial", ctx -> AdminController.removeVariantOrMaterial(connectionPool,ctx));
        app.post("/editMaterial", ctx -> AdminController.pickEditableMaterial(connectionPool, ctx));
        app.post("/editVariant", ctx -> AdminController.pickEditableVariant(connectionPool, ctx));
        app.post("/filterMaterials", ctx -> AdminController.filterMaterials(connectionPool, ctx));
        app.post("/addNewMaterial", ctx-> addNewMaterial(connectionPool,ctx));
        app.post("/addNewVariant", ctx-> addNewVariant(connectionPool,ctx));
        app.post("removeMaterial",ctx->removeMaterial(connectionPool, ctx));
        app.post("removeVariant",ctx->removeVariant(connectionPool,ctx));
    }


    public static void addOrder(ConnectionPool connectionPool, Context ctx){

    }

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

    public static void addVariantOrMaterial(ConnectionPool connectionPool, Context ctx){
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

    public static void removeVariantOrMaterial(ConnectionPool connectionPool, Context ctx){
        String picked = ctx.formParam("remove_select");
        try{
            if(picked.equalsIgnoreCase("Material")){
                ctx.sessionAttribute("remove_material",true);
            }else if(picked.equalsIgnoreCase("Variant")){
                ctx.sessionAttribute("remove_material",false);
            }
            loadAdminSite(connectionPool, ctx);
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
                String name = Validator.userInput(ctx.formParam("edited_material_name"),pickedMaterial.getName());
                String type = Validator.userInput(ctx.formParam("edited_material_type"),pickedMaterial.getType().toString());
                int width = Validator.userInput(ctx.formParam("edited_material_width"),pickedMaterial.getWidthMm());
                int depth = Validator.userInput(ctx.formParam("edited_material_depth"),pickedMaterial.getDepthMm());
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
                    if(!pickedMaterial.equals(newMaterial)){
                        MaterialsMapper.updateMaterial(connectionPool, newMaterial);
                    }
                }
                ctx.sessionAttribute("edit_material",-1);
            }else{
               int pickedEditInt =  Integer.parseInt(pickedEdit);
                ctx.sessionAttribute("edit_material",pickedEditInt);
            }
        }catch(Exception e){
            System.out.println(e.getMessage());
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
                int length = Validator.userInput(ctx.formParam("edited_variant_length"),pickedVariant.getLengthCm());
                double price = Validator.userInput(ctx.formParam("edited_variant_price"),pickedVariant.getPrice());
                MaterialVariantDTO newVariant = new MaterialVariantDTO(pickedVariant.getMvId(),pickedVariant.getMaterialId(),length,price);
                if(newVariant != null){
                    if(!newVariant.equals(pickedVariant)){
                        VariantsMapper.updateVariant(connectionPool,newVariant,finalId);
                    }
                }
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



    public static void filterMaterials(ConnectionPool connectionPool, Context ctx) throws DatabaseException{
        String filter = ctx.formParam("filter");
        String alreadySelected = ctx.sessionAttribute("already_selected");
        if(alreadySelected != null && alreadySelected.isEmpty() == false){
            if(filter.equalsIgnoreCase(alreadySelected)){
                loadAdminSite(connectionPool,ctx);
                return;
            }
        }

        if(filter != null && filter.isEmpty() == false){
            if(filter.equalsIgnoreCase("all")){
                List<MaterialDTO> materials = MaterialsMapper.getAllMaterialInfo(connectionPool);
                ctx.sessionAttribute("modified_list",materials);
                ctx.sessionAttribute("already_selected",filter);
            }else{
                List<MaterialDTO> materials = MaterialsMapper.getMaterialInfoByType(connectionPool, Mtype.valueOf(filter));
                ctx.sessionAttribute("modified_list",materials);
                ctx.sessionAttribute("already_selected",filter);
            }
        }
        loadAdminSite(connectionPool, ctx);
    }


    //TODO YOU HAVE ALL THESE METHODS LEFT!
    public static boolean addNewMaterial(ConnectionPool connectionPool, Context ctx) throws Exception{
        String name = Validator.userInput(ctx.formParam("material_name"), "unnamed");
        Mtype type = Mtype.valueOf(ctx.formParam("type_select"));
        int width = Validator.userInput(ctx.formParam("material_width"),10);
        int length = Validator.userInput(ctx.formParam("material_depth"),10);
        MaterialDTO material = null;
        try{
            switch(type.getName()){
                case "beam":
                    material = new BeamDTO(name,type,width,length);
                    break;
                case "cover_planks":
                    material = new CrossbeamDTO(name,type,width,length);
                    break;
                case "pillar":
                    material = new PillarDTO(name,type,width,length);
                    break;
                case "roof":
                    material = new RoofDTO(name,type,width,length);
                    break;
            }
            MaterialsMapper.addMaterial(connectionPool,material);
        }catch(Exception e){
            throw new Exception("Error while adding material");
        }
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

    //TODO: MAYBE ADD FILTER VARIANTS?
}
