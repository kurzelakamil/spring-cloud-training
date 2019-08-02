package pl.training.cloud.users.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.training.cloud.users.model.Role;

import java.util.Optional;

public interface RolesRepository extends JpaRepository<Role, Long> {

    Optional<Role> getByName(String name);

}