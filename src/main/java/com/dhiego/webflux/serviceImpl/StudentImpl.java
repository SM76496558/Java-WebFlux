package com.dhiego.webflux.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dhiego.webflux.entity.Student;
import com.dhiego.webflux.repositories.StudentRepository;
import com.dhiego.webflux.service.IStudentService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class StudentImpl implements IStudentService {

	@Autowired
	private StudentRepository repository;

	@Override
	public Flux<Student> getAllStudents() {
		return repository.findAll();
	}

	@Override
	public Mono<Student> getOneById(String id) {
		return repository.findById(id);
	}

	@Override
	public Mono<Student> addStudent(Student student) {
		return repository.save(student);
	}

	@Override
	public Mono<Student> updateStudent(String id, Student student) {
		return repository.findById(id).flatMap(s -> {

			s.setName(student.getName());
			s.setAge(student.getAge());
			s.setCourse(student.getCourse());

			return repository.save(s);
		});
	}

	@Override
	public Mono<Student> deleteStudentById(String id) {
		return repository.findById(id).flatMap(s -> repository.deleteById(id).then(Mono.just(s)));
	}

	@Override
	public Flux<Student> getStudentsByAge(int age) {
		return repository.findAll().filter(s -> {

			if (s.getAge() == age) {
				return true;
			}
			return false;
		});
	}

}
