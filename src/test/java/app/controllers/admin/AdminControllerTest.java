package app.controllers.admin;

import app.Main;
import app.config.ThymeleafConfig;
import app.persistence.ConnectionPool;
import com.zaxxer.hikari.HikariDataSource;
import io.javalin.Javalin;
import io.javalin.rendering.template.JavalinThymeleaf;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AdminControllerTest {


    private static final String DEFAULT_USER = "postgres";
    private static final String DEFAULT_PASSWORD = "postgres";
    private static final String DEFAULT_URL = "jdbc:postgresql://localhost:5432/%s?currentSchema=public";
    private static final String DEFAULT_DB = "cupcake";

    public static ConnectionPool connectionPool = null;

    @BeforeEach
    void setup(){
    }
    @Test
    void loadAdminSite() {
    }
}