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
    private final TaskService tasks;
    private final UserService userService;
    private final JwtService jwtService;
    private final AchievementService achievementService;

    public LessonService(UserProgressRepository userProgressRepository, UserRepository userRepository, LessonRepository lessonRepository, UserService userService, JwtService jwtService, AchievementService achievementService, TaskService tasks) {
        this.tasks = tasks;
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

        User user = userRepository.findForUpdate(jwtService.extractUserId(jwt))
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));
        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new RuntimeException("Урок не найден"));
        UserProgress userProgress = userProgressRepository.findByUserIdAndLessonId(user.getId(), lessonId)
                .orElse(null);

        boolean alreadyCompleted = userProgress != null && Boolean.TRUE.equals(userProgress.getIsCompleted());

        if (alreadyCompleted) return List.of();
        tasks.requirePassed(lessonId, user.getId());

        if (userProgress == null) {
            userProgress = new UserProgress();
            userProgress.setUser(user);
            userProgress.setLesson(lesson);
        }

        Integer reward = lesson.getXpReward();



        user.setTotalXp(user.getTotalXp() + reward);
        user.setXp(user.getXp() + reward);
        user.setCompletedLessons(user.getCompletedLessons() + 1);
        user = userService.checkStreak(user);
        user = userService.checkLevel(user);

        userProgress.setIsCompleted(true);
        userProgress.setCompletedAt(java.time.LocalDateTime.now());


        achievementDtos.addAll(achievementService.checkAllAchievements(user));


        userRepository.save(user);


        userProgressRepository.save(userProgress);

        return achievementDtos;
    }
}
