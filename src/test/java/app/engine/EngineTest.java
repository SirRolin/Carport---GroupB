package app.engine;

import app.Main;
import app.entities.MaterialDTO;
import app.exceptions.DatabaseException;
import app.persistence.ConnectionPool;
import app.svg.SVG;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class EngineTest {

  private static ConnectionPool connectionPool;
  @BeforeAll
  static void setup(){
    connectionPool = ConnectionPool.getInstance("postgres", "postgres", "jdbc:postgresql://localhost:5432/%s?currentSchema=public", "carport_dev");
  }

  @Test
  void getBeamID() {
    int beamID = 0;
    try {
      beamID = Engine.getBeamID();
    } catch (DatabaseException e) {
      assertNotNull(connectionPool);
    }
    assertTrue(beamID > 0);
  }

  @Test
  void getPillarID() {
    int beamID = 0;
    try {
      beamID = Engine.getBeamID();
    } catch (DatabaseException e) {
      assertNotNull(connectionPool);
    }
    assertTrue(beamID > 0);
  }
  @Test
  void drawCarportDraft1() {
    List<MaterialDTO> list = new ArrayList<>();
    SVG svg;
    try {
      svg = Engine.drawCarportDraft1(list, connectionPool, 500, 500, 1, 2);
    } catch (DatabaseException e) {
      throw new RuntimeException(e);
    }
    assertNotEquals("", svg.toString());
  }
}