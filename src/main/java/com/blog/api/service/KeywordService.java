package com.blog.api.service;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.blog.api.model.KeywordEntity;
import com.blog.api.repository.KeywordJPARepository;

@Service
public class KeywordService {
    
    private final KeywordJPARepository keywordRepository;

    public KeywordService(KeywordJPARepository keywordRepository) {
        this.keywordRepository = keywordRepository;
    }

    @Transactional
    public KeywordEntity countKeyword(String keyword) {
        Optional<KeywordEntity> result = keywordRepository.findByKeyword(keyword);
        KeywordEntity entity;
        if (result.isEmpty()) {
            entity = new KeywordEntity(keyword);
            keywordRepository.save(entity);
        } else {
            entity = result.get();
            entity.setSearchCount(entity.getSearchCount() + 1);
        }
        return entity;
    }

}
