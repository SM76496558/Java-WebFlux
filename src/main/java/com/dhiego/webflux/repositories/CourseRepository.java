package com.dhiego.webflux.repositories;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

import com.dhiego.webflux.entity.Course;

@Repository
public interface CourseRepository extends ReactiveMongoRepository<Course, String> {

}
