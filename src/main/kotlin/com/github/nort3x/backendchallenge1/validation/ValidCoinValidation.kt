package com.github.nort3x.backendchallenge1.validation

import javax.validation.Constraint
import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext
import javax.validation.Payload
import kotlin.reflect.KClass

@Retention(AnnotationRetention.RUNTIME)
@Target(
    AnnotationTarget.FUNCTION, AnnotationTarget.FIELD,
    AnnotationTarget.PROPERTY,
    AnnotationTarget.VALUE_PARAMETER,
    AnnotationTarget.PROPERTY_GETTER
)
@MustBeDocumented
@Constraint(validatedBy = [ValidCoinValidation::class])

annotation class ValidCoin(
    val message: String = "coin value should be 5,10,20,50 or 100",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<in Payload>> = []
)

class ValidCoinValidation : ConstraintValidator<ValidCoin, Int> {
    override fun isValid(value: Int?, context: ConstraintValidatorContext?): Boolean {
        if (value == null) return false
        return value in arrayOf(5, 10, 20, 50, 100)
    }

}