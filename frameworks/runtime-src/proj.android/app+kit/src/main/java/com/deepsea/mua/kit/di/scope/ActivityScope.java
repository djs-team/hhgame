package com.deepsea.mua.kit.di.scope;

import java.lang.annotation.Retention;

import javax.inject.Scope;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Created by JUN on 2019/3/24
 */
@Scope
@Retention(RUNTIME)
public @interface ActivityScope {
}
