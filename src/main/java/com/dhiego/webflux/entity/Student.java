package com.dhiego.webflux.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.*;
import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.validation.constraints.NotNull;

@Document(collection = "students")
public class Student implements Serializable {

	@Id
	@NotNull
	private String id = new ObjectId().toString();

	private String name;
	private int age;
	private List<Course> course = new ArrayList<>();

	public Student() {
	}

	public Student(@NotNull String id, String name, int age, List<Course> course) {

		this.id = id;
		this.name = name;
		this.age = age;
		this.course = course;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public List<Course> getCourse() {
		return course;
	}

	public void setCourse(List<Course> course) {
		this.course = course;
	}

	public void agregarCurso(Course courseB) {
		course.add(courseB);
	}

	@Override
	public String toString() {
		return "Student [id=" + id + ", name=" + name + ", age=" + age + ", course=" + course + "]";
	}

}
