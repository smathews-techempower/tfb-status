package tfb.status.service;

import com.google.common.base.Ticker;
import com.google.common.base.Stopwatch;
import javax.inject.Singleton;
import org.glassfish.hk2.api.Factory;

/**
 * Provides the standard {@link Ticker} used by this application.
 *
 * <p>Use this ticker to measure elapsed time.  Avoid using APIs that implicitly
 * rely on the system clock.  This usage pattern allows us to swap in a "fake"
 * ticker during tests and manipulate the apparent passage of time.
 *
 * <table>
 *   <caption>APIs to avoid and their replacements</caption>
 *   <tr>
 *   <th>Don't use this
 *   <th>Use this instead
 *   <tr>
 *   <td>{@link System#nanoTime()}
 *   <td>{@link Ticker#read()}
 *   <tr>
 *   <td>{@link Stopwatch#createStarted()}
 *   <td>{@link Stopwatch#createStarted(Ticker)}
 * </table>
 */
@Singleton
public final class StandardTicker implements Factory<Ticker> {
  @Override
  @Singleton
  public Ticker provide() {
    return Ticker.systemTicker();
  }

  @Override
  public void dispose(Ticker instance) {
    // No cleanup required.
  }
}
