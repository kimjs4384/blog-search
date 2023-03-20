package com.blog.api.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.blog.api.model.KeywordEntity;

public interface KeywordJPARepository extends JpaRepository<KeywordEntity, Long> {

    Optional<KeywordEntity> findByKeyword(String keyword);

    List<KeywordEntity> findTop10ByOrderBySearchCountDesc();
    
}
