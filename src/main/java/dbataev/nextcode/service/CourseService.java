package dbataev.nextcode.service;

import dbataev.nextcode.model.base.Course;
import dbataev.nextcode.model.dto.CourseDto;
import dbataev.nextcode.model.mapper.CourseMapper;
import dbataev.nextcode.repository.CourseRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CourseService {
    private final CourseRepository courseRepository;

    public CourseService(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    public List<CourseDto> getAllCourses() {
        List<Course> courses = courseRepository.findAll();
        List<CourseDto> courseDtos = new ArrayList<>();

        for(Course course : courses) {
            courseDtos.add(CourseMapper.toDto(course));
        }

        return courseDtos;
    }
}
