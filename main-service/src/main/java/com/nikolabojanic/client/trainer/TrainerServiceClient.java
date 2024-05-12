package com.nikolabojanic.client.trainer;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(value = "trainer-service")
@CircuitBreaker(name = "trainer-service")
public interface TrainerServiceClient {
    @PostMapping("/api/v1/workload")
    void workloadUpdate(
        @RequestBody TrainerWorkloadRequestDto trainer,
        @RequestHeader("traceId") String traceId,
        @RequestHeader("Authorization") String jwt);

}
