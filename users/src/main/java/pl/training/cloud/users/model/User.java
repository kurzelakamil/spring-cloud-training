package pl.training.cloud.users.model;

import lombok.Data;

import javax.persistence.*;

@Table(name = "client")
@Entity
@Data
public class User {

    @GeneratedValue
    @Id
    private Long id;
    private String firstName;
    private String lastName;
    private Long departmentId;
    @Transient
    private String departmentName;

}