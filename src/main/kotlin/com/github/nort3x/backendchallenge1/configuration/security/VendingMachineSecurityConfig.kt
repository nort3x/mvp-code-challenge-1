package com.github.nort3x.backendchallenge1.configuration.security

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.web.servlet.invoke
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.access.AccessDeniedHandler
import org.springframework.security.web.util.matcher.AntPathRequestMatcher
import org.springframework.security.web.util.matcher.NegatedRequestMatcher
import org.springframework.security.web.util.matcher.OrRequestMatcher


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
class VendingMachineSecurityConfig {

    /**
     *  according to spring-docs component based security-configuration is the standard way in favor of
     *  [WebSecurityConfigurerAdapter]
     */

    // security config - api level
    private companion object {
        val PUBLIC_CONTROLLER_END_POINTS = OrRequestMatcher(
            AntPathRequestMatcher("/v1/user", "POST"),
            AntPathRequestMatcher("/login"),

            AntPathRequestMatcher("/**", "GET"),
            AntPathRequestMatcher("/**", "POST"),
            AntPathRequestMatcher("/**", "PUT"),
            AntPathRequestMatcher("/**", "DELETE"),
        )
        val PUBLIC_DEV_PATHS = OrRequestMatcher(
            AntPathRequestMatcher("/swagger"),
            AntPathRequestMatcher("/swagger-ui/**"),
            AntPathRequestMatcher("/v3/api-docs/**"),
        )

        val PUBLIC_PATHS = OrRequestMatcher(PUBLIC_CONTROLLER_END_POINTS, PUBLIC_DEV_PATHS)

        val AUTHENTICATED_PATHS = NegatedRequestMatcher(PUBLIC_PATHS)
    }

    @Bean
    fun passwordHashEncoder(): PasswordEncoder = BCryptPasswordEncoder()


    @Bean
    fun filterChain(
        http: HttpSecurity,
        accessDeniedHandler: AccessDeniedHandler,
        authenticationEntryPoint: AuthenticationEntryPoint
    ): SecurityFilterChain {


        http.authorizeRequests()
        http.invoke {
            authorizeRequests {
                authorize(PUBLIC_PATHS, permitAll)
                authorize(AUTHENTICATED_PATHS, authenticated)
            }
            exceptionHandling {
                this.authenticationEntryPoint = authenticationEntryPoint
                this.accessDeniedHandler = accessDeniedHandler
            }
            formLogin {
                loginProcessingUrl = loginPage
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