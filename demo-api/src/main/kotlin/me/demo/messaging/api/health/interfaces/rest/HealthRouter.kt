package me.demo.messaging.api.health.interfaces.rest

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.bodyValueAndAwait
import org.springframework.web.reactive.function.server.coRouter

@Configuration
class HealthRouter {
    @Bean
    fun healthRoute() = coRouter {
        GET("/health") { ServerResponse.ok().bodyValueAndAwait(mapOf("isAlive" to true)) }
    }
}