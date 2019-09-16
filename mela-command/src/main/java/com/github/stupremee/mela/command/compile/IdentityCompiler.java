package com.github.stupremee.mela.command.compile;

import com.github.stupremee.mela.command.CommandCallable;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.Set;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * @author Johnny_JayJay (https://www.github.com/JohnnyJayJay)
 */
public final class IdentityCompiler implements CommandCompiler {

  public static final IdentityCompiler INSTANCE = new IdentityCompiler();

  private IdentityCompiler() {}

  @Nonnull
  @Override
  public Set<? extends CommandCallable> compile(@Nonnull Object command) {
    checkArgument(command instanceof CommandCallable,
        "Could not compile command using IdentityCompiler: Object "
            + command + " is not an instance of CommandCallable");
    return Collections.singleton((CommandCallable) command);
  }

}