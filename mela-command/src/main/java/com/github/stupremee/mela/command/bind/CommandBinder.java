package com.github.stupremee.mela.command.bind;

import com.github.stupremee.mela.command.bind.internal.InternalCommandBinder;
import com.google.inject.Binder;

import javax.annotation.Nonnull;

import static com.google.common.base.Preconditions.checkNotNull;

public interface CommandBinder {

  @Nonnull
  CommandBindingNode root();

  @Nonnull
  static CommandBinder create(@Nonnull Binder binder) {
    return new InternalCommandBinder(binder);
  }
}
