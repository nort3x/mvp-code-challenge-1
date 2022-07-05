package com.github.nort3x.backendchallenge1.exceptions

import org.springframework.http.HttpStatus

class Insufficient(msg: String, ex: Throwable? = null): VendingMachineExceptionBase(msg, HttpStatus.NOT_ACCEPTABLE,ex) {
}