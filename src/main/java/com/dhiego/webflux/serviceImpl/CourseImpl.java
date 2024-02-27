package com.dhiego.webflux.serviceImpl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dhiego.webflux.entity.Course;
import com.dhiego.webflux.entity.Student;
import com.dhiego.webflux.repositories.CourseRepository;
import com.dhiego.webflux.repositories.StudentRepository;
import com.dhiego.webflux.service.ICourseService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class CourseImpl implements ICourseService {

	@Autowired
	private CourseRepository repository;

	@Autowired
	private StudentRepository repository2;

	@Override
	public Flux<Course> getAllCourses() {
		return repository.findAll();
	}

	@Override
	public Mono<Course> getOneById(String id) {
		return repository.findById(id);
	}

	@Override
	public Mono<Course> addCourse(Course course) {
		return repository.save(course);
	}

	@Override
	public Mono<Course> updateCourse(String id, Course course) {
		return repository.findById(id).flatMap(c -> {

			c.setName(course.getName());
			c.setProfessorName(course.getProfessorName());
			c.setDescription(course.getDescription());
			return repository.save(c);

		});
	}

	@Override
	public Mono<Course> deleteCourseById(String id) {

		return repository.findById(id).flatMap(c -> {
			return repository.deleteById(id).then(Mono.just(c));
		});
	}

	@Override
	public Flux<Map<String, Object>> getAllCoursesWithStudents() {

		return repository.findAll().flatMap(c -> {
			Map<String, Object> result = new HashMap<>();
			result.put("Course", c.getName());
			return repository2.findAll().filter(student -> {
				List<Course> studentCourseList = student.getCourse();
				for (Course course : studentCourseList) {

					if (course.getId().equals(c.getId())) {
						return true;
					}

				}
				return false;

			}).collectList().map(students -> {
				result.put("List of Student", students);
				return result;
			});
		});
	}

}
