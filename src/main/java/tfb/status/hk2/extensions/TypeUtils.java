package tfb.status.hk2.extensions;

import com.google.common.reflect.ImmutableTypeToInstanceMap;
import com.google.common.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.Objects;
import java.util.stream.Stream;

/**
 * Utility methods for working with {@link Type}.
 */
final class TypeUtils {
  private TypeUtils() {
    throw new AssertionError("This class cannot be instantiated");
  }

  static boolean containsTypeVariable(Type type) {
    Objects.requireNonNull(type);

    // We want to call TypeToken#rejectTypeVariables(), but that method is
    // package-private.  Instead, call a public method in that package that is
    // known to call rejectTypeVariables().
    try {
      Object ignored = ImmutableTypeToInstanceMap.of().getInstance(TypeToken.of(type));
    } catch (IllegalArgumentException e) {
      return true;
    }
    return false;
  }

  static Class<?> getRawType(Type type) {
    Objects.requireNonNull(type);
    return TypeToken.of(type).getRawType();
  }

  static Type resolveType(Type contextType, Type dependentType) {
    Objects.requireNonNull(contextType);
    Objects.requireNonNull(dependentType);
    return TypeToken.of(contextType)
                    .resolveType(dependentType)
                    .getType();
  }

  static Stream<Type> getTypes(Type type) {
    Objects.requireNonNull(type);
    return TypeToken.of(type)
                    .getTypes()
                    .stream()
                    .map(token -> token.getType());
  }

  static boolean isSupertype(Type a, Type b) {
    Objects.requireNonNull(a);
    Objects.requireNonNull(b);
    return TypeToken.of(a).isSupertypeOf(b);
  }

  static boolean isSubtype(Type a, Type b) {
    Objects.requireNonNull(a);
    Objects.requireNonNull(b);
    return TypeToken.of(a).isSubtypeOf(b);
  }
}
