package me.demo.messaging

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import java.time.ZoneId
import java.util.*
import javax.annotation.PostConstruct

@SpringBootApplication
class MessageApiApplication {
    @PostConstruct
    fun setUp() {
        TimeZone.setDefault(TimeZone.getTimeZone(ZoneId.of("UTC")))
    }
}

fun main(args: Array<String>) {
    runApplication<MessageApiApplication>(*args)
}