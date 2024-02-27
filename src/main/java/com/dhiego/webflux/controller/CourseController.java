package com.dhiego.webflux.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.dhiego.webflux.entity.Course;
import com.dhiego.webflux.entity.Student;
import com.dhiego.webflux.service.ICourseService;
import com.dhiego.webflux.service.IStudentService;

import jakarta.validation.Valid;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/courses")
public class CourseController {

	@Autowired
	private ICourseService service;

	@Autowired
	private IStudentService serviceStudent;

	@GetMapping
	public Mono<ResponseEntity<Flux<Course>>> getAllCourses() {
		return Mono.just(ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON_UTF8).body(service.getAllCourses()))
				.defaultIfEmpty(ResponseEntity.noContent().build());
	}

	@GetMapping("/withStudents")
	public Mono<ResponseEntity<Flux<Map<String, Object>>>> getAllCoursesWithStudents() {

		return Mono.just(ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON_UTF8)
				.body(service.getAllCoursesWithStudents()));

	}

	@GetMapping("/{id}")
	public Mono<ResponseEntity<Map<String, Object>>> getOneById(@PathVariable String id) {
		Map<String, Object> response = new HashMap<String, Object>();

		Flux<Student> filterStudents = serviceStudent.getAllStudents().filter(student -> {

			List<Course> arrayCourse = student.getCourse();

			for (int i = 0; i < arrayCourse.size(); i++) {

				String studentIdCourse = arrayCourse.get(i).getId();
				if (studentIdCourse.equals(id)) {
					return true;
				}

			}
			return false;
		});

		return filterStudents.collectList().flatMap(students -> {

			response.put("message", "Request success");

			response.put("Students in Course", students);
			return Mono.just(ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON_UTF8).body(response));

		}).defaultIfEmpty(ResponseEntity.noContent().build());

	}

	@PostMapping("/add")
	public Mono<ResponseEntity<Course>> addCourse(@RequestBody @Valid Course course) {
		return service.addCourse(course).map(c -> {
			return ResponseEntity.created(null).contentType(MediaType.APPLICATION_JSON_UTF8).body(c);
		}).defaultIfEmpty(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
	}

	@PutMapping("/update/{id}")
	public Mono<ResponseEntity<Course>> updateCourse(@PathVariable String id, @RequestBody Course course) {
		return service.updateCourse(id, course).map(c -> {
			return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON_UTF8).body(c);
		}).defaultIfEmpty(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());

	}

	@DeleteMapping("/delete/{id}")
	public Mono<ResponseEntity<Map<String, Object>>> deleteCourseById(@PathVariable String id) {

		Map<String, Object> response = new HashMap<String, Object>();
		Mono<Course> courseMono = service.deleteCourseById(id);

		return courseMono.map(c -> {

			response.put("message", "Deleted success!");
			response.put("Course Deleted", c);
			return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON_UTF8).body(response);

		}).defaultIfEmpty(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());

	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Map<String, String>> handleValidationBadRequest(MethodArgumentNotValidException exception) {
		Map<String, String> errors = new HashMap<>();

		exception.getBindingResult().getAllErrors().forEach((error) -> {
			String fieldName = ((FieldError) error).getField();
			String errorMessage = error.getDefaultMessage();
			errors.put(fieldName, errorMessage);
		});
		return ResponseEntity.badRequest().body(errors);
	}

}
