package me.demo.messaging.api.appconfig

import org.springframework.context.MessageSource
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.support.ResourceBundleMessageSource
import org.springframework.web.reactive.config.WebFluxConfigurer
import org.springframework.web.server.i18n.AcceptHeaderLocaleContextResolver
import java.nio.charset.StandardCharsets
import java.util.*

@Configuration
class WebFluxAppConfig : WebFluxConfigurer {
    @Bean
    fun messageSource(): MessageSource = ResourceBundleMessageSource().apply {
        setBasename("i18n/message")
        setCacheMillis(5)
        setDefaultEncoding(StandardCharsets.UTF_8.displayName())
        setDefaultLocale(Locale.KOREA)
    }

    @Bean
    fun localeContextResolver(): AcceptHeaderLocaleContextResolver =
        AcceptHeaderLocaleContextResolver().apply { defaultLocale = Locale.KOREA }
}