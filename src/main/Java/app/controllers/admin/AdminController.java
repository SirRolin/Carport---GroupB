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
import java.util.ArrayList;
import java.util.List;

public class AdminController {

    public static void AddRenders(Javalin app, ConnectionPool connectionPool){
        app.get("/admin", ctx -> ctx.render("admin_nav.html"));
        app.get("/editMaterial", ctx -> AdminController.loadAdminSite(connectionPool, ctx));
        app.post("/chooseAddVariantOrMaterial", ctx -> AdminController.addVariantOrMaterial(connectionPool, ctx));
        app.post("/chooseRemoveVariantOrMaterial", ctx -> AdminController.removeVariantOrMaterial(connectionPool,ctx));
        app.post("/editMaterial", ctx -> AdminController.pickEditableMaterial(connectionPool, ctx));
        app.post("/editVariant", ctx -> AdminController.pickEditableVariant(connectionPool, ctx));
        app.post("/filterMaterials", ctx -> AdminController.filterMaterials(connectionPool, ctx));
        app.post("/addNewMaterial", ctx-> addNewMaterial(connectionPool,ctx));
        app.post("/addNewVariant", ctx-> addNewVariant(connectionPool,ctx));
        app.post("/removeMaterial",ctx->removeMaterial(connectionPool, ctx));
        app.post("/removeVariant",ctx->removeVariant(connectionPool,ctx));
        app.get("/login", ctx -> ctx.render("login.html"));
        app.post("/login", ctx -> login(connectionPool,ctx));
    }


