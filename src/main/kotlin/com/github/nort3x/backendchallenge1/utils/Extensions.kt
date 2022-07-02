package com.github.nort3x.backendchallenge1.utils

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity


fun Any.logger(): Logger {
    return LoggerFactory.getLogger(this.javaClass)
}

fun <T : Any> T.asResponseEntity(status: HttpStatus = HttpStatus.OK): ResponseEntity<T> =
    ResponseEntity.status(status).body(this)


fun <T> ifCan(condition: Boolean, func: () -> T): T? {
    return if (condition) {
        func()
    } else null
}

infix fun <T> T?.otherwiseThrow(t: Throwable): T {
    if (this == null)
        throw t
    return this
}