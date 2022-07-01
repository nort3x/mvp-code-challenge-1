package com.github.nort3x.backendchallenge1.utils

import org.slf4j.Logger
import org.slf4j.LoggerFactory


fun Any.logger(): Logger {
    return LoggerFactory.getLogger(this.javaClass)
}