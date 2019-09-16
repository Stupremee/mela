package com.github.stupremee.mela.command.bind;

import com.github.stupremee.mela.command.guice.annotation.ArgumentInterceptors;
import com.github.stupremee.mela.command.guice.annotation.ExceptionHandlers;
import com.github.stupremee.mela.command.guice.annotation.CommandInterceptors;
import com.github.stupremee.mela.command.guice.annotation.ArgumentMappers;
import com.google.inject.Inject;
import com.google.inject.Key;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.annotation.Annotation;
import java.util.Map;
import java.util.Objects;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * @author Johnny_JayJay (https://www.github.com/JohnnyJayJay)
 */
public final class CommandBindings {

  public static final CommandBindings EMPTY = new CommandBindings(Map.of(), Map.of(), Map.of(), Map.of());

  private final Map<Class<? extends Annotation>,  CommandInterceptor<?>> commandInterceptors;
  private final Map<Class<? extends Throwable>, ExceptionHandler<?>> handlers;
  private final Map<Key<?>, ArgumentMapper<?>> mappers;
  private final Map<Class<? extends Annotation>, MappingInterceptor<?>> argumentInterceptors;

  @Inject
  public CommandBindings(@CommandInterceptors Map<Class<? extends Annotation>, CommandInterceptor<?>> commandInterceptors,
                         @ExceptionHandlers Map<Class<? extends Throwable>, ExceptionHandler<?>> handlers,
                         @ArgumentMappers Map<Key<?>, ArgumentMapper<?>> mappers,
                         @ArgumentInterceptors Map<Class<? extends Annotation>, MappingInterceptor<?>> argumentInterceptors) {
    this.commandInterceptors = Map.copyOf(commandInterceptors);
    this.handlers = Map.copyOf(handlers);
    this.mappers = Map.copyOf(mappers);
    this.argumentInterceptors = Map.copyOf(argumentInterceptors);
  }

  @SuppressWarnings("unchecked")
  @Nullable
  public <T extends Annotation> MappingInterceptor<T> getArgumentInterceptor(@Nonnull Class<T> annotationType) {
    checkNotNull(annotationType);
    return (MappingInterceptor<T>) argumentInterceptors.get(annotationType);
  }

  @SuppressWarnings("unchecked")
  @Nullable
  public <T extends Annotation> CommandInterceptor<T> getCommandInterceptor(@Nonnull Class<T> annotationType) {
    checkNotNull(annotationType);
    return (CommandInterceptor<T>) commandInterceptors.get(annotationType);
  }

  @SuppressWarnings("unchecked")
  @Nullable
  public <T extends Throwable> ExceptionHandler<T> getHandler(@Nonnull Class<T> exceptionType) {
    checkNotNull(exceptionType);
    Class<? super T> current = exceptionType;
    do {
      ExceptionHandler<T> binding = (ExceptionHandler<T>) handlers.get(exceptionType);
      if (binding != null)
        return binding;
      current = current.getSuperclass();
    } while (current != null);
    return null;
  }

  @SuppressWarnings("unchecked")
  public <T> ArgumentMapper<T> getMapper(@Nonnull Key<T> key) {
    return (ArgumentMapper<T>) mappers.get(key);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;

    if (o == null || getClass() != o.getClass())
      return false;
    CommandBindings that = (CommandBindings) o;
    return Objects.equals(commandInterceptors, that.commandInterceptors) &&
        Objects.equals(handlers, that.handlers) &&
        Objects.equals(mappers, that.mappers);
  }

  @Override
  public int hashCode() {
    return Objects.hash(commandInterceptors, handlers, mappers);
  }
}

