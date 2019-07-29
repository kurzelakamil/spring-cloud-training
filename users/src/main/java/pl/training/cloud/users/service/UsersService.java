package pl.training.cloud.users.service;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import pl.training.cloud.users.model.ResultPage;
import pl.training.cloud.users.model.User;
import pl.training.cloud.users.repository.UsersRepository;

@RequiredArgsConstructor
@Service
public class UsersService {

    @NonNull
    private UsersRepository usersRepository;
    @NonNull
    private DepartmentsService departmentsService;
    @Value("${defaultDepartmentId}")
    @Setter
    private Long defaultDepartmentId;

    public void addUser(User user) {
        if (user.getDepartmentId() == null) {
            user.setDepartmentId(defaultDepartmentId);
        }
        usersRepository.saveAndFlush(user);
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

}
