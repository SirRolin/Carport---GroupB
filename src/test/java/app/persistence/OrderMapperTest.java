package app.persistence;

import app.entities.OrderDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OrderMapperTest {


    private static final String USER = "postgres";
    private static final String PASSWORD = "postgres";
    private static final String URL = "jdbc:postgresql://localhost:5432/%s?currentSchema=public";
    private static final String DB = "carport";
    private static ConnectionPool connectionPool = null;

    private OrderDTO order;

    @BeforeEach
    void setup(){
        connectionPool = ConnectionPool.getInstance(USER,PASSWORD,URL,DB);
        order = new OrderDTO(100,100,200,200,90,true,0,"initialised","Test");
    }
    @Test
    void successfulTest(){
        try{
            assertNotNull(OrderMapper.addOrder(connectionPool,order));
        }catch(Exception e){
            e.printStackTrace();
        }

    }

    @Test
    void failedTest(){
        try{
            assertNull(OrderMapper.addOrder(connectionPool,order));
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}