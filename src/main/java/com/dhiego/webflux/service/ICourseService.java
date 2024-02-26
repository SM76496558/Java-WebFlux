package com.dhiego.webflux.service;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dhiego.webflux.entity.Course;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;



public interface ICourseService {

	public Flux<Course> getAllCourses();

	public Mono<Course> getOneById(String id);

	public Mono<Course> addCourse(Course course);

	public Mono<Course> updateCourse(String id, Course course);

	public Mono<Course> deleteCourseById(String id);
}
