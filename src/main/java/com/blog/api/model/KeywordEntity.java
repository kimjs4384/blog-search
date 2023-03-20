package com.blog.api.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.ColumnDefault;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "keywords")
public class KeywordEntity {

    public KeywordEntity() {}
    public KeywordEntity(String keyword) {this.keyword = keyword;}
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long seq;
    
    @Column(unique = true, nullable = false)
    @Getter
    private String keyword;

    @Column
    @ColumnDefault("1")
    @Getter
    @Setter
    private int searchCount = 1;
}
