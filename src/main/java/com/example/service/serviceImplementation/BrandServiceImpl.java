package com.example.service.serviceImplementation;

import com.example.entity.Brand;
import com.example.repository.BrandRepository;
import com.example.service.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;

@Service
public class BrandServiceImpl implements BrandService {

    @Autowired
    private BrandRepository brandRepository;

    @Override
    public Brand saveBrand(Brand brand) {
        return brandRepository.save(brand);
    }

    @Override
    public Boolean existBrand(String name) {
        return brandRepository.existsByName(name);
    }

    @Override
    public List<Brand> getAllBrand() {
        return brandRepository.findAll();
    }

    @Override
    public Boolean deleteBrand(int id) {
        Brand brand = brandRepository.findById(id).orElse(null);
        if (!ObjectUtils.isEmpty(brand)) {
            brandRepository.delete(brand);
            return true;
        }
        return false;
    }

    @Override
    public List<Brand> getAllActiveBrand() {
        return brandRepository.findByIsActiveTrue();
    }

    @Override
    public Brand getBrandById(int id) {
        return brandRepository.findById(id).orElse(null);
    }


}
