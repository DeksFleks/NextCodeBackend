package dbataev.nextcode.controller;

import dbataev.nextcode.service.LessonService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "api/lesson")
public class LessonController {
    private final LessonService lessonService;

    public LessonController(LessonService lessonService) {
        this.lessonService = lessonService;
    }

    @PostMapping("{lessonId}/completed")
    public void lessonCompleted(@PathVariable Long lessonId, @RequestHeader("Authorization") String authHeader) {
        String jwt = authHeader.replace("Bearer ", "");

        lessonService.lessonCompleted(lessonId, jwt);
    }
}
