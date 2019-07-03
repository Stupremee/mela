package com.github.stupremee.mela.command.bind.internal;

import com.github.stupremee.mela.command.bind.CommandBinder;
import com.github.stupremee.mela.command.bind.CommandBindingNode;
import com.google.inject.Binder;

/**
 * @author Johnny_JayJay (https://www.github.com/JohnnyJayJay)
 */
public final class InternalCommandBinder implements CommandBinder {

  private final CommandBindingNode root;

  public InternalCommandBinder(Binder binder) {
    this.root = new InternalCommandBindingNode(new CommandMultibinder(binder));
  }

  @Override
  public CommandBindingNode root() {
    return root;
  }

}