package com.main.stpaul.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.main.stpaul.entities.TransferCertificate;

public interface TransferCertificateRepository extends JpaRepository<TransferCertificate, Long> {

}
