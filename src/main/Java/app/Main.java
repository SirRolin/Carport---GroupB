package app;

import app.config.ThymeleafConfig;
import app.persistence.ConnectionPool;
import io.javalin.Javalin;
import io.javalin.rendering.template.JavalinThymeleaf;
import app.controllers.CustomController;
import io.javalin.http.Context;

import java.util.TimerTask;


public class Main {
private static ConnectionPool connectionPool = null;
    //// Testing How many people are trying to access the page
    private static int people = 0;

    public static void main(String[] args)
    {
        // Initializing Javalin and Jetty webserver

        Javalin app = Javalin.create(config -> {
            config.staticFiles.add("/public");
            JavalinThymeleaf.init(ThymeleafConfig.templateEngine());
        }).start(7070);

        CustomController customController = new CustomController();
        connectionPool = connectionPool.getInstance();

        // render start:
        app.get("/",ctx ->ctx.render("index.html"));
        app.post("/costumCarport",ctx ->customController.renderCostumCarportFile(ctx,connectionPool));


        //app.get("/SynchronousVisitsTestPage", ctx -> testLoading(ctx));
    }

    /*public static void testLoading(Context ctx){
        changePeople(1);
        try {
            Thread.sleep(10000);
        } catch (InterruptedException ignored) {}
        ctx.attribute("people", people);
        ctx.render("testpage.html");
        changePeople(-1);
    }
    //// Increasing and Decreasing people synchronously to avoid desyncs.
    private static synchronized void changePeople(int by){
        people += by;
    }*/



}