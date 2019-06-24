package com.github.stupremee.mela.command.internal;

import com.github.stupremee.mela.command.ExceptionHandler;
import com.github.stupremee.mela.command.Interceptor;
import com.github.stupremee.mela.command.binding.ExceptionBindings;
import com.github.stupremee.mela.command.binding.InterceptorBindings;
import com.github.stupremee.mela.command.binding.ParameterBindings;
import com.github.stupremee.mela.command.compile.CommandTree;
import com.github.stupremee.mela.command.exception.ConflictException;
import com.github.stupremee.mela.command.inject.InjectionObjectHolder;
import com.github.stupremee.mela.command.mapping.ArgumentMapper;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.inject.Inject;
import com.google.inject.Key;

import javax.annotation.Nonnull;
import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import static com.google.common.base.Preconditions.*;

/**
 * @author Johnny_JayJay (https://www.github.com/JohnnyJayJay)
 */
final class RecursiveCommandTree implements CommandTree {

  private final UnboundGroup root;
  private UnboundGroup currentNode;

  RecursiveCommandTree() {
    this(new UnboundGroup(new MapBasedParameterBindings(),
        new MapBasedInterceptorBindings(), new MapBasedExceptionBindings(),
        Collections.emptySet(), null));
  }

  private RecursiveCommandTree(UnboundGroup root) {
    this.root = root;
    this.currentNode = root;
  }

  @Nonnull
  @Override
  public CommandTree merge(@Nonnull CommandTree o) {
    checkArgument(checkNotNull(o) instanceof RecursiveCommandTree,
        "Must be instance of RecursiveCommandTree to merge");
    RecursiveCommandTree other = (RecursiveCommandTree) o;
    RecursiveCommandTree mergingTree = new RecursiveCommandTree();
    mergingTree.mergeMutating(this, other);
    return mergingTree;
  }

  private void mergeMutating(RecursiveCommandTree one, RecursiveCommandTree two) {
    this.stepToRoot();
    one.stepToRoot();
    this.mergeMutating(one);
    one.stepToRoot();
    this.stepToRoot();
    two.stepToRoot();
    this.mergeMutating(two);
    two.stepToRoot();
    this.stepToRoot();
  }

  private void mergeMutating(RecursiveCommandTree tree) {
    currentNode.interceptorBindings.putAll(tree.currentNode.interceptorBindings);
    currentNode.parameterBindings.putAll(tree.currentNode.parameterBindings);
    currentNode.exceptionBindings.putAll(tree.currentNode.exceptionBindings);
    currentNode.commands.putAll(tree.currentNode.commands);
    for (UnboundGroup child : tree.currentNode.children) {
      tree.stepDown(child);
      try {
        this.stepDownOrCreate(child.aliases);
      } catch (IllegalArgumentException e) {
        throw new ConflictException("Two groups from two different CommandBinders that " +
            "are on the same layer have the same alias", e);
      }
      mergeMutating(tree);
    }
  }

  @Inject
  public void inject(InjectionObjectHolder holder) {
    this.stepToRoot();
    this.recursiveInject(holder);
  }

  private void recursiveInject(InjectionObjectHolder holder) {
    for (Object command : holder.getCommandObjects())
      currentNode.commands.computeIfPresent(command.getClass(), (k, v) -> command);
    currentNode.parameterBindings.inject(holder.getMappers());
    currentNode.interceptorBindings.inject(holder.getInterceptors());
    currentNode.exceptionBindings.inject(holder.getHandlers());
    for (UnboundGroup child : currentNode.children) {
      this.stepDown(child);
      recursiveInject(holder);
    }
    currentNode.makeImmutable();
  }

  void stepDownOrCreate(Set<String> childAliases) {
    UnboundGroup child = createChild(childAliases);
    checkNodeForDuplicateChildAliases(child);
    boolean exists = !currentNode.children.add(child);
    this.currentNode = !exists ? child : getExistingChildReference(child);
  }

  private UnboundGroup createChild(Set<String> aliases) {
    return new UnboundGroup(currentNode.parameterBindings.copy(),
        currentNode.interceptorBindings.copy(), currentNode.exceptionBindings.copy(), aliases, currentNode);
  }

  private void checkNodeForDuplicateChildAliases(UnboundGroup child) {
    for (UnboundGroup element : currentNode.children) {
      if (!element.equals(child))
        continue;
      for (String alias : element.aliases) {
        checkArgument(!child.aliases.contains(alias),
            "Duplicate alias for different groups in the same layer: " + alias);
      }
    }
  }

