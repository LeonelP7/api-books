package com.example.database.services;

import com.example.database.domain.entities.AuthorEntity;

import java.util.List;
import java.util.Optional;

public interface AuthorService {

     AuthorEntity save(AuthorEntity authorEntity);

    List<AuthorEntity> findAll();

    Optional<AuthorEntity> findById(Long id);

    boolean existsById(Long id);

    AuthorEntity partialUpdate(Long id, AuthorEntity authorEntity);

    void deleteById(Long id);
}
