package com.github.nort3x.backendchallenge1.configuration

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.web.servlet.invoke
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.util.matcher.AntPathRequestMatcher
import org.springframework.security.web.util.matcher.NegatedRequestMatcher
import org.springframework.security.web.util.matcher.OrRequestMatcher


@Configuration
@EnableWebSecurity
class VendingMachineSecurityConfig {

    companion object {
        val PUBLIC_PATHS = OrRequestMatcher(
            AntPathRequestMatcher("/v1/user","POST"),
            AntPathRequestMatcher("/swagger"),
            AntPathRequestMatcher("/swagger-ui/**"),
            AntPathRequestMatcher("/v3/api-docs/**"),
            AntPathRequestMatcher("/login"),
        )
        val AUTHENTICATED_PATHS = NegatedRequestMatcher(PUBLIC_PATHS)
    }

    @Bean
    fun passwordHashEncoder(): PasswordEncoder = BCryptPasswordEncoder()


    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {


        http.authorizeRequests()
        http.invoke {
            authorizeRequests {
                authorize(PUBLIC_PATHS, permitAll)
                authorize(AUTHENTICATED_PATHS, authenticated)
            }

            formLogin {
                this.loginProcessingUrl = "/login"
            }
            csrf {
                disable()
            }
            logout {
                logoutUrl = "/logout"
            }
        }
        return http.build()
    }
}