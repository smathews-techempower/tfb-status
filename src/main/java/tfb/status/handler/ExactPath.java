package tfb.status.handler;

import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.glassfish.hk2.api.Factory;

/**
 * Indicates that an {@link HttpHandler} handles requests whose paths are
 * exactly equal to the specified string.  The {@link RootHandler} will forward
 * those requests to that handler.
 *
 * <p>This annotation may be used on classes that implement {@link HttpHandler}
 * and on classes that implement {@link Factory Factory&lt;HttpHandler&gt;}.
 *
 * <p>In the annotated handler's {@link
 * HttpHandler#handleRequest(HttpServerExchange)} method, the {@linkplain
 * HttpServerExchange#getRelativePath() relative path} of the exchange will be
 * empty.
 *
 * @see RootHandler
 * @see PrefixPath
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ExactPath {
  /**
   * The exact path string to be matched.
   */
  String value();
}
