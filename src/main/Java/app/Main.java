package app;

import app.config.ThymeleafConfig;
import io.javalin.Javalin;
import io.javalin.rendering.template.JavalinThymeleaf;
import io.javalin.http.Context;

import java.util.Random;


public class Main {

  public static void main(String[] args) {
    // Initializing Javalin and Jetty webserver

    Javalin app = Javalin.create(config -> {
      config.staticFiles.add("/public");
      JavalinThymeleaf.init(ThymeleafConfig.templateEngine());
    }).start(7070);

    // render start:
    // tests
    app.get("/SynchronousVisitsTestPage", Main::testLoading);
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
    ctx.attribute("time", time);
    ctx.render("testPage.html");
    changePeople(-1);
  }

  //// Increasing and Decreasing people synchronously to avoid desyncs.
  private static synchronized void changePeople(int by) {
    people += by;
  }
}