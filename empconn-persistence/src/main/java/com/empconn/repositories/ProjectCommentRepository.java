package com.empconn.repositories;

import org.springframework.data.repository.CrudRepository;

import com.empconn.persistence.entities.ProjectComment;

public interface ProjectCommentRepository extends CrudRepository<ProjectComment, Long> {

}
