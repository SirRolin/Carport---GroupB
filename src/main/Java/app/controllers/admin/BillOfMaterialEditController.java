package app.controllers.admin;

import app.calculator.Calculator;
import app.controllers.OrderEditController;
import app.entities.MaterialDTO;
import app.entities.OrderDTO;
import app.exceptions.DatabaseException;
import app.persistence.ConnectionPool;
import app.persistence.MaterialsMapper;
import app.persistence.OrderItemMapper;
import io.javalin.Javalin;
import io.javalin.http.Context;

import java.util.ArrayList;
import java.util.List;

public class BillOfMaterialEditController {

    public static void addRenders(Javalin app,ConnectionPool connectionPool){
        app.post("/showBillOfMaterial",ctx -> BillOfMaterialEditController.showBillOfMaterial(ctx, connectionPool));
        app.post("/back_to_order",ctx -> OrderEditController.loadOrderEditSite(ctx,connectionPool));
        app.post("/submitEditedBillOfMaterial", ctx -> editBillOfMaterial(ctx, connectionPool));
        app.post("/getBillOfMaterialByOrderID", ctx -> getBillOfMaterial(ctx,connectionPool));
    }

    private static void getBillOfMaterial(Context ctx, ConnectionPool connectionPool) {

    }

    public static void showBillOfMaterial(Context ctx, ConnectionPool connectionPool) {
        List<MaterialDTO> materialList = ctx.sessionAttribute("bill_of_materials");
        OrderDTO currentOrder = ctx.sessionAttribute("chosen_order");
        List<MaterialDTO> materialListFromDB = null;
        // if there is no material list, we try and get one.
        if(materialList == null) {
            try {
                materialListFromDB = OrderItemMapper.getOrderItemsByOrderID(currentOrder.getId(), connectionPool);
                materialList = materialListFromDB;
            } catch (DatabaseException e) {
                ctx.attribute("message", "was unable get to find order items"+e.getMessage());
                ctx.render("billOfMaterialEditSite.html");
            }
        }
        // if there is no material list and there is nothing in the db, we generate one and save it.
        if((materialList == null || materialList.isEmpty() ) && (materialListFromDB == null || materialListFromDB.isEmpty())){
            try {
                materialList = Calculator.generateBillOfMaterials(currentOrder, connectionPool);
                OrderItemMapper.saveBillOfMaterials(materialList,currentOrder.getId(),connectionPool);
            }catch (DatabaseException e){
                ctx.attribute("message", "unable to get any information"+e.getMessage());
                ctx.render("billOfMaterialEditSite.html");
            }
        }
        ctx.sessionAttribute("bill_of_materials",materialList);
        ctx.render("billOfMaterialEditSite.html");
    }

    public static void printBillOfMaterial(){

    }
    public static void editBillOfMaterial(Context ctx,ConnectionPool connectionPool){
        List<MaterialDTO> currentBillOfMaterial = ctx.sessionAttribute("bill_of_materials");
        int newAmount;
        String newDesciption = ctx.formParam("new_desciption_input");
        int editedMaterialID;
        OrderDTO chosenOrder = ctx.sessionAttribute("chosen_order");
        int orderID = chosenOrder.getId();
        try {
            editedMaterialID = Integer.parseInt(ctx.formParam("material_input"));
            newAmount = Integer.parseInt(ctx.formParam("new_amount_input"));
        }catch (NumberFormatException e){
            ctx.attribute("message", "No Order ID matched what you entered");
            ctx.render("billOfMaterialEditSite.html");
            return;
        }
       /* for (MaterialDTO m: currentBillOfMaterial) {
            if(m.getMaterialId() == editedMaterialID && m.getMaterialVariantID() == ){
                m.setAmount(newAmount);
                m.setDescription(newDesciption);
                ctx.attribute("message", "Bill of materials saved");
            }

        }
        */
        /*
        currentBillOfMaterial.stream()
                .filter(orderLine -> orderLine.getMaterialVariantID() == editedMaterialID)
                .forEach(orderLine -> {
                    // checks if the inputs are different from the original content, if yes, its saved.
                    if (newAmount != orderLine.getAmount() || !newDesciption.equals(orderLine.getDescription())) {
                        orderLine.setAmount(newAmount);
                        orderLine.setDescription(newDesciption);
                        ctx.attribute("message", "Bill of materials saved");
                        try {
                            OrderItemMapper.updateOrderItem(orderLine, orderID, connectionPool);
                        } catch (DatabaseException e) {
                            ctx.attribute("message", "Database error:" +e.getMessage());
                            ctx.render("billOfMaterialEditSite.html");
                            return;
                        }
                    }else{
                        ctx.attribute("message", "You entered duplicate info, orderline not saved");
                    }
                });
                */
        ctx.sessionAttribute("bill_of_materials",currentBillOfMaterial);
        ctx.render("billOfMaterialEditSite.html");
    }
}
