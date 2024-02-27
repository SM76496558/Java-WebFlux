package com.dhiego.webflux.service;

import com.dhiego.webflux.entity.Student;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface IStudentService {

	public Flux<Student> getAllStudents();

	public Mono<Student> getOneById(String id);

	public Mono<Student> addStudent(Student student);

	public Mono<Student> updateStudent(String id, Student student);

	public Mono<Student> asignar(String id, Student student);

	public Mono<Student> deleteStudentById(String id);

	public Flux<Student> getStudentsByAge(int age);

}
