package com.dhiego.webflux.repositories;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

import com.dhiego.webflux.entity.Student;

@Repository
public interface StudentRepository extends ReactiveMongoRepository<Student, String> {
	
	
	

}
