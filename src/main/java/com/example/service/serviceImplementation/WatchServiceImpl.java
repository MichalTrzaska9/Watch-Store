package com.example.service.serviceImplementation;

import com.example.entity.Watch;
import com.example.repository.WatchRepository;
import com.example.service.WatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Service
public class WatchServiceImpl implements WatchService {
    @Autowired
    private WatchRepository watchRepository;

    @Override
    public List<Watch> getAllWatches() {
        return watchRepository.findAll();
    }

    @Override
    public Watch saveWatch(Watch watch) {
        return watchRepository.save(watch);
    }

    @Override
    public Boolean deleteWatch(Integer id) {
        Watch watch = watchRepository.findById(id).orElse(null);

        if (!ObjectUtils.isEmpty(watch)) {
            watchRepository.delete(watch);
            return true;
        }
        return false;
    }

    @Override
    public Watch getWatchById(Integer id) {
        Watch watch = watchRepository.findById(id).orElse(null);
        return watch;
    }

    @Override
    public Watch updateWatch(Watch watch, MultipartFile image) {

        Watch dbWatch = getWatchById(watch.getId());

        String imageName = image.isEmpty() ? dbWatch.getImage() : image.getOriginalFilename();

        dbWatch.setModel(watch.getModel());
        dbWatch.setCondition(watch.getCondition());
        dbWatch.setDescription(watch.getDescription());
        dbWatch.setBrand(watch.getBrand());
        dbWatch.setPrice(watch.getPrice());
        dbWatch.setStock(watch.getStock());
        dbWatch.setImage(watch.getImage());
        dbWatch.setIsActive(watch.getIsActive());

        Watch updateWatch = watchRepository.save(dbWatch);
        if (!ObjectUtils.isEmpty(updateWatch)) {
            if (!image.isEmpty()) {
                try {
                    File saveFile = new ClassPathResource("static/img").getFile();

                    Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + "product_img" + File.separator
                            + image.getOriginalFilename());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return watch;
        }
        return null;
    }

    @Override
    public List<Watch> searchWatch(String x) {
        return watchRepository.findByModelContainingIgnoreCaseOrBrandContainingIgnoreCase(x, x);
    }

    @Override
    public Page<Watch> getAllActiveWatchPagination(String brand, Integer pageSize, Integer pageNumber) {
        Pageable pageable = PageRequest.of(pageSize, pageNumber);
        Page<Watch> page = null;
        if (!ObjectUtils.isEmpty(brand)) {
            page = watchRepository.findByBrand(brand, pageable);
        } else {
            page = watchRepository.findByIsActiveTrue(pageable);
        }
        return page;
    }

    @Override
    public Page<Watch> searchActiveWatchPagination(String brand, Integer pageSize, Integer pageNumber, String x) {

        Page<Watch> watchPage = null;
        Pageable pageable = PageRequest.of(pageSize, pageNumber);

        watchPage = watchRepository.findByIsActiveTrueAndModelContainingIgnoreCaseOrBrandContainingIgnoreCase(x, x, pageable);

        return watchPage;
    }

    @Override
    public Page<Watch> searchWatchPagination(Integer pageSize, Integer pageNumber, String x) {
        Pageable pageable = PageRequest.of(pageSize, pageNumber);
        return watchRepository.findByModelContainingIgnoreCaseOrBrandContainingIgnoreCase(pageable, x, x);
    }

    @Override
    public Page<Watch> getAllWatchesPagination(Integer pageSize, Integer pageNumber) {
        Pageable pageable = PageRequest.of(pageSize, pageNumber, Sort.Direction.DESC, "id");
        return watchRepository.findAll(pageable);
    }


}
