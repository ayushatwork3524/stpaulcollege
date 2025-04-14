package com.main.stpaul.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.main.stpaul.entities.Session;


public interface SessionRepo extends JpaRepository<Session,Long>{
    Optional<Session> findBySessionId(Long sessionId);
    Optional<Session> findByToken(String token);;
}
