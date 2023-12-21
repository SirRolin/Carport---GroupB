package app.controllers;

import app.entities.OrderDTO;
import app.entities.Status;
import app.persistence.ConnectionPool;
import app.persistence.OrderMapper;
import io.javalin.Javalin;
import io.javalin.http.Context;

public class CustomController {
  //renders the website itself for the user and "constructs" the orderDTO
  public static void addRender(Javalin app, ConnectionPool connectionPool) {
    app.get("/submitCustomCarport", ctx -> ctx.render("submitCustomCarport.html")); //// TODO Christian er der ikke MERE logik der mangler her???
    app.post("/costumerDetail", ctx -> sendOrderDTO(ctx, connectionPool));
    app.post("/checkout", ctx -> sendOrderDtoToReceipt(ctx, connectionPool));
    app.post("/receipt", ctx -> sendOrderDtoToDatabase(ctx, connectionPool));
    app.get("/customCarport/1", ctx -> ctx.render("customCarport1.html"));
    app.get("/customCarport/flat", ctx -> renderCustomCarportFile(ctx, connectionPool, false));
    app.get("/customCarport/inclined", ctx -> renderCustomCarportFile(ctx, connectionPool, true));
  }

  //add the detail for the order for the costumer
  public static void sendOrderDTO(Context ctx, ConnectionPool connectionPool) {
    try {
      int widthOption = Integer.parseInt(ctx.formParam("width_option"));
      int lengthOption = Integer.parseInt(ctx.formParam("length_options"));
      String slopes = ctx.formParam("slope_options");
      int slopeOptions;
      if (slopes == null) {
        slopeOptions = 0;
      } else {
        slopeOptions = Integer.parseInt(ctx.formParam("slope_options"));
      }


      int shedWidthOption = Integer.parseInt(ctx.formParam("shed_width_option"));
      int shedLengthOption = Integer.parseInt(ctx.formParam("shed_length_options"));
      // TODO: combine svg generator with costumer order generator
      String svgText = "";
      String wishChangesText = ctx.formParam("wish_changes_text");


      if (wishChangesText == null) {
        wishChangesText = "";
      }
      OrderDTO orderDTO = new OrderDTO(lengthOption, widthOption, shedLengthOption, shedWidthOption, slopeOptions, false, 0, Status.initialised, svgText, wishChangesText);


      ctx.sessionAttribute("current_order", orderDTO);
      ctx.render("costumerDetail.html");
      if (ctx.sessionAttribute("current_order") == null) {
        System.out.println("it is null for some reason");
      }


    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
  }

  //final construction of the dto contains name,email and specification for the carport
  public static void sendOrderDtoToDatabase(Context ctx, ConnectionPool connectionPool) {
    boolean hasAssembler = Boolean.parseBoolean(ctx.formParam("assembleCarport"));
    try {
      OrderDTO orderDTO = ctx.sessionAttribute("current_order");


      try {
        ////fejler grundet id ikke tildelt endnu.
        //OrderDTO finalDTO = new OrderDTO(orderDTO.getId(), orderDTO.getLengthCm(), orderDTO.getWidthCm(), orderDTO.getShedLengthCm(), orderDTO.getShedWidthCm(), orderDTO.getSlopeDegrees(), orderDTO.getAssembler(), orderDTO.getPrice(), orderDTO.getStatus(), orderDTO.getSvg(), orderDTO.getName(), orderDTO.getEmail(), orderDTO.getDate(), orderDTO.getNotice());
        OrderDTO finalDTO = new OrderDTO(orderDTO.getLengthCm(), orderDTO.getWidthCm(), orderDTO.getShedLengthCm(), orderDTO.getShedWidthCm(), orderDTO.getSlopeDegrees(), orderDTO.getAssembler(), orderDTO.getPrice(), orderDTO.getStatus(), orderDTO.getSvg(), orderDTO.getName(), orderDTO.getEmail(), orderDTO.getNotice());
        finalDTO.setHasAssembler(hasAssembler);
        OrderMapper.addOrder(connectionPool, finalDTO);
        ctx.render("/receipt.html");
      } catch (Exception e) {
        System.out.println("error with db try again");
        ctx.attribute("error_function", "sendOrderDtoToDatabase(Context ctx, ConnectionPool connectionPool)");
        ctx.attribute("error_path", ctx.path());
        ctx.attribute("error_message", e.getMessage());
        ctx.render("/error.html");
      }

    } catch (Exception e) {
      System.out.println(e);
    }
  }


  //adds the final piece to the dto where the costumer chooses if they want to have fog assemble it or not
  public static void sendOrderDtoToReceipt(Context ctx, ConnectionPool connectionPool) {

    try {
      OrderDTO orderDTO = ctx.sessionAttribute("current_order");
      String name = ctx.formParam("costumer_name");

      String email = ctx.formParam("user_email");
      orderDTO.setName(name);

      orderDTO.setEmail(email);
      ctx.sessionAttribute("current_order", orderDTO);


    } catch (Exception e) {
      System.out.println(e);
    }
    //ctx.redirect("/checkOut");
    ctx.render("checkOut.html");
  }

  //checks what sort of roof the user wants inclined or non inclined
  public static void renderCustomCarportFile(Context ctx, ConnectionPool connectionPool, boolean rooftype) {
    ctx.sessionAttribute("roof_type", rooftype);
    ctx.render("customCarport2.html");
  }

}



