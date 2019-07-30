package pl.training.cloud.departments.service;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.training.cloud.departments.model.Department;
import pl.training.cloud.departments.repository.DepartmentsRepository;

@RequiredArgsConstructor
@Service
public class DepartmentsService {

    @NonNull
    private DepartmentsRepository departmentsRepository;

    @NotifyDepartmentChange
    public Department addDepartment(Department department) {
        departmentsRepository.saveAndFlush(department);
        return department;
    }

    public Department getDepartmentById(Long id) {
        return departmentsRepository.getById(id)
                .orElseThrow(DepartmentNotFoundException::new);
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
