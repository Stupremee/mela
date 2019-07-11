package com.github.stupremee.mela.command.bind.internal;

import com.github.stupremee.mela.command.intercept.Interceptor;
import com.github.stupremee.mela.command.bind.CommandBindingNode;
import com.github.stupremee.mela.command.bind.InterceptorBindingBuilder;
import com.google.inject.multibindings.Multibinder;

import javax.annotation.Nonnull;
import java.lang.annotation.Annotation;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * @author Johnny_JayJay (https://www.github.com/JohnnyJayJay)
 */
final class InternalInterceptorBindingBuilder<T extends Annotation> implements InterceptorBindingBuilder<T> {

  private final InternalCommandBindingNode node;
  private final CompilableGroup group;
  private final Multibinder<Interceptor<?>> binder;
  private final Class<T> annotationType;

  InternalInterceptorBindingBuilder(InternalCommandBindingNode node, CompilableGroup group,
                                    Multibinder<Interceptor<?>> binder, Class<T> annotationType) {
    this.node = node;
    this.group = group;
    this.binder = binder;
    this.annotationType = annotationType;
  }

  @Nonnull
  @Override
  public CommandBindingNode with(@Nonnull Class<? extends Interceptor<T>> clazz) {
    checkNotNull(clazz);
    group.addInterceptorBinding(annotationType, clazz);
    binder.addBinding().to(clazz);
    return node;
  }

  @Nonnull
  @SuppressWarnings("unchecked")
  @Override
  public CommandBindingNode with(@Nonnull Interceptor<T> interceptor) {
    checkNotNull(interceptor);
    group.addInterceptorBinding(annotationType, (Class<? extends Interceptor<T>>) interceptor.getClass());
    binder.addBinding().toInstance(interceptor);
    return node;
  }
}
