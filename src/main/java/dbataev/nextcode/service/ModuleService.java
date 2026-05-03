package dbataev.nextcode.service;

import dbataev.nextcode.LessonState;
import dbataev.nextcode.model.base.Lesson;
import dbataev.nextcode.model.base.Module;
import dbataev.nextcode.model.base.Task;
import dbataev.nextcode.model.dto.LessonDto;
import dbataev.nextcode.model.dto.ModuleDto;
import dbataev.nextcode.model.dto.TaskDto;
import dbataev.nextcode.model.manyToMany.ModuleLesson;
import dbataev.nextcode.model.mapper.LessonMapper;
import dbataev.nextcode.model.mapper.ModuleMapper;
import dbataev.nextcode.repository.ModuleRepository;
import dbataev.nextcode.repository.UserProgressRepository;
import dbataev.nextcode.security.jwt.JwtService;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ModuleService {
    private final ModuleRepository moduleRepository;
    private final UserProgressRepository userProgressRepository;
    private final JwtService jwtService;

    public ModuleService(ModuleRepository moduleRepository, UserProgressRepository userProgressRepository, JwtService jwtService) {
        this.moduleRepository = moduleRepository;
        this.userProgressRepository = userProgressRepository;
        this.jwtService = jwtService;
    }

    public List<ModuleDto> findModulesInCourse(Long courseId) {
        List<ModuleDto> moduleDtos = new ArrayList<>();

        List<Module> modules = moduleRepository.findByCourseIdAndIsDeletedFalseOrderByOrderIndexAsc(courseId);

        for (Module module : modules) {
            moduleDtos.add(ModuleMapper.toDto(module));
        }

        return moduleDtos;
    }

    public List<ModuleDto> getModulesByCourseId(Long courseId, String jwt) {
        List<Module> modules = moduleRepository.findModulesWithLessonsByCourseId(courseId);
        modules.sort(Comparator.comparing(Module::getOrderIndex));

        Long userId = jwtService.extractUserId(jwt);
        List<ModuleDto> result = new ArrayList<>();
        Set<Long> completedLessonIds = new HashSet<>(userProgressRepository.findCompletedLessonIdsByUserIdAndCourseId(userId, courseId));
        boolean currentAssigned = false;

        for (Module module : modules) {
            ModuleDto moduleDto = new ModuleDto();

            moduleDto.setId(module.getId());
            moduleDto.setTitle(module.getTitle());
            moduleDto.setOrderIndex(module.getOrderIndex());

            List<LessonDto> lessonDtos = new ArrayList<>();

            List<ModuleLesson> moduleLessons = new ArrayList<>(module.getModuleLessons());
            moduleLessons.sort(Comparator.comparing(ModuleLesson::getOrderIndex));


            for (ModuleLesson moduleLesson : moduleLessons) {
                Lesson lesson = moduleLesson.getLesson();

                LessonState state;

                if (completedLessonIds.contains(lesson.getId())) {
                    state = LessonState.COMPLETED;
                } else if (!currentAssigned) {
                    state = LessonState.CURRENT;
                    currentAssigned = true;
                } else {
                    state = LessonState.BLOCKED;
                }

                LessonDto lessonDto = new LessonDto();
                lessonDto.setId(lesson.getId());
                lessonDto.setTitle(lesson.getTitle());
                lessonDto.setXpReward(lesson.getXpReward());
                lessonDto.setTheoryText(lesson.getTheoryText());
                lessonDto.setState(state);

                lessonDtos.add(lessonDto);
            }

            moduleDto.setLessons(lessonDtos);
            result.add(moduleDto);
        }

        return result;
    }


}