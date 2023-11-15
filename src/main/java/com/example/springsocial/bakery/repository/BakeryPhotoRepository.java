package com.example.springsocial.bakery.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.springsocial.bakery.model.BakeryPhoto;

@Repository
public interface BakeryPhotoRepository extends JpaRepository<BakeryPhoto, Long> {
    List<BakeryPhoto> findByBakeryId(Long bakeryId);
}
