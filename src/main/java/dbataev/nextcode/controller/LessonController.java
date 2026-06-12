package dbataev.nextcode.controller;

import dbataev.nextcode.model.dto.AchievementDto;
import dbataev.nextcode.service.LessonService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "api/lesson")
public class LessonController {
    private final LessonService lessonService;

    public LessonController(LessonService lessonService) {
        this.lessonService = lessonService;
    }

    @PostMapping("{lessonId}/completed")
    public List<AchievementDto> lessonCompleted(@PathVariable Long lessonId, @RequestHeader("Authorization") String authHeader) {
        String jwt = authHeader.replace("Bearer ", "");

        return lessonService.lessonCompleted(lessonId, jwt);
    }
}
