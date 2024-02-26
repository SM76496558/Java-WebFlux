package com.dhiego.webflux.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dhiego.webflux.entity.Student;
import com.dhiego.webflux.service.ICourseService;
import com.dhiego.webflux.service.IStudentService;
import com.dhiego.webflux.serviceImpl.StudentImpl;

import jakarta.validation.Valid;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@SuppressWarnings("deprecation")
@RestController
@RequestMapping("/students")
public class StudentController {

	@Autowired
	private IStudentService service;

	@Autowired

	private ICourseService serviceCourse;

	@GetMapping
	public Mono<ResponseEntity<Flux<Student>>> getAllStudents() {

		return Mono
				.just(ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON_UTF8).body(service.getAllStudents()))
				.defaultIfEmpty(ResponseEntity.noContent().build());
	}

	@GetMapping("/{id}")
	public Mono<ResponseEntity<Student>> getOneById(@PathVariable String id) {
		if (id != null && !id.isEmpty()) {
			return service.getOneById(id)
					.map(s -> ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON_UTF8).body(s));
		}
		return Mono.just(ResponseEntity.notFound().build());

	}

	@PostMapping("/add")
	public Mono<ResponseEntity<Student>> addStudent(@RequestBody Student student) {

		return service.addStudent(student)
				.map(s -> ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON_UTF8).body(s))
				.defaultIfEmpty(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
	}

	@PutMapping("/update/{id}")
	public Mono<ResponseEntity<Student>> updateStudent(@PathVariable String id, @RequestBody Student student) {

		return service.updateStudent(id, student)
				.map(s -> ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON_UTF8).body(s))
				.defaultIfEmpty(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());

	}

	@DeleteMapping("/delete/{id}")
	public Mono<ResponseEntity<Student>> deleteStudentById(@PathVariable String id) {
		return service.deleteStudentById(id).map(s -> {
			return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON_UTF8).body(s);
		}).defaultIfEmpty(ResponseEntity.notFound().build());
	}

	@PostMapping("/{idStudent}/asignar/{idCourse}")
	public Mono<ResponseEntity<Map<String, Object>>> asignar(@PathVariable String idStudent,
			@PathVariable String idCourse) {

		Map<String, Object> response = new HashMap<String, Object>();

		return serviceCourse.getOneById(idCourse).flatMap(cFound -> {

			return service.getOneById(idStudent).flatMap(sFound -> {
				response.put("message", "RequestSuccess");
				response.put("Course", cFound);
				response.put("Student", sFound);
				sFound.agregarCurso(cFound);
				return service.updateStudent(idStudent, sFound).map(s -> {

					return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON_UTF8).body(response);
				});
			}).defaultIfEmpty(ResponseEntity.notFound().build());

		}).defaultIfEmpty(ResponseEntity.notFound().build());

	}

	@GetMapping("/edad/{age}")
	public Mono<ResponseEntity<Map<String, Object>>> getStudentsByAge(@PathVariable int age) {

		Map<String, Object> response = new HashMap<String, Object>();

		Flux<Student> students = service.getStudentsByAge(age);

		return students.collectList().flatMap(s -> {
			response.put("message", "Request Success");
			response.put("Age", age);
			response.put("Students", s);
			return Mono.just(ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON_UTF8).body(response));
		}).defaultIfEmpty(ResponseEntity.noContent().build());

	}

}
