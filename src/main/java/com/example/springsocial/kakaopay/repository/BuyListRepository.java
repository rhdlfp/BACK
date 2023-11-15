package com.example.springsocial.kakaopay.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.springsocial.kakaopay.entity.BuyList;

@Repository
public interface BuyListRepository extends JpaRepository<BuyList, Long> {
	Optional<List<BuyList>> findByPaymentId(Long paymentId);
}

