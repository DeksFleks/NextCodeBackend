package dbataev.nextcode.service;

import dbataev.nextcode.model.base.Lesson;
import dbataev.nextcode.model.base.User;
import dbataev.nextcode.model.manyToMany.UserProgress;
import dbataev.nextcode.repository.LessonRepository;
import dbataev.nextcode.repository.UserProgressRepository;
import dbataev.nextcode.repository.UserRepository;
import dbataev.nextcode.security.jwt.JwtService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class LessonService {
    private final UserProgressRepository userProgressRepository;
    private final UserRepository userRepository;
    private final LessonRepository lessonRepository;
    private final UserService userService;
    private final JwtService jwtService;

    public LessonService(UserProgressRepository userProgressRepository, UserRepository userRepository, LessonRepository lessonRepository, UserService userService, JwtService jwtService) {
        this.userProgressRepository = userProgressRepository;
        this.userRepository = userRepository;
        this.lessonRepository = lessonRepository;
        this.userService = userService;
        this.jwtService = jwtService;
    }


    public void lessonCompleted(Long lessonId, String jwt) {
        System.out.println("Урок пройден");

        User user = userRepository.findById(jwtService.extractUserId(jwt))
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));
        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new RuntimeException("Урок не найден"));
        UserProgress userProgress = userProgressRepository.findByUserIdAndLessonId(user.getId(), lessonId)
                .orElse(null);

        boolean alreadyCompleted = userProgress != null && userProgress.getIsCompleted();

        if (userProgress == null) {
            userProgress = new UserProgress();
            userProgress.setUser(user);
            userProgress.setLesson(lesson);
        }

        Integer reward = lesson.getXpReward();

        if (alreadyCompleted) {
            reward = reward / 5;
        }

        user.setTotalXp(user.getTotalXp() + reward);
        user.setXp(user.getXp() + reward);
        user = userService.checkStreak(user);
        user = userService.checkLevel(user);

        userProgress.setIsCompleted(true);

        userRepository.save(user);
        userProgressRepository.save(userProgress);
    }
}
