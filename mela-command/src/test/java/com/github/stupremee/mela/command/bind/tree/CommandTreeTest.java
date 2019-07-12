package com.github.stupremee.mela.command.bind.tree;

import com.github.stupremee.mela.command.CommandGroup;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author Johnny_JayJay (https://www.github.com/JohnnyJayJay)
 */
public final class CommandTreeTest {

  @Inject
  private CommandTree tree;

  @BeforeEach
  public void setUp() {
    Injector injector = Guice.createInjector(new CommandTreeTestModule());
    injector.injectMembers(tree);
  }

  @Test
  public void testStep() {
    tree.stepToRoot();
    assertTrue(tree.isAtRoot(), "Tree did not step to root");
    CommandGroup root = tree.getCurrent();
    CommandGroup child = root.getChildren().iterator().next();
    tree.stepDown(child);
    assertEquals(child, tree.getCurrent(), "Tree did not step down to specified child");
    tree.stepUp();
    assertEquals(root, tree.getCurrent(), "Tree did not step up");
    assertThrows(IllegalStateException.class, tree::stepUp,
        "Tree did not throw when stepping up from root");
  }

}