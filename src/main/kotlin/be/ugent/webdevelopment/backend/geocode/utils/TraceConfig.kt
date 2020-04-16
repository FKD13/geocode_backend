package be.ugent.webdevelopment.backend.geocode.utils

import org.springframework.boot.actuate.trace.http.InMemoryHttpTraceRepository
import org.springframework.context.annotation.Configuration

@Configuration
class TraceConfig : InMemoryHttpTraceRepository()