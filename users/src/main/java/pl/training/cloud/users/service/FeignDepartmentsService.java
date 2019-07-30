package pl.training.cloud.users.service;

import feign.FeignException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import pl.training.cloud.users.dto.DepartmentDto;

import java.util.Optional;

/*@DefaultProperties(
        commandProperties = {
                @HystrixProperty(name = "metrics.rollingStats.timeInMilliseconds", value = "10000"),
                @HystrixProperty(name = "metrics.rollingStats.numBuckets", value = "10")
        }
)*/
@Primary
@Service
@Log
@RequiredArgsConstructor
public class FeignDepartmentsService implements DepartmentsService {

    @NonNull
    private FeignDepartmentsClient feignDepartmentsClient;

    /*@HystrixCommand(
        threadPoolKey = "departments",
        threadPoolProperties = {
                @HystrixProperty(name = "coreSize", value = "10"),
                @HystrixProperty(name = "maxQueueSize", value = "15")
        },
        commandProperties = {
                @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "20"),
                @HystrixProperty(name = "circuitBreaker.errorThresholdPercentage", value = "50"),
                @HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds", value = "5000")
        }
    )*/
    @Cacheable(value = "departments", unless = "#result == null")
    @Override
    public Optional<String> getDepartmentName(Long departmentId) {
        try {
            DepartmentDto departmentDto = feignDepartmentsClient.getDepartment(departmentId);
            if (departmentDto != null) {
                log.info("Fetching department...");
                return Optional.of(departmentDto.getName());
            }
        } catch (FeignException ex) {
            log.warning("Error fetching department with id:  + departmentId");
        }
        return Optional.empty();
    }

    @CacheEvict(value = "departments", allEntries = true)
    @StreamListener(Sink.INPUT)
    public void onDepartmentsChange(String message) {
        log.info("Cleaning departments cache...");
    }

}
