package app.controllers.admin;

import app.calculator.Calculator;
import app.controllers.OrderEditController;
import app.engine.Engine;
import app.entities.MaterialDTO;
import app.entities.OrderDTO;
import app.exceptions.DatabaseException;
import app.persistence.ConnectionPool;
import app.persistence.MaterialsMapper;
import app.persistence.OrderItemMapper;
import app.persistence.OrderMapper;
import app.svg.SVG;
import io.javalin.Javalin;
import io.javalin.http.Context;

import java.util.ArrayList;
import java.util.List;

public class BillOfMaterialEditController {

  public static void addRenders(Javalin app, ConnectionPool connectionPool) {
    app.post("/showBillOfMaterial", ctx -> BillOfMaterialEditController.showBillOfMaterial(ctx, connectionPool));
    app.post("/back_to_order", ctx -> OrderEditController.loadOrderEditSite(ctx, connectionPool));
    app.post("/submitEditedBillOfMaterial", ctx -> editBillOfMaterial(ctx, connectionPool));
    app.post("/generateBillOfMaterial", ctx -> generateNewBilOfMaterial(ctx, connectionPool));
    //app.post("/getBillOfMaterialByOrderID", ctx -> getBillOfMaterial(ctx,connectionPool));
    app.post("/saveBillOfMaterial", ctx -> saveBilOfMaterialToOrder(ctx, connectionPool));
    app.post("/addMaterialToBilOfMaterial", ctx -> addMaterialToBillOfMaterial(ctx));
  }

  private static void addMaterialToBillOfMaterial(Context ctx) {
    int index = Integer.parseInt(ctx.formParam("selected_material_input"));
    List<MaterialDTO> billOfMaterials = ctx.sessionAttribute("bill_of_materials");
    List<MaterialDTO> listOfMaterials = ctx.sessionAttribute("material_list");
    MaterialDTO chosenMaterial = listOfMaterials.get(index);
    int checkVar = 0;
    for (int i = 0; i < billOfMaterials.size(); i++) {
      if (billOfMaterials.get(i).getMaterialVariantID() == chosenMaterial.getMaterialVariantID()) {
        checkVar++;
        int originalAmount = billOfMaterials.get(i).getAmount();
        billOfMaterials.get(i).setAmount(originalAmount + 1);
        ctx.attribute("message", "The chosen material was already in the bill of material, 1 was added to the quantity");
      }
    }
    if (checkVar < 1) {
      billOfMaterials.add(chosenMaterial);
    }
    ctx.sessionAttribute("bill_of_materials", billOfMaterials);
    ctx.render("billOfMaterialEditSite.html");
  }

  private static void generateNewBilOfMaterial(Context ctx, ConnectionPool connectionPool) {
    OrderDTO currentOrder = ctx.sessionAttribute("chosen_order");


    //// Generating SVG via our Engine.
    List<MaterialDTO> billOfMaterialListFromDBViaEngine = new ArrayList<>();
    SVG svg;
    try {
      assert currentOrder != null;
      ////TODO erstat getPillarID og getBeamID med IDer fra ordren som skal defineres fra bestillingssiden.
      svg = Engine.drawCarportDraft1(billOfMaterialListFromDBViaEngine, connectionPool, currentOrder.getLengthCm(), currentOrder.getWidthCm(), Engine.getPillarID(), Engine.getBeamID());
      ctx.sessionAttribute("svg", svg.toString());
    } catch (DatabaseException e) {
      ctx.attribute("error_function", "Engine.drawCarportDraft1 via showBillOfMaterial(Context ctx, ConnectionPool connectionPool)");
      ctx.attribute("error_path", ctx.path());
      ctx.attribute("error_message", e.getMessage());
      ctx.render("error.html");
      return;
    } catch (AssertionError e) {
      ctx.attribute("error_function", "showBillOfMaterial(Context ctx, ConnectionPool connectionPool)");
      ctx.attribute("error_path", ctx.path());
      ctx.attribute("error_message", e.getMessage());
      ctx.render("error.html");
      return;
    }

    List<MaterialDTO> billOfMaterials;
    // calculates a bill of materials based on the specs of the current order, deletes existing ones first.
    try {
      MaterialsMapper.deleteOrderItemsByOrderID(currentOrder.getId(), connectionPool);
      if(svg == null) {
        billOfMaterials = Calculator.generateBillOfMaterials(currentOrder, connectionPool);
      } else {
        billOfMaterials = billOfMaterialListFromDBViaEngine;
      }
      OrderItemMapper.saveBillOfMaterials(billOfMaterials, currentOrder.getId(), connectionPool);
      ctx.attribute("message", "Generated a new bill of materials based on the spec of the order.");
      ctx.sessionAttribute("bill_of_materials", billOfMaterials);
      //ctx.redirect("/showBillOfMaterial");
      ctx.render("billOfMaterialEditSite.html");
    } catch (DatabaseException e) {
      ctx.attribute("message", "Something went wrong when creating the new bill of material: " + e.getMessage());
      ctx.render("orderEditSite.html");
      return;
    }
  }

