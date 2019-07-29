package pl.training.cloud.users.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.training.cloud.users.dto.DepartmentDto;

@FeignClient("departments")
public interface FeignDepartmentsClient {

    @RequestMapping("departments/{department-id}")
    DepartmentDto getDepartment(@PathVariable("department-id") Long departmentId);

}
