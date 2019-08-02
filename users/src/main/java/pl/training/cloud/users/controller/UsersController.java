package pl.training.cloud.users.controller;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import pl.training.cloud.users.dto.PageDto;
import pl.training.cloud.users.dto.UserDto;
import pl.training.cloud.users.model.Mapper;
import pl.training.cloud.users.model.ResultPage;
import pl.training.cloud.users.model.User;
import pl.training.cloud.users.service.UsersService;

import java.net.URI;
import java.security.Principal;
import java.util.List;

@RequestMapping(value = "users")
@RestController
@RequiredArgsConstructor
public class UsersController {

    @NonNull
    private UsersService usersService;
    @NonNull
    private Mapper mapper;
    private UriBuilder uriBuilder = new UriBuilder();

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity createUser(@RequestBody UserDto userDto) {
        User user = mapper.map(userDto, User.class);
        usersService.addUser(user);
        URI uri = uriBuilder.requestUriWithId(user.getId());
        return ResponseEntity.created(uri).build();
    }

    @RequestMapping(value = "active", method = RequestMethod.GET)
    public UserDto getActiveUser(Principal principal) {
        UserDetails user = usersService.loadUserByUsername(principal.getName());
        return mapper.map(user, UserDto.class);
    }

    @RequestMapping(method = RequestMethod.GET)
    public PageDto<UserDto> getUsers(
            @RequestParam(required = false, defaultValue = "0", name = "pageNumber") int pageNumber,
            @RequestParam(required = false, defaultValue = "10", name = "pageSize") int pageSize) {
        ResultPage<User> resultPage = usersService.getUsers(pageNumber, pageSize);
        List<UserDto> usersDtos = mapper.map(resultPage.getContent(), UserDto.class);
        return new PageDto<>(usersDtos, resultPage.getPageNumber(), resultPage.getTotalPages());
    }

}
