package app.controllers;

import app.entities.CarportChoiceEntity;
import app.entities.OrderDTO;
import app.entities.Status;
import app.persistence.ConnectionPool;
import app.persistence.OrderMapper;
import io.javalin.http.Context;

public class CustomController {
    public void sendOrderDTO(Context ctx, ConnectionPool connectionPool){
        try{
            int widthOption = Integer.parseInt(ctx.formParam("width_option"));
            int lengthOption = Integer.parseInt(ctx.formParam("length_options"));
            //String chosenRoof = ctx.formParam("chosen_roof");
            //int slopeOptions = Integer.parseInt(ctx.formParam("slope_options"));
            int shedWidthOption = Integer.parseInt(ctx.formParam("shed_width_option"));
            int shedLengthOption = Integer.parseInt(ctx.formParam("shed_length_options"));
            String wishChangesText = ctx.formParam("special_wishes_or_notices");

            if(wishChangesText == null){
                wishChangesText = "";
            }


            OrderDTO orderDTO = new OrderDTO(lengthOption,widthOption,shedLengthOption,shedWidthOption,0,false,0.0, Status.initialised,wishChangesText);

            OrderDTO savedOrder = OrderMapper.addOrder(connectionPool,orderDTO);


            ctx.redirect("/congratsYouDidIt");

        }

        catch (Exception e){
            System.out.println(e);
        }
    }
    public void renderCostumCarportFile(Context ctx, ConnectionPool connectionPool){
        try{
            String choice = ctx.formParam("inclined");
            if(choice.equals("carport_with_incline")) {
                ctx.sessionAttribute("roof_type", true);
                ctx.render("costumCarport.html");
            } else if (choice.equals("carport_without_incline")) {
                ctx.sessionAttribute("roof_type",false);
                ctx.render("costumCarport.html");
            }


        }catch (Exception e){
            System.out.println(e);
        }
    }

}



