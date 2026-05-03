package dbataev.nextcode.controller;

import dbataev.nextcode.model.dto.user.CreateUserDto;
import dbataev.nextcode.model.dto.user.LoginUserDto;
import dbataev.nextcode.security.jwt.JwtDto;
import dbataev.nextcode.service.UserService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "api/auth")
public class AuthController {
    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(path = "/register")
    private JwtDto register(@Valid @RequestBody CreateUserDto user) {
        return userService.registerUser(user);
    }

    @PostMapping(path = "/login")
    private JwtDto login(@RequestBody LoginUserDto user) {
        return userService.loginUser(user);
    }
}
