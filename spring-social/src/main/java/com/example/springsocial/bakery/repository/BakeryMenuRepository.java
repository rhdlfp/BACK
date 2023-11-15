package com.example.springsocial.bakery.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.springsocial.bakery.model.BakeryMenu;

@Repository
public interface BakeryMenuRepository extends JpaRepository<BakeryMenu, Long> {
    List<BakeryMenu> findByBakeryId(Long bakeryId);
}
