package com.demo.elasticsearch

import javax.validation.Constraint
import javax.validation.Payload
import kotlin.annotation.AnnotationRetention.RUNTIME
import kotlin.reflect.KClass

@MustBeDocumented
@Retention(RUNTIME)
@Target(AnnotationTarget.FIELD, AnnotationTarget.ANNOTATION_CLASS, AnnotationTarget.VALUE_PARAMETER)
@Constraint(validatedBy = [PublicationYearValidator::class])
annotation class PublicationYear(
    val message: String = "Publication year cannot be future year",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = []
)