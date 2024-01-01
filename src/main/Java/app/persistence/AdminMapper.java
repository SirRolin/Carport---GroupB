package app.persistence;

import app.entities.Admin;
import app.exceptions.DatabaseException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class AdminMapper {

    /**
     * A method that attempts to log in the admin via a username and a password.
     * @param connectionPool ConnectionPool.
     * @param username String with the username.
     * @param password String with the password.
     * @return Admin Object with the username and password as attributes.
     * @throws DatabaseException
     */
    public static Admin login(ConnectionPool connectionPool, String username, String password) throws DatabaseException{
        Admin admin = null;
        String sql = "select * from Admin where username = ? and password = ?";
        try(Connection connection = connectionPool.getConnection()){
            try(PreparedStatement ps = connection.prepareStatement(sql)){
                ps.setString(1,username);
                ps.setString(2,password);

                ResultSet rs = ps.executeQuery();
                while(rs.next()){
                    String name = rs.getString("username");
                    String pass = rs.getString("password");
                    admin = new Admin(name,password);
                }
            }catch (Exception e){
                throw new DatabaseException("Error while fetching admin: "+e);
            }
        }catch (Exception e){
            throw new DatabaseException("Error while connecting to database: " +e);
        }
        return admin;
    }

}
