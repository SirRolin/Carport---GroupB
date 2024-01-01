package commonTestFunctions;

import app.persistence.ConnectionPool;

public class testConnection {
  public static ConnectionPool getTestConnection(){
    return ConnectionPool.getInstance("postgres", "postgres", "jdbc:postgresql://localhost:5432/%s?currentSchema=public", "carport_dev");
  }
}
