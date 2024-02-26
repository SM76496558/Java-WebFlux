package com.dhiego.webflux.entity;

import java.io.Serializable;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.*;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "courses")
public class Course implements Serializable {

	@Id
	private String id = new ObjectId().toString();

	private String name;

	private String professorName;

	private String description;

	public Course() {
	}

	public Course(String id, String name, String professorName, String description) {

		this.id = id;
		this.name = name;
		this.professorName = professorName;
		this.description = description;
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

	public String getProfessorName() {
		return professorName;
	}

	public void setProfessorName(String professorName) {
		this.professorName = professorName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public String toString() {
		return "Course [id=" + id + ", name=" + name + ", professorName=" + professorName + ", description="
				+ description + "]";
	}

}
