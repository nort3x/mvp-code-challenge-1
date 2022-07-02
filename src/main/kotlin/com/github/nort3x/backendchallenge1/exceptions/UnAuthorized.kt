package com.github.nort3x.backendchallenge1.exceptions

import org.springframework.http.HttpStatus

class UnAuthorized(msg: String = "unauthorized access") : VendingMachineExceptionBase(msg, HttpStatus.UNAUTHORIZED) {
}