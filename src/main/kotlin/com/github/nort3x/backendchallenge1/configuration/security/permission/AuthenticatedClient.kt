package com.github.nort3x.backendchallenge1.configuration.security.permission

import org.springframework.security.access.prepost.PreAuthorize
import java.lang.annotation.Inherited

@Target(AnnotationTarget.FUNCTION, AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
@Inherited
@PreAuthorize("not anonymous")
annotation class AuthenticatedClient
