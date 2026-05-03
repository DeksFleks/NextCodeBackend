package dbataev.nextcode.model.mapper;

import dbataev.nextcode.model.base.Lesson;
import dbataev.nextcode.model.dto.LessonDto;
import dbataev.nextcode.model.dto.ModuleDto;
import dbataev.nextcode.model.base.Module;
import dbataev.nextcode.model.manyToMany.ModuleLesson;

import java.util.ArrayList;
import java.util.List;
import java.util.Comparator;

public class ModuleMapper {

    public static ModuleDto toDto(Module module) {
        ModuleDto dto = new ModuleDto();

        dto.setId(module.getId());
        dto.setTitle(module.getTitle());
        dto.setOrderIndex(module.getOrderIndex());

        return dto;
    }

    public static ModuleDto toDtoWithLessons(Module module) {
        ModuleDto dto = toDto(module);

        List<ModuleLesson> moduleLessons = new ArrayList<>(module.getModuleLessons());
        moduleLessons.sort(Comparator.comparing(ModuleLesson::getOrderIndex));

        List<LessonDto> lessonDtos = new ArrayList<>();

        for (ModuleLesson moduleLesson : moduleLessons) {
            Lesson lesson = moduleLesson.getLesson();

            LessonDto lessonDto = new LessonDto();
            lessonDto.setId(lesson.getId());
            lessonDto.setTitle(lesson.getTitle());
            lessonDto.setXpReward(lesson.getXpReward());
            lessonDto.setTheoryText(lesson.getTheoryText());

            lessonDtos.add(lessonDto);
        }

        dto.setLessons(lessonDtos);

        return dto;
    }

    public static List<ModuleDto> toDtoListWithLessons(List<Module> modules) {
        List<ModuleDto> result = new ArrayList<>();

        for (Module module : modules) {
            result.add(toDtoWithLessons(module));
        }

        return result;
    }
}