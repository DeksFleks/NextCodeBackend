package dbataev.nextcode.model.mapper;

import dbataev.nextcode.model.base.Course;
import dbataev.nextcode.model.dto.CourseDto;

public class CourseMapper {
    public static CourseDto toDto(Course course) {
        CourseDto dto = new CourseDto();

        dto.setId(course.getId());
        dto.setTitle(course.getTitle());
        dto.setIsActive(course.getIsActive());

        return dto;
    }
}
