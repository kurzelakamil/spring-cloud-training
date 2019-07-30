package pl.training.cloud.users.service;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import pl.training.cloud.users.dto.DepartmentDto;

import java.util.List;
import java.util.Optional;

@Log
@Service
@RequiredArgsConstructor
public class SimpleDepartmentsService implements DepartmentsService {

    private static final String DEPARTMENTS_MICROSERVICE_NAME = "departments";
    private static final String RESOURCE_PATH = "/departments";

    @NonNull
    private DiscoveryClient discoveryClient;
    @NonNull
    private RestTemplate restTemplate;

    @Cacheable(value = "departments", unless = "#result == null")
    @Override
    public Optional<String> getDepartmentName(Long departmentId) {
        Optional<String> resourceUri = getResourceUri(departmentId);
        if (resourceUri.isPresent()) {
            return getDepartmentName(resourceUri.get());
        } else {
            return Optional.empty();
        }
    }

    private Optional<String> getResourceUri(Long departmentId) {
        List<ServiceInstance> instances = discoveryClient.getInstances(DEPARTMENTS_MICROSERVICE_NAME);
        if (instances.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(instances.get(0).getUri().toString() + RESOURCE_PATH + "/" + departmentId);
    }

    private Optional<String> getDepartmentName(String resourceUri) {
        try {
            log.info("Fetching department...");
            DepartmentDto departmentDto = restTemplate.getForObject(resourceUri, DepartmentDto.class);
            if (departmentDto != null) {
                return Optional.of(departmentDto.getName());
            }
        } catch (HttpClientErrorException ex) {
            log.warning("Error fetching department: " + resourceUri);
        }
        return Optional.empty();
    }

}
