package dbataev.nextcode.service;

import dbataev.nextcode.model.base.User;
import dbataev.nextcode.model.dto.user.CreateUserDto;
import dbataev.nextcode.model.dto.user.CurrentUserDto;
import dbataev.nextcode.model.dto.user.LoginUserDto;
import dbataev.nextcode.repository.UserRepository;
import dbataev.nextcode.security.PasswordHasher;
import dbataev.nextcode.security.jwt.JwtDto;
import dbataev.nextcode.security.jwt.JwtService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final JwtService jwtService;

    public UserService(UserRepository userRepository, JwtService jwtService) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
    }

    public JwtDto registerUser(CreateUserDto user) {
        User newUser = new User();

        newUser.setUsername(user.getUsername());
        newUser.setPasswordHash(PasswordHasher.hashPassword(user.getPassword()));
        newUser.setXp(0);
        newUser.setLevel(1);
        newUser.setStreak(0);
        newUser.setCreatedAt(LocalDateTime.now());

        if(user.getNickname() == null || user.getNickname().isBlank()){
            Long nextNumber = userRepository.getNextDeveloperNicknameNumber();
            newUser.setNickname("Developer#%07d".formatted(nextNumber));
        }
        else {
            newUser.setNickname(user.getNickname());
        }

        userRepository.save(newUser);
        return new JwtDto(jwtService.generateToken(newUser));
    }

    public JwtDto loginUser(LoginUserDto loginUser) {
        User user = userRepository.findByUsername(loginUser.getUsername());

        if (user == null) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "Неверный логин или пароль"
            );
        }

        boolean itsOk = PasswordHasher.checkPassword(
                loginUser.getPassword(),
                user.getPasswordHash()
        );

        if (!itsOk) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "Неверный логин или пароль"
            );
        }

        return new JwtDto(jwtService.generateToken(user));
    }

    public CurrentUserDto getCurrentUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));

        return new CurrentUserDto(
                user.getId(),
                user.getUsername(),
                user.getNickname(),
                user.getXp(),
                user.getLevel(),
                user.getStreak()
        );
    }
}
