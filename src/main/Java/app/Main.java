package app;

import app.config.ThymeleafConfig;
import io.javalin.Javalin;
import io.javalin.rendering.template.JavalinThymeleaf;
import io.javalin.http.Context;

import javax.swing.*;
import java.util.Random;


public class Main {

    //// Testing How many people are trying to access the page
    private static int people = 0;

    public static void main(String[] args)
    {
        // Initializing Javalin and Jetty webserver

        Javalin app = Javalin.create(config -> {
            config.staticFiles.add("/public");
            JavalinThymeleaf.init(ThymeleafConfig.templateEngine());
        }).start(7070);

        // render start:
        app.get("/SynchronousVisitsTestPage", Main::testLoading);
    }

    public static void testLoading(Context ctx){
        changePeople(1);
        ctx.attribute("queue", people);
        ctx.render("testQueue.html"); // Virker ikke. hjemmesiden venter til funktionen er fuldført.
        Random rng = new Random();
        int time = rng.nextInt(5000,10000);
        try {
            Thread.sleep(time);
        } catch (InterruptedException ignored) {}
        ctx.attribute("people", people);
        ctx.attribute("time", time);
        ctx.render("testPage.html");
        changePeople(-1);
    }
    //// Increasing and Decreasing people synchronously to avoid desyncs.
    private static synchronized void changePeople(int by){
        people += by;
    }
}