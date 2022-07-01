package com.github.nort3x.backendchallenge1.exceptions

import org.springframework.http.HttpStatus

class AlreadyExist(msg: String, cause: Throwable? = null) :
    VendingMachineExceptionBase(msg, HttpStatus.CONFLICT, cause)