package com.github.nort3x.backendchallenge1.utils

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity


fun Any.logger(): Logger {
    return LoggerFactory.getLogger(this.javaClass)
}

fun <T : Any> T.asOk(): ResponseEntity<T> =
    ResponseEntity.ok(this)