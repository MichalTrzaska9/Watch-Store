package com.example.repository;

import com.example.entity.Brand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BrandRepository extends JpaRepository<Brand, Integer> {

    public List<Brand> findByIsActiveTrue();

    public Boolean existsByName(String name);


}
