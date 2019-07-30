package pl.training.cloud.users.config;

import lombok.Getter;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

@RefreshScope
@Component
@Log
public class DepartmentsConfig {

    @Getter
    private Long defaultDepartmentId;

    @Value("${defaultDepartmentId}")
    public void setDefaultDepartmentId(Long defaultDepartmentId) {
        log.info("Updating default department id: " + defaultDepartmentId);
        this.defaultDepartmentId = defaultDepartmentId;
    }

}
