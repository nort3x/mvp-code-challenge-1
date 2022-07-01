package com.github.nort3x.backendchallenge1.exceptions

import org.springframework.http.HttpStatus

class AlreadyExist(msg: String) : VendingMachineExceptionBase(msg, HttpStatus.CONFLICT) {
}