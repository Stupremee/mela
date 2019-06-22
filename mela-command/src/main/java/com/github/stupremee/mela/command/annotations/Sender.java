package com.github.stupremee.mela.command.annotations;

import com.sk89q.intake.parametric.annotation.Classifier;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Stu (https://github.com/Stupremee)
 * @since 13.05.19
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Classifier
public @interface Sender {

}
