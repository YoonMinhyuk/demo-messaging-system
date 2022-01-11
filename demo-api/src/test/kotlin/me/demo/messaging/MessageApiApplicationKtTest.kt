package me.demo.messaging

import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest


@SpringBootTest
internal class MessageApiApplicationKtTest {
    @Test
    @DisplayName("헬로우")
    internal fun test() {
        println("HelloWorld")
    }
}