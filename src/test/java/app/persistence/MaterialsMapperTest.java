package app.persistence;

import app.entities.MaterialDTO;
import app.entities.Mtype;
import app.entities.PillarDTO;
import app.exceptions.DatabaseException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class MaterialsMapperTest {

  private static ConnectionPool connectionPool;

  private List<MaterialDTO> mats;
  private MaterialDTO mat;
  @BeforeAll
  static void all_setup(){
    connectionPool = commonTestFunctions.testConnection.getTestConnection();
    assertNotNull(connectionPool);
  }

  @BeforeEach
  void each_setup(){
    try {
      mats = MaterialsMapper.getAllMaterials(connectionPool);
    } catch (DatabaseException e) {
      throw new RuntimeException(e);
    }
    assertFalse(mats.isEmpty());
    Optional<MaterialDTO> testMaterialFromDB = mats.stream().takeWhile(material -> Objects.equals(material.getName(), "MapperTestPillar")).findFirst();
    if(testMaterialFromDB.isPresent()){
      mat = testMaterialFromDB.get();
    } else {
      mat = new PillarDTO("MapperTestPillar", Mtype.pillar, 99, 99);
      try {
        MaterialsMapper.addMaterial(connectionPool, mat);
      } catch (DatabaseException e) {
        throw new RuntimeException(e);
      }
    }
  }

  @Test
  void getMaterialInfoByType() {
    try {
      assertNotNull(MaterialsMapper.getMaterialInfoByType(connectionPool, Mtype.pillar));
    } catch (DatabaseException e) {
      throw new RuntimeException(e);
    }
  }

  @Test
  void getAllMaterialInfo() {
    try {
      assertNotNull(MaterialsMapper.getAllMaterialInfo(connectionPool));
    } catch (DatabaseException e) {
      throw new RuntimeException(e);
    }
  }

  @Test
  void deleteOrderItemsByOrderID() {
    // ToDo needs to be looked at.
  }

  @Test
  void getAllMaterials() {
    try {
      assertNotNull(MaterialsMapper.getAllMaterials(connectionPool));
    } catch (DatabaseException e) {
      throw new RuntimeException(e);
    }
  }

  @Test
  void getAllMaterialsByType() {
    try {
      assertNotNull(MaterialsMapper.getAllMaterialsByType(connectionPool, Mtype.pillar));
    } catch (DatabaseException e) {
      throw new RuntimeException(e);
    }
  }

  @Test
  void updateMaterial() {
    try {
      //// get id so we can find it again.
      int id = mat.getMaterialId();
      //// update amount on the DB
      Random rng = new Random();
      int number = rng.nextInt(100);
      while (number == mat.getWidthMm()){
        number =  rng.nextInt(100);
      }
      mat.setWidth(number);
      assertTrue(MaterialsMapper.updateMaterial(connectionPool, mat));
      //// retrieve the material from the DB which should be updated.
      MaterialDTO newMat = MaterialsMapper.getMaterialById(connectionPool, id);
      assertEquals(mat.getWidthMm(), newMat.getWidthMm());
    } catch (DatabaseException e) {
      throw new RuntimeException(e);
    }
  }

  @Test
  void getMaterialById() {
    try {
      assertNotNull(MaterialsMapper.getMaterialById(connectionPool, mat.getMaterialId()));
    } catch (DatabaseException e) {
      throw new RuntimeException(e);
    }
  }

  @Test
  void addMaterial() {
    try {
      assertTrue(MaterialsMapper.addMaterial(connectionPool, mat));
    } catch (DatabaseException e) {
      throw new RuntimeException(e);
    }
  }

  @Test
  void removeMaterial() {
    try {
      assertTrue(MaterialsMapper.removeMaterial(connectionPool, mat));
    } catch (DatabaseException e) {
      throw new RuntimeException(e);
    }
  }
}