package tfb.status.handler;

import static com.google.common.net.HttpHeaders.CONTENT_TYPE;
import static com.google.common.net.MediaType.HTML_UTF_8;
import static io.undertow.util.StatusCodes.OK;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static tfb.status.util.MoreAssertions.assertContains;
import static tfb.status.util.MoreAssertions.assertMediaType;
import static tfb.status.util.MoreAssertions.assertStartsWith;

import javax.ws.rs.core.Response;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import tfb.status.bootstrap.TestServices;

/**
 * Tests for {@link HomePageHandler}.
 */
public final class HomePageHandlerTest {
  private static TestServices services;

  @BeforeAll
  public static void beforeAll() {
    services = new TestServices();
  }

  @AfterAll
  public static void afterAll() {
    services.shutdown();
  }

  /**
   * Verifies that {@code GET /} produces an HTML response.
   */
  @Test
  public void testGet() {
    try (Response response = services.httpGet("/")) {

      assertEquals(OK, response.getStatus());

      assertMediaType(
          HTML_UTF_8,
          response.getHeaderString(CONTENT_TYPE));

      String body = response.readEntity(String.class);

      assertStartsWith(
          "<!DOCTYPE html>",
          body);

      assertContains(
          "This is the test announcement!",
          body);
    }
  }
}
