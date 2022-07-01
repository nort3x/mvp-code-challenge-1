package com.github.nort3x.backendchallenge1.exceptions

import org.springframework.http.HttpStatus

class Forbidden(msg: String, throwable: Throwable? = null) :
    VendingMachineExceptionBase(msg, HttpStatus.FORBIDDEN, throwable) {
}