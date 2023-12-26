package app;

import app.config.ThymeleafConfig;
import app.controllers.CustomController;
import app.controllers.ViewCustomerOrdersController;
import app.controllers.admin.OrderEditController;
import app.controllers.admin.AdminController;
import app.controllers.admin.BillOfMaterialEditController;
import app.persistence.ConnectionPool;
import io.javalin.Javalin;
import io.javalin.rendering.template.JavalinThymeleaf;
import io.javalin.http.Context;

import java.util.Random;


public class Main {

    private static final String DEFAULT_USER = "postgres";
    private static final String DEFAULT_PASSWORD = "postgres";
    private static final String DEFAULT_URL = "jdbc:postgresql://localhost:5432/%s?currentSchema=public";
    private static final String DEFAULT_DB = "carport";

    public static ConnectionPool connectionPool = null;

    public static void main(String[] args) {
        // Initializing Javalin and Jetty webserver

        Javalin app = Javalin.create(config -> {
            config.staticFiles.add("/public");
            JavalinThymeleaf.init(ThymeleafConfig.templateEngine());
        }).start(7070);

        ConstructConnectionPool();
        //// render start:

        //// Index / start side
        app.get("/", ctx -> ctx.render("index.html"));

        //// Order edit site:
        try {
            OrderEditController.addRenders(app, connectionPool);
        } catch (Exception ignore) {

        }

        //// Bill of Material edit site:
        try {
            BillOfMaterialEditController.addRenders(app, connectionPool);
        } catch (Exception ignore) {

        }
        //// Order View Site:
        try {
            ViewCustomerOrdersController.addRenders(app, connectionPool);
        } catch (Exception ignore) {

        }

        //// Admin
        try{
            AdminController.AddRenders(app, connectionPool);
        } catch (Exception ignore) {

        }

        try{
            CustomController.addRender(app, connectionPool);
        } catch (Exception ignore) {

        }
        // tests
        app.get("/SynchronousVisitsTestPage", Main::testLoading);
    }

    public static void ConstructConnectionPool(){
        if(connectionPool== null) {
            try {
                connectionPool = connectionPool.getInstance(DEFAULT_USER, DEFAULT_PASSWORD, DEFAULT_URL, DEFAULT_DB);
            } catch (Exception ignored) {

            }
        }
    }
    public static ConnectionPool getConnectionPool(){
        ConstructConnectionPool();
        return connectionPool;
    }

    //// Testing Section:
    //// Testing How many people are trying to access the page
    private static int people = 0;

    //// Testing of parallel processing via Javalin/Jetty - Success
    //// Testing of buffer page - Failure
    public static void testLoading(Context ctx) {
        changePeople(1);
        ctx.attribute("queue", people);
        ctx.render("testQueue.html"); // Virker ikke. hjemmesiden venter til funktionen er fuldført.
        Random rng = new Random();
        int time = rng.nextInt(5000, 10000);
        try {
            Thread.sleep(time);
        } catch (InterruptedException ignored) {
        }
        ctx.attribute("people", people);
        ctx.render("testpage.html");
        changePeople(-1);
    }

    //// Increasing and Decreasing people synchronously to avoid desyncs.
    private static synchronized void changePeople(int by) {
        people += by;
    }

}