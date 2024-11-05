package dev.sstol.tasktrackerrestapi.infrastructure.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Component;

/**
 * @author Sergey Stol
 * 2024-11-05
 */
@Component
public class CustomMetrics {

   private final Counter customCounter;

   public CustomMetrics(MeterRegistry meterRegistry) {
      this.customCounter = Counter.builder("custom_counter_total")
        .description("Total number of custom events")
        .register(meterRegistry);
   }

   public void incrementCounter() {
      customCounter.increment();
   }
}
