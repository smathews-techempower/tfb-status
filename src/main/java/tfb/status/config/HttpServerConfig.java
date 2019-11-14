package tfb.status.config;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.errorprone.annotations.Immutable;
import java.util.Objects;
import javax.inject.Singleton;
import org.checkerframework.checker.nullness.qual.Nullable;

/**
 * The configuration for the HTTP server.
 */
@Immutable
@Singleton
public final class HttpServerConfig {
  /**
   * The hostname for the server.  The server accepts incoming HTTP requests
   * matching this hostname only.
   */
  public final String host;

  /**
   * The port number for the server.  The server listens for incoming HTTP
   * requests on this port only.
   */
  public final int port;

  /**
   * The key store having the certificate for the server, enabling HTTPS, or
   * {@code null} if the server is using unencrypted HTTP.
   */
  public final @Nullable KeyStore keyStore;

  @JsonCreator
  public HttpServerConfig(

      @JsonProperty(value = "host", required = false)
      @Nullable String host,

      @JsonProperty(value = "port", required = false)
      @Nullable Integer port,

      @JsonProperty(value = "keyStore", required = false)
      @Nullable KeyStore keyStore) {

    this.host = Objects.requireNonNullElse(host, DEFAULT_HOST);
    this.port = Objects.requireNonNullElse(port, DEFAULT_PORT);
    this.keyStore = keyStore;
  }

  private static final String DEFAULT_HOST = "0.0.0.0";
  private static final int DEFAULT_PORT = 80;

  /**
   * The key store for the HTTP server.
   */
  @Immutable
  public static final class KeyStore {
    public final String path;
    public final String password;

    @JsonCreator
    public KeyStore(

        @JsonProperty(value = "path", required = true)
        String path,

        @JsonProperty(value = "password", required = true)
        String password) {

      this.path = Objects.requireNonNull(path);
      this.password = Objects.requireNonNull(password);
    }
  }
}
