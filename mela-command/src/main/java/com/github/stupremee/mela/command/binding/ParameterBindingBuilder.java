package com.github.stupremee.mela.command.binding;

import com.github.stupremee.mela.command.mapping.ArgumentMapper;

import java.lang.annotation.Annotation;

public interface ParameterBindingBuilder<T> {

  ParameterBindingBuilder<T> annotatedWith(Class<? extends Annotation> annotationType);

  CommandBindingNode toInstance(T instance);

  CommandBindingNode toMapper(ArgumentMapper<T> provider);

  CommandBindingNode toMapper(Class<? extends ArgumentMapper<T>> clazz);

}