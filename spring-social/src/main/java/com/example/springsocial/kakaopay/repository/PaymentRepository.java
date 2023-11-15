package com.example.springsocial.kakaopay.repository;

import com.example.springsocial.kakaopay.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.List;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Optional<Payment> findByTid(String tid);
    Optional<List<Payment>> findByUser_Id(Long userId);
}
