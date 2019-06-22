package com.github.stupremee.mela.command.providers;

import com.google.common.base.Preconditions;
import com.sk89q.intake.argument.CommandArgs;
import com.sk89q.intake.parametric.Provider;
import com.sk89q.intake.parametric.ProvisionException;
import java.lang.annotation.Annotation;
import java.util.Collections;
import java.util.List;

/**
 * @author Stu (https://github.com/Stupremee)
 * @since 13.05.19
 */
public final class NamespaceProvider<T> implements Provider<T> {

  private final Class<T> type;

  private NamespaceProvider(Class<T> type) {
    this.type = type;
  }

  @Override
  public boolean isProvided() {
    return true;
  }

  @Override
  public T get(CommandArgs arguments, List<? extends Annotation> modifiers)
      throws ProvisionException {
    T thing = arguments.getNamespace().get(type);
    if (thing == null) {
      throw new ProvisionException(
          "Could not find object of type " + type.getSimpleName() + " in the namespace.");
    }
    return thing;
  }

  @Override
  public List<String> getSuggestions(String prefix) {
    return Collections.emptyList();
  }

  public static <T> Provider<T> create(Class<T> type) {
    Preconditions.checkNotNull(type, "type can't be null.");
    return new NamespaceProvider<>(type);
  }
}
