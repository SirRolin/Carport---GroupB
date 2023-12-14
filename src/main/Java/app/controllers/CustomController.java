package app.controllers;

import app.entities.CarportChoiceEntity;
import app.entities.OrderDTO;
import app.entities.Status;
import app.persistence.ConnectionPool;
import app.persistence.OrderMapper;
import io.javalin.http.Context;

import java.util.Date;

public class CustomController {
    public void sendOrderDTO(Context ctx, ConnectionPool connectionPool){
        try{
            int widthOption = Integer.parseInt(ctx.formParam("width_option"));
            int lengthOption = Integer.parseInt(ctx.formParam("length_options"));
            //String chosenRoof = ctx.formParam("chosen_roof");
            String slopes = ctx.formParam("slope_options");
            int slopeOptions;
            if(slopes == null){
                slopeOptions = 0;
            }else{
                slopeOptions = Integer.parseInt(ctx.formParam("slope_options"));
            }



            int shedWidthOption = Integer.parseInt(ctx.formParam("shed_width_option"));
            int shedLengthOption = Integer.parseInt(ctx.formParam("shed_length_options"));
            String svgText = "";
            String wishChangesText = ctx.formParam("wish_changes_text");



            if(wishChangesText == null){
                wishChangesText ="";
            }
            OrderDTO orderDTO = new OrderDTO(lengthOption,widthOption,shedLengthOption,shedWidthOption,slopeOptions,false,0, Status.initialised,svgText,wishChangesText);


            ctx.sessionAttribute("current_order",orderDTO);
            ctx.render("costumerDetail.html");
            if(ctx.sessionAttribute("current_order") == null){
                System.out.println("it is null for some reason");
            }


        }

        catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    public void sendOrderDtoToDatabase(Context ctx, ConnectionPool connectionPool){
        boolean hasAssembler = Boolean.parseBoolean(ctx.formParam("assembler"));
        try {
            OrderDTO orderDTO = ctx.sessionAttribute("current_order");
            orderDTO.setHasAssembler(hasAssembler);
            OrderDTO finalDTO = new OrderDTO(orderDTO.getId(),orderDTO.getLengthCm(),orderDTO.getWidthCm(),orderDTO.getShedLengthCm(),orderDTO.getShedWidthCm(),orderDTO.getSlopeDegrees(),orderDTO.getAssembler(),orderDTO.getPrice(),orderDTO.getStatus(),orderDTO.getSvg(),orderDTO.getName(),orderDTO.getEmail(),orderDTO.getDate(),orderDTO.getNotice());

            try {
                OrderDTO savedDTO = OrderMapper.addOrder(connectionPool,finalDTO);
                String dtoName = savedDTO.getName();
                ctx.render("index.html");
            }catch (Exception e){
                System.out.println(finalDTO.getName());
                System.out.println("error with db try again");
            }

        } catch (Exception e){
            System.out.println(e);
        }
    }

    public void sendOrderDtoToReciept(Context ctx, ConnectionPool connectionPool){

        try {
            OrderDTO orderDTO = ctx.sessionAttribute("current_order");
            String name = ctx.formParam("costumer_name");
            String email = ctx.formParam("user_email");
            if(email != null || email != "null"){
                orderDTO.setName(name);
                orderDTO.setEmail(email);
                System.out.println(orderDTO.getName());
                System.out.println(orderDTO.getEmail());
                ctx.sessionAttribute("current_order",orderDTO);
            }else if(orderDTO.getName() == null){
                System.out.println("name :" +orderDTO.getName());
            }

        }catch (Exception e){
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



