package com.github.nort3x.backendchallenge1.validation

import javax.validation.Constraint
import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext
import javax.validation.Payload
import kotlin.properties.Delegates
import kotlin.reflect.KClass

@Retention(AnnotationRetention.RUNTIME)
@Target(
    AnnotationTarget.FUNCTION, AnnotationTarget.FIELD,
    AnnotationTarget.PROPERTY,
    AnnotationTarget.VALUE_PARAMETER,
    AnnotationTarget.PROPERTY_GETTER
)
@MustBeDocumented
@Constraint(validatedBy = [MultipleOfValidation::class])
annotation class MultipleOf(val value: Int,
                            val message: String = "value should be multiply of given value",
                            val groups: Array<KClass<*>> = [],
                            val payload: Array<KClass<in Payload>> = []
)

class MultipleOfValidation : ConstraintValidator<MultipleOf, Int> {
    var modulus by Delegates.notNull<Int>()
    override fun initialize(constraintAnnotation: MultipleOf) {
        super.initialize(constraintAnnotation)
        modulus = constraintAnnotation.value
    }

    override fun isValid(value: Int?, context: ConstraintValidatorContext): Boolean =
        if (value == null) false
        else value % modulus == 0
}