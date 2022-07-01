package com.github.nort3x.backendchallenge1.exceptions

import org.springframework.http.HttpStatus

class UnAuthorized: VendingMachineExceptionBase("authentication required",HttpStatus.UNAUTHORIZED) {
}