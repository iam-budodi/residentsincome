package com.japhet.application.residentsincome.util;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.inject.Qualifier;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import static java.lang.annotation.ElementType.ANNOTATION_TYPE;

@Qualifier
@Target({ TYPE, METHOD, PARAMETER, FIELD, ANNOTATION_TYPE })
@Retention(RUNTIME)
@Documented
public @interface Faces {

}
