package pl.training.cloud.users.service;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import pl.training.cloud.users.dto.DepartmentDto;

import java.util.Optional;

@Primary
@Log
@Service
@RequiredArgsConstructor
public class BalancedDepartmentsService implements DepartmentsService {

    private static final String RESOURCE_URI = "http://departments-service/departments/";

    @NonNull
    private RestTemplate restTemplate;

    @Cacheable(value = "departments", unless = "#result == null")
    @Override
    public Optional<String> getDepartmentName(Long departmentId) {
        try {
            log.info("Fetching department...");
            DepartmentDto departmentDto = restTemplate.getForObject(RESOURCE_URI + departmentId, DepartmentDto.class);
            if (departmentDto != null) {
                return Optional.of(departmentDto.getName());
            }
        } catch (HttpClientErrorException ex) {
            log.warning("Error fetching department: " + departmentId);
        }
        return Optional.empty();
    }

}
