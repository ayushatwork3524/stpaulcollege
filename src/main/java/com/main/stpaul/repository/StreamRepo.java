package com.main.stpaul.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import com.main.stpaul.entities.Stream;

public interface StreamRepo extends JpaRepository<Stream,Long>{
    
}
