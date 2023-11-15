package com.example.springsocial.comunity.heart.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.springsocial.comunity.Comunity;
import com.example.springsocial.comunity.heart.Heart;
import com.example.springsocial.model.User;

@Repository
public interface HeartRepository extends JpaRepository<Heart, Long> {
    List<Heart> findAllByComunity(Comunity comunity);
    Optional<Heart> findByComunityAndUser(Comunity comunity, User user);
}

