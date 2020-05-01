package be.ugent.webdevelopment.backend.geocode.utils

import org.springframework.boot.actuate.trace.http.InMemoryHttpTraceRepository
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.EnableAsync

@Configuration
@EnableAsync
class TraceConfig : InMemoryHttpTraceRepository()