  private UnboundGroup getExistingChildReference(UnboundGroup child) {
    for (UnboundGroup element : currentNode.children) {
      if (element.equals(child))
        return element;
    }
    return null;
  }

  @Nonnull
  @Override
  public Group getCurrent() {
    return currentNode;
  }

  @Override
  public void stepDown(@Nonnull Group child) {
    checkArgument(checkNotNull(child) instanceof UnboundGroup, "Child must be instance of the same implementation as the tree");
    checkArgument(currentNode.children.contains(child), "Provided group is not a children of the current currentNode");
    currentNode = (UnboundGroup) child;
  }

  @Override
  public void stepUp() {
    checkState(!isAtRoot(), "Current currentNode is root currentNode, can't step up");
    currentNode = currentNode.parent;
  }

  @Override
  public void stepToRoot() {
    while (!isAtRoot())
      stepUp();
  }

  @Override
  public boolean isAtRoot() {
    return currentNode == root;
  }

  void addCommand(Class<?> commandClass) {
    currentNode.commands.put(commandClass, null);
  }

  <T> void addParameterBinding(Key<T> key,
                               Class<? extends ArgumentMapper<T>> mapperType) {
    currentNode.parameterBindings.put(key, mapperType);
  }

  <T extends Annotation> void addInterceptorBinding(Class<T> annotationType,
                                                    Class<? extends Interceptor<T>> interceptorType) {
    currentNode.interceptorBindings.put(annotationType, interceptorType);
  }

  <T extends Throwable> void addExceptionBinding(Class<T> exceptionType,
                                                 Class<? extends ExceptionHandler<T>> handlerType,
                                                 boolean ignoreInheritance) {
    currentNode.exceptionBindings.put(exceptionType, handlerType, ignoreInheritance);
  }

  private static final class UnboundGroup implements CommandTree.Group {

    final UnboundGroup parent;
    final MapBasedParameterBindings parameterBindings;
    final MapBasedInterceptorBindings interceptorBindings;
    final MapBasedExceptionBindings exceptionBindings;
    final Set<String> aliases;
    final Set<UnboundGroup> children;

    Map<Class<?>, Object> commands;
    Set<Group> finalChildren;


    UnboundGroup(MapBasedParameterBindings parameterBindings,
                 MapBasedInterceptorBindings interceptorBindings,
                 MapBasedExceptionBindings exceptionBindings, Set<String> aliases,
                 UnboundGroup parent) {
      this.parameterBindings = parameterBindings;
      this.interceptorBindings = interceptorBindings;
      this.exceptionBindings = exceptionBindings;
      this.commands = Maps.newHashMap();
      this.aliases = aliases;
      this.parent = parent;
      this.children = Sets.newHashSet();
    }

    @Override
    public boolean equals(Object o) {
      if (this == o)
        return true;
      if (o == null || getClass() != o.getClass())
        return false;
      UnboundGroup that = (UnboundGroup) o;
      return Objects.equals(parameterBindings, that.parameterBindings) &&
          Objects.equals(interceptorBindings, that.interceptorBindings) &&
          Objects.equals(exceptionBindings, that.exceptionBindings) &&
          Objects.equals(aliases, that.aliases) &&
          Objects.equals(commands, that.commands) &&
          Objects.equals(children, that.children) &&
          Objects.equals(parent, that.parent);
    }

    @Override
    public int hashCode() {
      return Objects.hash(parameterBindings, interceptorBindings, exceptionBindings,
          aliases, commands, children, parent);
    }

    @Override
    public CommandTree.Group getParent() {
      return parent;
    }

    @Override
    public String primaryAlias() {
      return aliases.isEmpty() ? null : aliases.iterator().next();
    }

    @Nonnull
    @Override
    public Set<CommandTree.Group> getChildren() {
      return finalChildren;
    }

    @Nonnull
    @Override
    public Set<String> getAliases() {
      return aliases;
    }

    @Nonnull
    @Override
    public ParameterBindings getParameterBindings() {
      return parameterBindings;
    }

    @Nonnull
    @Override
    public InterceptorBindings getInterceptorBindings() {
      return interceptorBindings;
    }

    @Nonnull
    @Override
    public ExceptionBindings getExceptionBindings() {
      return exceptionBindings;
    }

    @Nonnull
    @Override
    public Collection<?> getCommandObjects() {
      return commands.values();
    }

    void makeImmutable() {
      finalChildren = Set.copyOf(children);
      commands = Map.copyOf(commands);
    }
  }
}
