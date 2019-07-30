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

@Primary
@Service
@Log
@RequiredArgsConstructor
public class FeignDepartmentsService implements DepartmentsService {

    @NonNull
    private FeignDepartmentsClient feignDepartmentsClient;

    @Cacheable(value = "departments", unless = "#result == null")
    @Override
    public Optional<String> getDepartmentName(Long departmentId) {
        try {
            log.info("Fetching department...");
            DepartmentDto departmentDto = feignDepartmentsClient.getDepartment(departmentId);
            if (departmentDto != null) {
                return Optional.of(departmentDto.getName());
            }
        } catch (FeignException ex) {
            log.warning("Error fetching department with id: "  + departmentId);
        }
        return Optional.empty();
    }

    @CacheEvict(value = "departments", allEntries = true)
    @StreamListener(Sink.INPUT)
    public void onDepartmentChange(String message) {
        log.info("Cleaning departments cache...");
    }

}