  private static void saveBilOfMaterialToOrder(Context ctx, ConnectionPool connectionPool) {
    OrderDTO currentOrder = ctx.sessionAttribute("chosen_order");
    List<MaterialDTO> billOfMaterials = ctx.sessionAttribute("bill_of_materials");
    // saves the bill of materials, deletes existing ones first.
    try {
      MaterialsMapper.deleteOrderItemsByOrderID(currentOrder.getId(), connectionPool);
      OrderItemMapper.saveBillOfMaterials(billOfMaterials, currentOrder.getId(), connectionPool);
      ctx.attribute("message", "Bill of material got updated");
      ctx.sessionAttribute("bill_of_materials", billOfMaterials);
    } catch (DatabaseException e) {
      ctx.attribute("message", "Something went wrong when editing the order: " + e.getMessage());
      ctx.render("orderEditSite.html");
      return;
    }
    // calculates the price and adds it to the order.
    int totalPrice = 0;
    for (MaterialDTO m : billOfMaterials) {
      totalPrice += (m.getAmount() * m.getPrice());
    }
    currentOrder.setPrice(totalPrice);
    ctx.sessionAttribute("chosen_order", currentOrder);
    try {
      OrderMapper.updateOrder(connectionPool, currentOrder);
    } catch (DatabaseException e) {
      ctx.attribute("message", "Was unable to save the order to the db:" + e.getMessage());
    }
    ctx.attribute("message", "The bil of material was saved, price of the materials has been added to the order.");
    ctx.render("billOfMaterialEditSite.html");
  }

  private static void getBillOfMaterial(Context ctx, ConnectionPool connectionPool) {
    // ad logic to handle no bill material issue. should not happen though, as a bill of material
    // is auto generated when we go the bill of material edit site.
  }

  public static void showBillOfMaterial(Context ctx, ConnectionPool connectionPool) {
    List<MaterialDTO> billOfMaterials = ctx.sessionAttribute("bill_of_materials");
    OrderDTO currentOrder = ctx.sessionAttribute("chosen_order");
    List<MaterialDTO> billOfMaterialListFromDB = null;


    List<MaterialDTO> listOfMaterials = null;
    // if there is no material list, we try and get one.
    if (billOfMaterials == null) {
      try {
        billOfMaterialListFromDB = OrderItemMapper.getOrderItemsByOrderID(currentOrder.getId(), connectionPool);
        billOfMaterials = billOfMaterialListFromDB;
      } catch (DatabaseException e) {
        ctx.attribute("message", "was unable get to find order items" + e.getMessage());
        ctx.render("billOfMaterialEditSite.html");
        return;
      }
    }
    // if there is no material list and there is nothing in the db, we generate one and save it.
    if ((billOfMaterials == null || billOfMaterials.isEmpty()) && (billOfMaterialListFromDB == null || billOfMaterialListFromDB.isEmpty())) {
      try {
        billOfMaterials = Calculator.generateBillOfMaterials(currentOrder, connectionPool);
        MaterialsMapper.deleteOrderItemsByOrderID(currentOrder.getId(), connectionPool);
        OrderItemMapper.saveBillOfMaterials(billOfMaterials, currentOrder.getId(), connectionPool);
      } catch (DatabaseException e) {
        ctx.attribute("message", "unable to get any information" + e.getMessage());
        ctx.render("billOfMaterialEditSite.html");
        return;
      }
    }
    ctx.sessionAttribute("bill_of_materials", billOfMaterials);
    try {
      listOfMaterials = MaterialsMapper.getAllMaterialInfo(connectionPool);
    } catch (
        DatabaseException e) {
      ctx.attribute("message", "unable to get materiel information" + e.getMessage());
      ctx.render("billOfMaterialEditSite.html");
      return;
    }
    ctx.sessionAttribute("material_list", listOfMaterials);
    ctx.render("billOfMaterialEditSite.html");
  }

  public static void printBillOfMaterial() {

  }

  public static void editBillOfMaterial(Context ctx, ConnectionPool connectionPool) {
    List<MaterialDTO> currentBillOfMaterial = ctx.sessionAttribute("bill_of_materials");
    int newAmount;
    String[] options;
    options = ctx.formParam("material_input").split("_");
    int indexOfMaterial = Integer.parseInt(options[1]);
    if (options[0].equals("save")) {
      try {
        newAmount = Integer.parseInt(ctx.formParam("newAmount_" + indexOfMaterial));
      } catch (NumberFormatException e) {
        ctx.attribute("message", "You somehow succeeded in not entering a number");
        ctx.render("billOfMaterialEditSite.html");
        return;
      }
      String newDesciption = ctx.formParam("newNotice_" + indexOfMaterial);
      // incase newDescription is null, make sure it's not, to avoid issues later.
      if (newDesciption == null) {
        newDesciption = "No comment";
      }
      OrderDTO chosenOrder = ctx.sessionAttribute("chosen_order");
      if (currentBillOfMaterial.get(indexOfMaterial).getDescription() != null && currentBillOfMaterial.get(indexOfMaterial).getAmount() == newAmount && currentBillOfMaterial.get(indexOfMaterial).getDescription().equals(newDesciption)) {
        ctx.attribute("message", "You entered duplicate info, orderline not saved");
        ctx.sessionAttribute("bill_of_materials", currentBillOfMaterial);
        ctx.render("billOfMaterialEditSite.html");
        return;
      } else {
        currentBillOfMaterial.get(indexOfMaterial).setAmount(newAmount);
        currentBillOfMaterial.get(indexOfMaterial).setDescription(newDesciption);
      }
    }
    if (options[0].equals("delete")) {
      currentBillOfMaterial.remove(indexOfMaterial);
    }
    ctx.attribute("message", "orderline updated");
    ctx.sessionAttribute("bill_of_materials", currentBillOfMaterial);
    ctx.render("billOfMaterialEditSite.html");
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
  }
}
