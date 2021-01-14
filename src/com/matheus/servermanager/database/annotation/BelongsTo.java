package com.matheus.servermanager.database.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.matheus.servermanager.database.model.DatabaseModel;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface BelongsTo {

	Class<? extends DatabaseModel> model();

	String foreignKey();
}
