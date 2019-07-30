package pl.training.cloud.departments.service;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.training.cloud.departments.model.Department;
import pl.training.cloud.departments.repository.DepartmentsRepository;

import java.util.Random;

@RequiredArgsConstructor
@Service
public class DepartmentsService {

    private static final String DEFAULT_DEPARTMENT_NAME = "UNKNOWN";

    @NonNull
    private DepartmentsRepository departmentsRepository;

    @NotifyDepartmentChange
    public Department addDepartment(Department department) {
        departmentsRepository.saveAndFlush(department);
        return department;
    }

    @HystrixCommand(fallbackMethod = "getDepartmentByIdFallback",
            ignoreExceptions = DepartmentNotFoundException.class,
            commandProperties = @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "2000")
    )
    public Department getDepartmentById(Long id) {
        fakeDelay();
        return departmentsRepository.getById(id)
                .orElseThrow(DepartmentNotFoundException::new);
    }

    public Department getDepartmentByIdFallback(Long id) {
        return new Department(DEFAULT_DEPARTMENT_NAME);
    }

    private void fakeDelay() {
        Random random = new Random();
        if (random.nextBoolean()) {
            try {
                Thread.sleep(12_0000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @NotifyDepartmentChange
    public void updateDepartment(Department department) {
        getDepartmentById(department.getId());
        departmentsRepository.save(department);
    }

    @NotifyDepartmentChange
    public void deleteDepartmentWithId(Long id) {
        departmentsRepository.deleteById(id);
    }

}
