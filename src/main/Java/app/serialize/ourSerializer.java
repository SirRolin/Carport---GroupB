package app.serialize;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public abstract class ourSerializer {
  public static byte[] serializeObject(Serializable obj) {
    try (ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
         ObjectOutputStream objectStream = new ObjectOutputStream(byteStream)) {

      // Serialize the object to the byte array
      objectStream.writeObject(obj);

      // Return the byte array
      return byteStream.toByteArray();
    } catch (IOException e) {
      e.printStackTrace();
      return null;
    }
  }
  public static String byteArrayToString(byte[] byteArray) {
    // Convert the byte array to a String
    return new String(byteArray);
  }
}