    //Loads the adminsite by fetching all the materials and variants needed.
    public static void loadAdminSite(ConnectionPool connectionPool, Context ctx){
        List<MaterialDTO> materials = new ArrayList<>();
        List<MaterialVariantDTO> variants = new ArrayList<>();

        try{
            //Fetches all materials from the materialmapper.
            materials = MaterialsMapper.getAllMaterials(connectionPool);
            variants = VariantsMapper.getVariantInfo(connectionPool);

            //If there has been no filter chosen, meaning there is no modified list, we use the list from the database. Vice versa if a filter has been applied.
            if(ctx.sessionAttribute("modified_list")==null){
                ctx.sessionAttribute("material_list",materials);
            }else {
                ctx.sessionAttribute("material_list",ctx.sessionAttribute("modified_list"));
            }

            //If there has been no filter chosen on the variants list, we use the list from the database. Vice versa if the filter has been applied.
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

    //Chooses between if you want to add a material or variant, then loads the admin site.
    public static void addVariantOrMaterial(ConnectionPool connectionPool, Context ctx){
        //Fetches the formparameter.
        String picked = ctx.formParam("add_select");

        //If the formparameters value equals to material, we set the "add_material" sessionattribute to true and load the site again. If the value equals to variant,
        //we set the "add_material" sessionattribute to false.
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

    //Chooses between if you want to remove a material or variant, then loads the admin site.
    public static void removeVariantOrMaterial(ConnectionPool connectionPool, Context ctx){
        //Fetches the formparameter.
        String picked = ctx.formParam("remove_select");

        //If the formparameters value equals to material, we set the "remove_material" sessionattribute to true and load the site again. If the value equals to variant,
        //we set the "add_material" sessionattribute to false, and then load the site.
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



    /**
     * A method which picks the material that we want to edit, from user input. Makes it modifiable, and then saves it when the user is done.
     * @param connectionPool ConnectionPool
     * @param ctx Context
     */
    public static void pickEditableMaterial(ConnectionPool connectionPool, Context ctx){
        try{
            //fetches formparameter.
            String pickedEdit = ctx.formParam("edit_material");

            //If the formparameter "edit_material" contains "done", we proceed to saving the material.
            if(pickedEdit.contains("done")){
                //Since we're done editing our material, we no longer need to see the variants associated with it. So we set the "showVariants" sessionattribute to false.
                ctx.sessionAttribute("showVariants",false);

                //Since the formparameter "edit_material", now saved as pickedEdit, contains both the word "done" and an index for the material picked.
                //We need to extract the index from the string to use it later. So we split the string on the space.
                String[] editString = pickedEdit.split(" ");
                int id = Integer.parseInt(editString[1]);

                //We fetch all the materials via the "material_list" sessionattribute.
                List<MaterialDTO> materials = ctx.sessionAttribute("material_list");

                //We get the ID from the material we've picked, by using the index we saved earlier as "id" on the material list.
                int finalId = materials.get(id).getMaterialId();

                //We then fetch the material by the id, from the database. We do this to make sure that its the right material we are looking for,
                //and that it exists in the database.
                MaterialDTO pickedMaterial = MaterialsMapper.getMaterialById(connectionPool, finalId);

                //We now validate all input from the user:
                //If the input is unchanged/the same as what the object already is, we just set it to the attribute the object already has.
                String name = Validator.userInput(ctx.formParam("edited_material_name"),pickedMaterial.getName());
                String type = Validator.userInput(ctx.formParam("edited_material_type"),pickedMaterial.getType().toString());
                int width = Validator.userInput(ctx.formParam("edited_material_width"),pickedMaterial.getWidthMm());
                int depth = Validator.userInput(ctx.formParam("edited_material_depth"),pickedMaterial.getDepthMm());

                //We then make a new material, and assign it a type from our type enum.
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
                //If we successfully made a new object, and the object is not the same as the object that is already in our list, we update the material.
                if(newMaterial != null){
                    if(!pickedMaterial.equals(newMaterial)){
                        MaterialsMapper.updateMaterial(connectionPool, newMaterial);
                    }
                }
                //Afterwards we reset all the sessionattributes.
                ctx.sessionAttribute("modified_variant_list", null);
                ctx.sessionAttribute("edit_material",null);
                ctx.sessionAttribute("edit_variant",null);
            }else{
               //If the formparameter "edit_material" does not contain "done", we parse the string from the formparameter to an int.
               //This parameter is equal to the index of the picked material in the material list.
               int pickedEditInt =  Integer.parseInt(pickedEdit);

               //We tell the website that we want to show the list of variants.
               ctx.sessionAttribute("showVariants",true);

               //We fetch the list of materials from the sessionattribute "material_list" that was created when we loaded the site.
               List<MaterialDTO> materials = ctx.sessionAttribute("material_list");

               //We set the "modified_variant_list" to be filtered by the picked material from the "pickedEditInt" integer,
                // by getting the material from the material list and using its MaterialID. This will return a list of all material variants that are connected to that
                // materialId.
               ctx.sessionAttribute("modified_variant_list", filterVariants(connectionPool,materials.get(pickedEditInt).getMaterialId()));
                //we set the "edit_material" sessionattribute to the pickedEditInt integer we made from the formparameter earlier.
                //This is done, to keep track of what material we are currently editing and later saving.
               ctx.sessionAttribute("edit_material",pickedEditInt);
            }
        }catch(Exception e){
            //If anything goes wrong, we reset all the sessionattributes that otherwise would've been changed during this code.
            ctx.sessionAttribute("message","Error while editing material: "+e);
            ctx.sessionAttribute("modified_variant_list", null);
            ctx.sessionAttribute("edit_variant",null);
            ctx.sessionAttribute("edit_material",null);
        }
        //Finally we load the site.
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
                ctx.sessionAttribute("edit_variant",null);
                ctx.sessionAttribute("edit_material",null);
                ctx.sessionAttribute("showVariants",false);
            }else {
                int pickedEditInt = Integer.parseInt(pickedEdit);
                ctx.sessionAttribute("edit_variant",pickedEditInt);
            }
        }catch(Exception e){
            ctx.sessionAttribute("message","Error while fetching variant: "+e);
            ctx.sessionAttribute("edit_variant",null);
            ctx.sessionAttribute("edit_material",null);
            ctx.sessionAttribute("showVariants",false);
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
        Mtype type = Mtype.valueOf(ctx.formParam("type_select").toLowerCase().toString());
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
                ctx.sessionAttribute("logged_in",true);
                ctx.redirect("/admin");
                //loadAdminSite(connectionPool,ctx); //// Vi redirecter for, at der ikke står "/login" i urlen...
                return true;
            }else{
                ctx.attribute("message", "Error while logging in, Try again later");
                ctx.sessionAttribute("logged_in",false);
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
