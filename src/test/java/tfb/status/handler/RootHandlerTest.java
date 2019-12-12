package tfb.status.handler;

import static io.undertow.util.StatusCodes.INTERNAL_SERVER_ERROR;
import static io.undertow.util.StatusCodes.OK;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.google.common.io.ByteStreams;
import io.undertow.server.HttpHandler;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Objects;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import tfb.status.testlib.HttpTester;
import tfb.status.testlib.LogTester;
import tfb.status.testlib.TestServicesInjector;

/**
 * Tests for {@link RootHandler}.
 */
@ExtendWith(TestServicesInjector.class)
public final class RootHandlerTest {
  /**
   * Verifies that incoming HTTP requests are logged.
   */
  @Test
  public void testRequestLog(HttpTester http, LogTester logs)
      throws IOException, InterruptedException {

    HttpHandler handler = exchange -> {};

    String path = http.addHandler(handler);

    HttpResponse<String> response = http.getString(path);

    assertEquals(OK, response.statusCode());

    assertEquals(
        1,
        logs.getEvents()
            .map(event -> event.getFormattedMessage())
            .filter(message -> message.contains(path))
            .count());
  }

  /**
   * Verifies that exceptions thrown from HTTP handlers are logged.
   */
  @Test
  public void testExceptionLog(HttpTester http, LogTester logs)
      throws IOException, InterruptedException {

    String message = "test exception " + UUID.randomUUID();

    HttpHandler handler =
        exchange -> {
          throw new TestException(message);
        };

    String path = http.addHandler(handler);

    HttpResponse<String> response = http.getString(path);

    assertEquals(INTERNAL_SERVER_ERROR, response.statusCode());

    assertEquals(
        1,
        logs.getEvents()
            .map(event -> event.getThrowableProxy())
            .filter(throwable -> throwable != null)
            .filter(throwable -> Objects.equals(throwable.getMessage(), message))
            .filter(throwable -> Objects.equals(throwable.getClassName(),
                                                TestException.class.getName()))
            .count());
  }

  // Extend IOException so that Undertow's internal logging does not also log
  // the exception.
  private static final class TestException extends IOException {
    TestException(String message) {
      super(Objects.requireNonNull(message));
    }

    private static final long serialVersionUID = 0;
  }

  /**
   * Verifies that HTTP handlers can perform blocking operations.
   */
  @Test
  public void testBlocking(HttpTester http, LogTester logs)
      throws IOException, InterruptedException {

    HttpHandler handler =
        exchange -> {
          assertFalse(exchange.isInIoThread());
          assertTrue(exchange.isBlocking());
          ByteStreams.copy(exchange.getInputStream(),
                           exchange.getOutputStream());
        };

    String path = http.addHandler(handler);

    URI uri = http.uri(path);

    String message =
        "the body of the request, which the server should echo back to us";

    HttpResponse<String> response =
        http.client().send(
            HttpRequest.newBuilder(uri)
                       .POST(HttpRequest.BodyPublishers.ofString(message))
                       .build(),
            HttpResponse.BodyHandlers.ofString());

    assertEquals(OK, response.statusCode());
    assertEquals(message, response.body());
  }
}
