package org.example.opentelementry.config;

import io.opentelemetry.context.propagation.TextMapPropagator;
import io.opentelemetry.exporter.otlp.trace.OtlpGrpcSpanExporter;
import io.opentelemetry.extension.trace.propagation.JaegerPropagator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OtelConfiguration {
    @Bean
    public TextMapPropagator jaegerPropagator() {
        return JaegerPropagator.getInstance();
    }

    /**
     * Export traces to Jaeger using GRPC.
     * Only needed if you want to use GRPC over HTTP on port 4318.
     * Default is OtlpHttpSpanExporter
     * See: <a href="https://github.com/spring-projects/spring-boot/blob/main/spring-boot-project/spring-boot-actuator-autoconfigure/src/main/java/org/springframework/boot/actuate/autoconfigure/tracing/otlp/OtlpAutoConfiguration.java">AutoConfig Implementation Reference</a>
     */
    @Bean
    public OtlpGrpcSpanExporter otlpExporter() {
        return OtlpGrpcSpanExporter.getDefault();
    }


}
