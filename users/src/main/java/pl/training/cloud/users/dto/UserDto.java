package pl.training.cloud.users.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import pl.training.cloud.users.model.Role;

import java.util.Set;

@Data
public class UserDto {

    private String login;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String departmentName;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Set<Role> roles;
    private String firstName;
    private String lastName;

}
