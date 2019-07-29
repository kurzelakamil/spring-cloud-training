package pl.training.cloud.users.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class UserDto {

    private String firstName;
    private String lastName;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String departmentName;

}
