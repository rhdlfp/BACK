package com.example.springsocial.bakery.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.springsocial.bakery.dto.BakeryDetails;
import com.example.springsocial.bakery.model.Bakery;
import com.example.springsocial.bakery.model.BakeryMenu;
import com.example.springsocial.bakery.model.BakeryPhoto;
import com.example.springsocial.bakery.repository.BakeryMenuRepository;
import com.example.springsocial.bakery.repository.BakeryPhotoRepository;
import com.example.springsocial.bakery.repository.BakeryRepository;

@RestController
@RequestMapping("/bakeries")
public class BakeryController {
    private final BakeryRepository bakeryRepository;
    private final BakeryPhotoRepository bakeryPhotoRepository;
    private final BakeryMenuRepository bakeryMenuRepository;

    public BakeryController(BakeryRepository bakeryRepository, 
                            BakeryPhotoRepository bakeryPhotoRepository, 
                            BakeryMenuRepository bakeryMenuRepository) {
        this.bakeryRepository = bakeryRepository;
        this.bakeryPhotoRepository = bakeryPhotoRepository;
        this.bakeryMenuRepository = bakeryMenuRepository;
    }
    
    @GetMapping("/all")
    public ResponseEntity<List<BakeryDetails>> getAllBakeries() {
        List<Bakery> allBakeries = bakeryRepository.findAll();
        List<BakeryDetails> bakeryDetailsList = new ArrayList<>();

        for (Bakery bakery : allBakeries) {
            List<BakeryPhoto> bakeryPhotos = bakeryPhotoRepository.findByBakeryId(bakery.getId());
            List<BakeryMenu> bakeryMenus = bakeryMenuRepository.findByBakeryId(bakery.getId());

            BakeryDetails bakeryDetails = new BakeryDetails(bakery, bakeryPhotos, bakeryMenus);
            bakeryDetailsList.add(bakeryDetails);
        }

        return ResponseEntity.ok(bakeryDetailsList);
    }


    @GetMapping("/{id}")
    public ResponseEntity<BakeryDetails> getBakeryDetails(@PathVariable Long id) {
        Optional<Bakery> bakeryOptional = bakeryRepository.findById(id);

        if (bakeryOptional.isPresent()) {
            Bakery bakery = bakeryOptional.get();
            List<BakeryPhoto> bakeryPhotos = bakeryPhotoRepository.findByBakeryId(id);
            List<BakeryMenu> bakeryMenus = bakeryMenuRepository.findByBakeryId(id);

            BakeryDetails bakeryDetails = new BakeryDetails(bakery, bakeryPhotos, bakeryMenus);

            return ResponseEntity.ok(bakeryDetails);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
