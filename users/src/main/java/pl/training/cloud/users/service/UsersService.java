package pl.training.cloud.users.service;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.training.cloud.users.config.DepartmentsConfig;
import pl.training.cloud.users.model.ResultPage;
import pl.training.cloud.users.model.Role;
import pl.training.cloud.users.model.User;
import pl.training.cloud.users.repository.RolesRepository;
import pl.training.cloud.users.repository.UsersRepository;

import java.util.Set;

@RequiredArgsConstructor
@Service
public class UsersService implements UserDetailsService {

    @NonNull
    private UsersRepository usersRepository;
    @NonNull
    private DepartmentsService departmentsService;
    @NonNull
    private DepartmentsConfig departmentsConfig;
    @NonNull
    private PasswordEncoder passwordEncoder;
    @NonNull
    private RolesRepository rolesRepository;
    @Value("${default-role.name}")
    @Setter
    private String defaultRoleName;

    public void addUser(User user) {
        Role role = getDefaultRole();
        user.setRoles(Set.of(role));
        if (user.getDepartmentId() == null) {
            user.setDepartmentId(departmentsConfig.getDefaultDepartmentId());
        }
        encodePassword(user);
        user.setActive(true);
        usersRepository.saveAndFlush(user);
    }

    private void encodePassword(User user) {
        String password = user.getPassword();
        user.setPassword(passwordEncoder.encode(password));
    }

    private Role getDefaultRole() {
        return rolesRepository.getByName(defaultRoleName)
                .orElseGet(this::createDefaultRole);
    }

    private Role createDefaultRole() {
        Role role = new Role(defaultRoleName);
        return rolesRepository.saveAndFlush(role);
    }

    public ResultPage<User> getUsers(int pageNumber, int pageSize) {
        Page<User> usersPage = usersRepository.findAll(PageRequest.of(pageNumber, pageSize));
        fetchDepartments(usersPage);
        return new ResultPage<>(usersPage.getContent(), usersPage.getNumber(), usersPage.getTotalPages());
    }

    private void fetchDepartments(Page<User> usersPage) {
        usersPage.getContent().forEach(user -> departmentsService.getDepartmentName(user.getDepartmentId())
                .ifPresent(user::setDepartmentName));
    }

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        return usersRepository.getByLogin(login)
                .orElseThrow(() -> new UsernameNotFoundException(String.format("User %s not found", login)));
    }

}
