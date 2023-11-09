package com.anything.gradproject.repository;

import com.anything.gradproject.entity.FileEntity;
import com.anything.gradproject.entity.Lectures;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileRepository extends JpaRepository<Lectures, Long> {

}
