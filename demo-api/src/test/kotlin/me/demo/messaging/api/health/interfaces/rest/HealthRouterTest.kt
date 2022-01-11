package me.demo.messaging.api.health.interfaces.rest

import me.demo.messaging.AbstractRouterTest
import org.junit.jupiter.api.Test

internal class HealthRouterTest : AbstractRouterTest() {

    @Test
    internal fun test() {
        webTestClient.get()
            .uri("/health")
            .exchange()
            .expectStatus().isOk
            .expectBody()
            .jsonPath("$.isAlive").exists()
            .jsonPath("$.isAlive").isEqualTo(true)
    }
}