package dbataev.nextcode.controller;

import dbataev.nextcode.model.dto.CourseDto;
import dbataev.nextcode.service.CourseService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path = "api/courses")
public class CourseController {
    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @GetMapping(path = "/all")
    public List<CourseDto> getAllCourses() {
        return courseService.getAllCourses();
    }

}
