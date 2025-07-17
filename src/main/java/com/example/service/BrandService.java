package com.example.service;

import com.example.entity.Brand;

import java.util.List;

public interface BrandService {

    public Brand saveBrand(Brand brand);

    public Boolean existBrand(String name);

    public List<Brand> getAllBrand();

    public Boolean deleteBrand(int id);

    public Brand getBrandById(int id);

    public List<Brand> getAllActiveBrand();
}
