package com.main.stpaul.services.serviceInterface;

import com.main.stpaul.entities.Session;

public interface SessionService {
    Session addSession(Session session);
    Session getSessionById(long id);
    Session getSessionByToken(String token);
}
