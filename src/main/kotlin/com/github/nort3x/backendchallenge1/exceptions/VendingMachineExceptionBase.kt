package com.github.nort3x.backendchallenge1.exceptions

import org.springframework.http.HttpStatus


open class VendingMachineExceptionBase(msg: String, val statusCode: HttpStatus, cause: Throwable? = null) :
    Error(msg, cause) {

}