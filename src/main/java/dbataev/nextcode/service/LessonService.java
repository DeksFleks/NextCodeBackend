package dbataev.nextcode.service;

import dbataev.nextcode.model.base.Lesson;
import dbataev.nextcode.model.base.User;
import dbataev.nextcode.model.dto.AchievementDto;
import dbataev.nextcode.model.manyToMany.UserProgress;
import dbataev.nextcode.repository.LessonRepository;
import dbataev.nextcode.repository.UserProgressRepository;
import dbataev.nextcode.repository.UserRepository;
import dbataev.nextcode.security.jwt.JwtService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class LessonService {
    private final UserProgressRepository userProgressRepository;
    private final UserRepository userRepository;
    private final LessonRepository lessonRepository;
    private final UserService userService;
    private final JwtService jwtService;
    private final AchievementService achievementService;

    public LessonService(UserProgressRepository userProgressRepository, UserRepository userRepository, LessonRepository lessonRepository, UserService userService, JwtService jwtService, AchievementService achievementService) {
        this.userProgressRepository = userProgressRepository;
        this.userRepository = userRepository;
        this.lessonRepository = lessonRepository;
        this.userService = userService;
        this.jwtService = jwtService;
        this.achievementService = achievementService;
    }

    @Transactional
    public List<AchievementDto> lessonCompleted(Long lessonId, String jwt) {
        List<AchievementDto> achievementDtos = new ArrayList<>();

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

        System.out.println("Точка 0: " + user.getCompletedLessons());

        user.setTotalXp(user.getTotalXp() + reward);
        user.setXp(user.getXp() + reward);
        user.setCompletedLessons(user.getCompletedLessons() + 1);
        user = userService.checkStreak(user);
        user = userService.checkLevel(user);

        userProgress.setIsCompleted(true);

        System.out.println("Точка 1: " + user.getCompletedLessons());

        achievementDtos.addAll(achievementService.checkAllAchievements(user));

        System.out.println("Точка 2: " + user.getCompletedLessons());

        userRepository.save(user);

        System.out.println("Точка 3: " + user.getCompletedLessons());

        userProgressRepository.save(userProgress);

        return achievementDtos;
    }
}
