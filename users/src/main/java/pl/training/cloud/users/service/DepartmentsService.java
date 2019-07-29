package pl.training.cloud.users.service;

import java.util.Optional;

public interface DepartmentsService {

    Optional<String> getDepartmentName(Long departmentId);

}
