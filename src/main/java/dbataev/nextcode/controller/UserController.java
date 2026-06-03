package dbataev.nextcode.controller;

import dbataev.nextcode.model.dto.user.CurrentUserDto;
import dbataev.nextcode.model.dto.user.UpdateUserDto;
import dbataev.nextcode.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/me")
    public CurrentUserDto getMe(Authentication authentication) {
        return userService.getCurrentUser((Long) authentication.getPrincipal());
    }

    @PutMapping(path = "/course/{courseId}")
    public void setCurrentCourse(Authentication authentication, @PathVariable Long courseId) {
        userService.setCurrentCourse((Long) authentication.getPrincipal(), courseId);
    }

    @PutMapping(path = "update")
    public void updateCurrentCourse(Authentication authentication, @RequestBody UpdateUserDto userDto) {
        userService.updateUser((Long) authentication.getPrincipal(), userDto);
    }
}
