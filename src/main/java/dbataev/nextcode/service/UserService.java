package dbataev.nextcode.service;

import dbataev.nextcode.model.base.Course;
import dbataev.nextcode.model.base.User;
import dbataev.nextcode.model.dto.user.CreateUserDto;
import dbataev.nextcode.model.dto.user.CurrentUserDto;
import dbataev.nextcode.model.dto.user.LoginUserDto;
import dbataev.nextcode.model.dto.user.UpdateUserDto;
import dbataev.nextcode.repository.CourseRepository;
import dbataev.nextcode.repository.UserRepository;
import dbataev.nextcode.security.PasswordHasher;
import dbataev.nextcode.security.jwt.JwtDto;
import dbataev.nextcode.security.jwt.JwtService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Locale;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final CourseRepository courseRepository;

    public UserService(UserRepository userRepository, JwtService jwtService, CourseRepository courseRepository) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.courseRepository = courseRepository;
    }

    public JwtDto registerUser(CreateUserDto user) {
        User newUser = new User();

        newUser.setUsername(user.getUsername());
        newUser.setPasswordHash(PasswordHasher.hashPassword(user.getPassword()));
        newUser.setXp(0);
        newUser.setLevel(1);
        newUser.setStreak(0);
        newUser.setCreatedAt(LocalDateTime.now());
        newUser.setXpForNextLevel(100);
        newUser.setTotalXp(0L);
        newUser.setBestStreak(0L);
        newUser.setCompletedLessons(0);

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
        System.out.println("Запрос на пользователя:");

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));

        return new CurrentUserDto(
                user.getId(),
                user.getUsername(),
                user.getNickname(),
                user.getXp(),
                user.getLevel(),
                user.getStreak(),
                user.getXpForNextLevel(),
                user.getCurrentCourse().getId(),
                user.getTotalXp(),
                user.getBestStreak(),
                user.getCompletedLessons()
        );
    }

    public void setCurrentCourse(Long userId, Long courseId) {
        User user = userRepository.findById(userId).get();
        Course course = courseRepository.findById(courseId).get();

        user.setCurrentCourse(course);

        userRepository.save(user);
    }

    public User updateUser(Long id, UpdateUserDto userDto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));

        if (userDto.getUsername() != null && !userDto.getUsername().isBlank()) {
            String newUsername = userDto.getUsername().trim();

            User userWithSameUsername = userRepository.findByUsername(newUsername);

            if (userWithSameUsername != null && !userWithSameUsername.getId().equals(user.getId())) {
                throw new RuntimeException("Такой username уже занят");
            }

            user.setUsername(newUsername);
        }

        if (userDto.getNickname() != null && !userDto.getNickname().isBlank()) {
            user.setNickname(userDto.getNickname().trim());
        }

        if (userDto.getPassword() != null && !userDto.getPassword().isBlank()) {
            user.setPasswordHash(PasswordHasher.hashPassword(userDto.getPassword()));
        }

        return userRepository.save(user);
    }

    public User checkStreak(User user) {
        LocalDate today = LocalDate.now();
        LocalDate yesterday = today.minusDays(1);
        LocalDate lastActivityDate = user.getLastActivityDate();

        if (lastActivityDate == null) {
            user.setStreak(1);
        } else if (lastActivityDate.isEqual(today)) {
            return user;
        } else if (lastActivityDate.isEqual(yesterday)) {
            user.setStreak(user.getStreak() + 1);
        } else {
            user.setStreak(1);
        }

        if(user.getStreak() > user.getBestStreak()) {
            user.setBestStreak(Long.valueOf(user.getStreak()));
        }

        user.setLastActivityDate(today);

        return user;
    }

    public User checkLevel(User user) {
        int xp = user.getXp() != null ? user.getXp() : 0;
        int level = user.getLevel() != null ? user.getLevel() : 1;

        int xpForNextLevel = getXpRequiredForLevel(level);

        while (xp >= xpForNextLevel) {
            xp -= xpForNextLevel;
            level++;

            xpForNextLevel = getXpRequiredForLevel(level);
        }

        user.setXp(xp);
        user.setLevel(level);
        user.setXpForNextLevel(xpForNextLevel);

        return user;
    }

    private int getXpRequiredForLevel(int level) {
        double baseXp = 100;
        double multiplier = 1.5;

        return (int) Math.round(baseXp * Math.pow(multiplier, level - 1));
    }


}
