package com.example.springsocial.bakery.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.springsocial.bakery.model.Bakery;

@Repository
public interface BakeryRepository extends JpaRepository<Bakery, Long> {
}
