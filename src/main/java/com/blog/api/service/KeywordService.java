package com.blog.api.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.blog.api.model.KeywordDTO;
import com.blog.api.model.KeywordEntity;
import com.blog.api.repository.KeywordJPARepository;

@Service
public class KeywordService {
    
    private final ModelMapper modelMapper;
    private final KeywordJPARepository keywordRepository;

    public KeywordService(ModelMapper modelMapper, KeywordJPARepository keywordRepository) {
        this.modelMapper = modelMapper;
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

    @Transactional(readOnly = true)
    public List<KeywordDTO> inquireKeyword() {
        List<KeywordEntity> resultList = keywordRepository.findTop10ByOrderBySearchCountDesc();
        return resultList.stream().map( entity -> {
            return modelMapper.map(entity, KeywordDTO.class);
        }).collect(Collectors.toList());
    }

}
