package com.example.repository;

import com.example.entity.Watch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WatchRepository extends JpaRepository<Watch, Integer> {

    List<Watch> findByBrand(String brand);

    List<Watch> findByIsActiveTrue();

    List<Watch> findByModelContainingIgnoreCaseOrBrandContainingIgnoreCase(String x, String y);

    Page<Watch> findByIsActiveTrue(Pageable pageable);

    Page<Watch> findByBrand(String brand, Pageable pageable);

    Page<Watch> findByModelContainingIgnoreCaseOrBrandContainingIgnoreCase(Pageable pageable, String x, String y);

    Page<Watch> findByIsActiveTrueAndModelContainingIgnoreCaseOrBrandContainingIgnoreCase(String x, String y, Pageable pageable);
}
