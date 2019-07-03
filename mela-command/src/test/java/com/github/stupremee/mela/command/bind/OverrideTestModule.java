package com.github.stupremee.mela.command.bind;

import com.github.stupremee.mela.command.TestAnnotation;
import com.github.stupremee.mela.command.TestException;
import com.github.stupremee.mela.command.TestModule;
import com.github.stupremee.mela.command.handle.ExceptionHandler;
import com.github.stupremee.mela.command.intercept.Interceptor;
import com.github.stupremee.mela.command.map.ArgumentMapper;

/**
 * @author Johnny_JayJay (https://www.github.com/JohnnyJayJay)
 */
final class OverrideTestModule extends TestModule {

  public static final Object ADDITIONAL_COMMAND = new Object();
  public static final ArgumentMapper<Object> MAPPER_OVERRIDE = (o, c) -> null;
  public static final Interceptor<TestAnnotation> INTERCEPTOR_OVERRIDE = (c) -> true;
  public static final ExceptionHandler<TestException> HANDLER_OVERRIDE = (t, c) -> {};

  OverrideTestModule() {
    super(ADDITIONAL_COMMAND);
  }

  @Override
  protected void configureCommandBindings(CommandBinder binder) {
    binder.root()
        .add(ADDITIONAL_COMMAND)
        .group("override")
        .bindParameter(Object.class).toMapper(MAPPER_OVERRIDE)
        .interceptAt(TestAnnotation.class).with(INTERCEPTOR_OVERRIDE)
        .handle(TestException.class).with(HANDLER_OVERRIDE);
  }
}