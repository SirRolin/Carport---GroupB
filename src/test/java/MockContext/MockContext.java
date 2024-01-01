package MockContext;
import io.javalin.http.Context;

import java.util.HashMap;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * After testing found out this doesn't work.
 */
public class MockContext {
  HashMap<String, Object> sessionAttributes = new HashMap<>();
  HashMap<String, Object> attributes = new HashMap<>();

  public Context context = mock(Context.class);
  public void setup(){
    when(context.sessionAttribute("id")).thenReturn("id");
  }
}
