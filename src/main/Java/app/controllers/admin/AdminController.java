package app.controllers.admin;

import app.entities.*;
import app.exceptions.DatabaseException;
import app.persistence.AdminMapper;
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

    public static void AddRenders(Javalin app, ConnectionPool connectionPool){
        app.get("/admin", ctx -> loadAdminSite(connectionPool, ctx));
        app.post("/chooseAddVariantOrMaterial", ctx -> addVariantOrMaterial(connectionPool, ctx));
        app.post("/chooseRemoveVariantOrMaterial", ctx -> removeVariantOrMaterial(connectionPool,ctx));
        app.post("/editMaterial", ctx -> pickEditableMaterial(connectionPool, ctx));
        app.post("/editVariant", ctx -> pickEditableVariant(connectionPool, ctx));
        app.post("/filterMaterials", ctx -> filterMaterials(connectionPool, ctx));
        app.post("/addNewMaterial", ctx-> addNewMaterial(connectionPool,ctx));
        app.post("/addNewVariant", ctx-> addNewVariant(connectionPool,ctx));
        app.post("removeMaterial",ctx->removeMaterial(connectionPool, ctx));
        app.post("removeVariant",ctx->removeVariant(connectionPool,ctx));
        app.post("/login", ctx -> login(connectionPool,ctx));
        app.get("/" , ctx -> ctx.render("login.html"));
    }


    public static void loadAdminSite(ConnectionPool connectionPool, Context ctx){
        List<MaterialDTO> materials = new ArrayList<>();
        List<MaterialVariantDTO> variants = new ArrayList<>();

        try{
            materials = MaterialsMapper.getAllMaterials(connectionPool);
            variants = VariantsMapper.getVariantInfo(connectionPool);
            if(ctx.sessionAttribute("modified_list")==null){
                ctx.sessionAttribute("material_list",materials);
            }else {
                ctx.sessionAttribute("material_list",ctx.sessionAttribute("modified_list"));
            }

            if(ctx.sessionAttribute("modified_variant_list")==null){
                ctx.sessionAttribute("variant_list",variants);
                ctx.sessionAttribute("showVariants",false);
            }else{
                ctx.sessionAttribute("variant_list",ctx.sessionAttribute("modified_variant_list"));
            }

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
                ctx.sessionAttribute("showVariants",false);
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
                    if(!pickedMaterial.equals(newMaterial)){ //TODO VIRKER IKKE
                        MaterialsMapper.updateMaterial(connectionPool, newMaterial);
                    }
                }
                ctx.sessionAttribute("modified_variant_list", null);
                ctx.sessionAttribute("edit_material",-1);
            }else{
               int pickedEditInt =  Integer.parseInt(pickedEdit);
               ctx.sessionAttribute("showVariants",true);
               List<MaterialDTO> materials = ctx.sessionAttribute("material_list");
               ctx.sessionAttribute("modified_variant_list", filterVariants(connectionPool,materials.get(pickedEditInt).getMaterialId()));
               ctx.sessionAttribute("edit_material",pickedEditInt);
            }
        }catch(Exception e){
            ctx.sessionAttribute("message","Error while editing material: "+e);
            ctx.sessionAttribute("modified_variant_list", null);
            ctx.sessionAttribute("edit_material",-1);
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
            ctx.sessionAttribute("message","Error while fetching variant: "+e);
            ctx.sessionAttribute("edit_variant",-1);
        }
        loadAdminSite(connectionPool,ctx);
    }



    public static void filterMaterials(ConnectionPool connectionPool, Context ctx){
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
                try{
                    List<MaterialDTO> materials = MaterialsMapper.getAllMaterials(connectionPool);
                    ctx.sessionAttribute("modified_list",materials);
                    ctx.sessionAttribute("already_selected",filter);
                }catch(DatabaseException e){
                    ctx.sessionAttribute("message","Error while fetching materials: "+e);
                }
            }else{
                try{
                    List<MaterialDTO> materials = MaterialsMapper.getAllMaterialsByType(connectionPool, Mtype.valueOf(filter));
                    ctx.sessionAttribute("modified_list",materials);
                    ctx.sessionAttribute("already_selected",filter);
                }catch(DatabaseException e){
                    ctx.sessionAttribute("message","Error while fetching materials: "+e);
                }
            }
        }
        loadAdminSite(connectionPool, ctx);
    }


    public static boolean addNewMaterial(ConnectionPool connectionPool, Context ctx){
        String name = (Validator.validateString(ctx.formParam("material_name")) ? ctx.formParam("material_name") : null);
        if(name == null){
            loadAdminSite(connectionPool, ctx);
            return false;
        }
        Mtype type = Mtype.valueOf(ctx.formParam("type_select").toLowerCase());
        int width = Validator.userInput(ctx.formParam("material_width"),10);
        int length = Validator.userInput(ctx.formParam("material_depth"),10);
        MaterialDTO material = null;
        try{
            switch(type.toString()){
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
            ctx.sessionAttribute("message","Error while adding material: "+e);
            ctx.sessionAttribute("add_material",null);
            loadAdminSite(connectionPool,ctx);
            return false;
        }
        ctx.sessionAttribute("add_material",null);
        loadAdminSite(connectionPool,ctx);
        return true;
    }

    public static boolean addNewVariant(ConnectionPool connectionPool, Context ctx){
        int materialId = Validator.userInput(ctx.formParam("variant_material_id"),Integer.parseInt(ctx.formParam("variant_material_id")));
        int length = Validator.userInput(ctx.formParam("variant_length"),Integer.parseInt(ctx.formParam("variant_length")));
        double price = Validator.userInput(ctx.formParam("variant_price"),Double.parseDouble(ctx.formParam("variant_price")));
        if(materialId <= 0){
            loadAdminSite(connectionPool, ctx);
        }
        try{
            VariantsMapper.addVariant(connectionPool, new MaterialVariantDTO(materialId,length,price));
        }catch(Exception e){
            ctx.sessionAttribute("message","Error while adding material: "+e);
            loadAdminSite(connectionPool,ctx);
            return false;
        }
        ctx.sessionAttribute("add_material",null);
        loadAdminSite(connectionPool, ctx);
        return true;
    }

    public static boolean removeMaterial(ConnectionPool connectionPool, Context ctx){
        String name = (Validator.validateString(ctx.formParam("material_name")) ? ctx.formParam("material_name") : null);
        if(name == null){
            ctx.sessionAttribute("message","Error! Can't remove a material without its name");
            loadAdminSite(connectionPool, ctx);
            return false;
        }
        Mtype type = Mtype.valueOf(ctx.formParam("type_select").toLowerCase());
        int width = Validator.userInput(ctx.formParam("material_width"),10);
        int length = Validator.userInput(ctx.formParam("material_depth"),10);
        MaterialDTO material = null;
        try{
            switch(type.toString()){
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
            MaterialsMapper.removeMaterial(connectionPool,material);
        }catch(Exception e){
            ctx.sessionAttribute("message","Error! Can't remove a material without its name: "+e);
            loadAdminSite(connectionPool, ctx);
            return false;
        }
        ctx.sessionAttribute("remove_material",null);
        loadAdminSite(connectionPool,ctx);
        return true;
    }

    public static boolean removeVariant(ConnectionPool connectionPool, Context ctx) throws Exception{
        int materialId = Validator.userInput(ctx.formParam("variant_material_id"),Integer.parseInt(ctx.formParam("variant_material_id")));
        int length = Validator.userInput(ctx.formParam("variant_length"),Integer.parseInt(ctx.formParam("variant_length")));
        double price = Validator.userInput(ctx.formParam("variant_price"),Double.parseDouble(ctx.formParam("variant_price")));
        if(materialId <= 0){
            ctx.sessionAttribute("message","Cannot remove variant with an empty ID");
            loadAdminSite(connectionPool, ctx);
            return false;
        }
        try{
            VariantsMapper.removeVariant(connectionPool, new MaterialVariantDTO(materialId,length,price));
        }catch(Exception e){
            ctx.sessionAttribute("message","Failed to remove variant: "+e);
            ctx.sessionAttribute("remove_material",null);
            loadAdminSite(connectionPool, ctx);
            return false;
        }
        ctx.sessionAttribute("remove_material",null);
        loadAdminSite(connectionPool, ctx);
        return true;
    }

    public static List<MaterialVariantDTO> filterVariants(ConnectionPool connectionPool, int id) throws DatabaseException{
        List<MaterialVariantDTO> variants = VariantsMapper.getVariantByMaterialId(connectionPool, id);
        return variants;
    }

    public static boolean login(ConnectionPool connectionPool, Context ctx){
        String name = ctx.formParam("username");
        String password = ctx.formParam("password");
        try{
            Admin admin = AdminMapper.login(connectionPool,name,password);
            if(admin != null){
                ctx.sessionAttribute("loggedIn",true);
                loadAdminSite(connectionPool,ctx);
                return true;
            }else{
                ctx.attribute("message", "Error while logging in, Try again later");
                ctx.sessionAttribute("loggedIn",false);
                ctx.render("login.html");
            }
        }catch(Exception e){
            ctx.attribute("message", "Error while logging in, Try again later");
            ctx.render("login.html");
            System.out.println(e);

        }
        return false;
    }

}
