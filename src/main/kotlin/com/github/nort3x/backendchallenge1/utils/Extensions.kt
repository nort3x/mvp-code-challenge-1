package com.github.nort3x.backendchallenge1.utils

import com.github.nort3x.backendchallenge1.configuration.security.Vetoer
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity


fun Any?.logger(): Logger {
    if (this != null) {
        return LoggerFactory.getLogger(this.javaClass)
    } else return LoggerFactory.getLogger("null-logger")
}

fun <T : Any> T.asResponseEntity(status: HttpStatus = HttpStatus.OK): ResponseEntity<T> =
    ResponseEntity.status(status).body(this)


/**
 * auxiliary method responsible for checking [Vetoer] and invoke respective function
 * @param func callable function after consideration of [Vetoer]
 */
fun <T> withConsiderationOf(condition: Vetoer, func: () -> T): T {
    return if (!condition.isDenied)
        func()
    else throw condition.reason
}

fun Any?.fireAndForget(useFallBackMessage: Boolean = false, fallbackMessage: String? = null, func: () -> Unit) {
    try {
        func()
    } catch (ex: Throwable) {
        if (useFallBackMessage)
            fallbackMessage?.let {
                logger().info(it)
            }
        else
            logger().info("fire and forget invoking thrown exception", ex)
    }
}