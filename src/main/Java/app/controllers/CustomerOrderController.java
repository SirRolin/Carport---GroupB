package app.controllers;

import app.entities.MaterialDTO;
import app.entities.OrderDTO;
import app.entities.SearchDTO;
import app.entities.Status;
import app.exceptions.DatabaseException;
import app.persistence.ConnectionPool;
import app.persistence.OrderItemMapper;
import app.persistence.OrderMapper;
import io.javalin.Javalin;
import io.javalin.http.Context;

import java.util.List;

public abstract class CustomerOrderController {
  public static void addRender(Javalin app, ConnectionPool connectionPool) {
    //// s brackets aka. {} can be used to get data within those with the name being the same as what's inside the brackets. example look to line 26.
    app.get("/Order/id/{id}/", ctx -> viewOrderByID(ctx, connectionPool));
    app.get("/Order/email/{email}/", ctx -> viewOrderByEmail(ctx, connectionPool));
  }

  private static void viewOrderByID(Context ctx, ConnectionPool connectionPool) {
    SearchDTO search = new SearchDTO();
    try {
      search.setOrderID(Integer.parseInt(ctx.pathParam("id")));
    } catch (NumberFormatException e) {
      ctx.attribute("error_path", ctx.path());
      ctx.attribute("error_message", "Id was not a number.");
      ctx.render("error.html");
      return;
    }
    viewOrder(ctx, connectionPool, search);
  }
  private static void viewOrderByEmail(Context ctx, ConnectionPool connectionPool) {
    SearchDTO search = new SearchDTO();
    search.setEmail(ctx.pathParam("email"));
    try {
      assert search.getEmail() != null;
    } catch (AssertionError e) {
      ctx.attribute("error_path", ctx.path());
      ctx.attribute("error_message", "Something went wrong with the email provided");
      ctx.render("error.html");
      return;
    }
    viewOrder(ctx, connectionPool, search);
  }

  private static void viewOrder(Context ctx, ConnectionPool connectionPool, SearchDTO search) {
    try {
      //// using the searchDTO it gets all the orders related to that search.
      List<OrderDTO> customerOrders = OrderMapper.getOrdersBySearchDTO(search, connectionPool);

      //// if there's multiple we show them a list of orders they can go to, if there's only 1 they get to see it.
      if(customerOrders.size() > 1) {
        ctx.sessionAttribute("the_costumers_orders", customerOrders);
        ctx.render("orderViewSite.html");
      } else {
        OrderDTO customerOrder = customerOrders.get(0);
        ctx.sessionAttribute("customer_order", customerOrder);
        List<MaterialDTO> billOfMaterialListFromDB = OrderItemMapper.getOrderItemsByOrderID(customerOrder.getId(), connectionPool);

        //// if it's paid the customer should be able to see the drawing and the bill.
        if (customerOrder.getStatus().equals(Status.paid)) {
          ctx.sessionAttribute("customer_svg", customerOrder.getSvg());
          ctx.sessionAttribute("customerBillOfMaterial", billOfMaterialListFromDB);

          //// if it's not and the bill has been generated it gives them a hint that they wouldn't see it until they pay.
        } else if (!billOfMaterialListFromDB.isEmpty()) {
          ctx.attribute("notPaidMessage", "Your bill of material is ready to be delivered. But to get access, you will need to pay for your order. please follow the instructions from the email");

          //// if the bill however is empty,  it tells them to wait and contact cs if they are worried.
        } else {
          ctx.attribute("notPaidMessage", "Your bill of material has not been generated yet. please contact customer service if you think think is a mistake");
        }
        ctx.render("orderViewSite.html");
      }

    } catch (DatabaseException e) {
      ctx.attribute("error_path", ctx.path());
      ctx.attribute("error_message", e.getMessage());
      ctx.render("error.html");
      return;
    }
  }
}
