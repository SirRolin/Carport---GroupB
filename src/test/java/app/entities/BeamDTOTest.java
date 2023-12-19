package app.entities;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BeamDTOTest {

  @Test
  void testEquals() {
    BeamDTO b1 = new BeamDTO(0,"#1 and 2",Mtype.beam,10,10);
    BeamDTO b2 = new BeamDTO(0,"#1 and 2",Mtype.beam,10,10);
    BeamDTO b3 = new BeamDTO(4,"#3",Mtype.beam,20,10);
    Assertions.assertTrue(b1.equals(b2));
    Assertions.assertFalse(b1.equals(b3));
  }
}