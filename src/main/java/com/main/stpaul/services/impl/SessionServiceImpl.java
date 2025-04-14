package com.main.stpaul.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.main.stpaul.entities.Session;
import com.main.stpaul.repository.SessionRepo;
import com.main.stpaul.services.serviceInterface.SessionService;

@Service
public class SessionServiceImpl implements SessionService{

    @Autowired
    private SessionRepo sessionRepo;

    @Override
    public Session addSession(Session session) {
        return this.sessionRepo.save(session);
    }

    @Override
    public Session getSessionById(long id) {
        return this.sessionRepo.findBySessionId(id).orElse(new Session());
    }

    @Override
    public Session getSessionByToken(String token) {
        return this.sessionRepo.findByToken(token).orElse(null);
    }
    
}
