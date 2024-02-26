package com.dhiego.webflux.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dhiego.webflux.entity.Course;
import com.dhiego.webflux.repositories.CourseRepository;
import com.dhiego.webflux.service.ICourseService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class CourseImpl implements ICourseService {

	@Autowired
	private CourseRepository repository;

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
			return repository.save(course);

		});
	}

	@Override
	public Mono<Course> deleteCourseById(String id) {

		return repository.findById(id).flatMap(c -> {
			return repository.deleteById(id).then(Mono.just(c));
		});
	}

}
