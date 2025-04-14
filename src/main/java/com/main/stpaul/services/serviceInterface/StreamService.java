package com.main.stpaul.services.serviceInterface;

import com.main.stpaul.entities.Stream;

public interface StreamService {
    Stream addStream(Stream stream);
    Stream getStreamById(Long id);
    void deleteStreamById(Long id);

}
