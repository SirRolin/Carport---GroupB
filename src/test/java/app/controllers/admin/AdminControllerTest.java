package app.controllers.admin;

import MockContext.MockContext;
import app.Main;
import app.config.ThymeleafConfig;
import app.entities.PillarDTO;
import app.persistence.ConnectionPool;
import app.persistence.MaterialsMapper;
import com.zaxxer.hikari.HikariDataSource;
import io.javalin.Javalin;
import io.javalin.http.Context;
import io.javalin.rendering.template.JavalinThymeleaf;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Rolin - Efter bokset med dette i en time, fandt jeg ud af det nok er umuligt at teste på ting der er afhængige af en Context.
 */
class AdminControllerTest {

    private static ConnectionPool connectionPool;
    @BeforeAll
    static void setup(){
        connectionPool = ConnectionPool.getInstance("postgres", "postgres", "jdbc:postgresql://localhost:5432/%s?currentSchema=public", "carport_dev");
    }
}