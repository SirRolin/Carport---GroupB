package app.persistence;

import app.entities.OrderDTO;
import app.exceptions.DatabaseException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.*;

class OrderMapperTest {

    private static final String USER = "postgres";
    private static final String PASSWORD = "postgres";
    private static final String URL = "jdbc:postgresql://localhost:5432/%s?currentSchema=public";
    private static final String DB = "carportTest";
    private static ConnectionPool connectionPool = null;

    private static OrderDTO order;

    @BeforeEach
    void setUp(){
        connectionPool = ConnectionPool.getInstance(USER,PASSWORD,URL,DB);
        order = new OrderDTO(0,200,200,100,100,90,true,20.0,"paid");
    }


    //int lengthCm, int widthCm, int shedLengthCm, int shedWidthCm, int slopeDegrees, boolean hasAssembler, double price, String status
    @Test
    void addOrder() throws DatabaseException {
        int orderId = 0;
        String sql = "insert into orders (length_cm, width_cm, shed_length_cm, shed_width_cm, slope_degrees, hire_assembler, status) values (?,?,?,?,?,?,?)";
        try(Connection connection = connectionPool.getConnection()){
            try(PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)){
                ps.setInt(1,order.getLengthCm());
                ps.setInt(2,order.getWidthCm());
                ps.setInt(3,order.getShedLengthCm());
                ps.setInt(4,order.getShedWidthCm());
                ps.setInt(5,order.getSlopeDegrees());
                ps.setBoolean(6,order.isHasAssembler());
                ps.setString(7,order.getStatus());

                int rowsAffected = ps.executeUpdate();
                if(rowsAffected <1){
                    throw new DatabaseException("Error while creating order");
                }

                try(ResultSet rs = ps.executeQuery()){
                    while(rs.next()){
                        orderId = rs.getInt("order_id");
                    }
                }
            }
        }catch(Exception e){
            throw new DatabaseException("Error while connecting");
        }

    }
}