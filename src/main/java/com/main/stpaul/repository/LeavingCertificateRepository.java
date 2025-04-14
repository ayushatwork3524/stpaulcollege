package com.main.stpaul.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.main.stpaul.entities.LeavingCertificate;

public interface LeavingCertificateRepository extends JpaRepository<LeavingCertificate, Long> {
    
}
