package com.example.intat3.repositories;
import jakarta.persistence.EntityManager;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;

public class CustomUserRepositoryImpl <T, ID extends Serializable> extends SimpleJpaRepository<T, ID> implements CustomUserRepository<T, ID>{
    private final EntityManager entityManager;

    public CustomUserRepositoryImpl(JpaEntityInformation jpaEntityInformation, EntityManager entityManager){
        super(jpaEntityInformation, entityManager);
        this.entityManager = entityManager;
    }

    @Override
    @Transactional
    public void refresh(T t){
        entityManager.refresh(t);
    }
    
}
