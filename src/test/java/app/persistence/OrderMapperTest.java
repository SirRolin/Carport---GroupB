package app.persistence;

import app.entities.OrderDTO;
import app.entities.SearchDTO;
import app.entities.Status;
import app.exceptions.DatabaseException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class OrderMapperTest {


    private static Status status;
    private OrderDTO order;
    private static ConnectionPool connectionPool;
    @BeforeAll
    static void all_setup(){
        connectionPool = commonTestFunctions.testConnection.getTestConnection();
        assertNotNull(connectionPool);
    }

    @BeforeEach
    void setup(){
        order = new OrderDTO(100,100,20,0,0,true,20.0,Status.paid,"blank","OrderMapperTest","Order@Mapper.test","Notice");
    }

    @Test
    void addOrder() {
      try {
        assertNotNull(OrderMapper.addOrder(connectionPool, order));
      } catch (DatabaseException e) {
        throw new RuntimeException(e);
      }
    }

    @Test
    void updateOrder() {
        try {
            SearchDTO search = new SearchDTO();
            search.setEmail("Order@Mapper.test");
            List<OrderDTO> orders = OrderMapper.getOrdersBySearchDTO(search, connectionPool);
            assertNotNull(orders);
            int ordersID = orders.get(0).getId();
            order.setID(ordersID);
            order.setDate(new java.sql.Date(new Date().getTime()));
            //order = new OrderDTO(currentOrder.getId(),currentOrder.getLengthCm(),currentOrder.getWidthCm(),currentOrder.getShedLengthCm(),currentOrder.getShedWidthCm(),currentOrder.getSlopeDegrees(),currentOrder.getAssembler(),currentOrder.getPrice(),Status.paid,currentOrder.getSvg(),currentOrder.getName(),currentOrder.getEmail(),currentOrder.getDate(),currentOrder.getNotice());
            assertEquals(Status.paid, order.getStatus());
            assertTrue(OrderMapper.updateOrder(connectionPool, order));
            order.setStatus(Status.waiting_for_customer);
            //order = new OrderDTO(currentOrder.getId(),currentOrder.getLengthCm(),currentOrder.getWidthCm(),currentOrder.getShedLengthCm(),currentOrder.getShedWidthCm(),currentOrder.getSlopeDegrees(),currentOrder.getAssembler(),currentOrder.getPrice(),Status.waiting_for_customer,currentOrder.getSvg(),currentOrder.getName(),currentOrder.getEmail(),currentOrder.getDate(),currentOrder.getNotice());
            assertTrue(OrderMapper.updateOrder(connectionPool, order));
        } catch (DatabaseException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Test
    void getOrdersBySearchDTO() {
        try {
            SearchDTO search = new SearchDTO();
            search.setEmail("Order@Mapper.test");
            List<OrderDTO> orders = OrderMapper.getOrdersBySearchDTO(search, connectionPool);
            assertNotNull(orders);
        } catch (DatabaseException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    /**
     * Nicklasses lege funktion
     */
    @Test
    void successfulTest(){
        try{
            assertNotNull(OrderMapper.addOrder(connectionPool, order));
        }catch(Exception e){
            e.printStackTrace();
        }

    }
    /*
    @Test
    void failedTest(){
        try{
            assertNull(OrderMapper.addOrder(connectionPool, order));
        }catch (Exception e){
            e.printStackTrace();
        }
    }*/
}