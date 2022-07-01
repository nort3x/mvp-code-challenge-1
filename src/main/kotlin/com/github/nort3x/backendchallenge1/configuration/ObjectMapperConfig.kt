package com.github.nort3x.backendchallenge1.configuration

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import java.time.Instant

private class Instant2EpochSerializer : JsonSerializer<Instant>() {
    override fun serialize(value: Instant?, gen: JsonGenerator, serializers: SerializerProvider) {
        if (value == null)
            gen.writeNull()
        else
            gen.writeNumber(value.toEpochMilli())
    }


}

@Configuration
class ObjectMapperConfig {
    @Bean
    @Primary
    fun objectMapper(): ObjectMapper =
        with(ObjectMapper()) {

            findAndRegisterModules()

            val javaTimeModule = JavaTimeModule()
            javaTimeModule.addSerializer(Instant::class.java, Instant2EpochSerializer())
            registerModule(javaTimeModule)
        }
}