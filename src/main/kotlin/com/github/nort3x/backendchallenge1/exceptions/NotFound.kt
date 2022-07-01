package com.github.nort3x.backendchallenge1.exceptions

import org.springframework.http.HttpStatus

class NotFound(msg: String, cause: Throwable? = null) : VendingMachineExceptionBase(msg, HttpStatus.NOT_FOUND, cause)