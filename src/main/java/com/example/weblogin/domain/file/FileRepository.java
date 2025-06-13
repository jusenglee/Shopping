package com.example.weblogin.domain.file;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.weblogin.domain.item.Item;

@Repository
public interface FileRepository extends JpaRepository<File, Long> {
}

