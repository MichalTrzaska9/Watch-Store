package com.example.service;

import com.example.entity.Watch;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface
WatchService {

    public Watch saveWatch(Watch watch);

    public List<Watch> getAllWatches();

    public Boolean deleteWatch(Integer id);

    public Watch getWatchById(Integer id);

    public Watch updateWatch(Watch watch, MultipartFile file);

    public List<Watch> searchWatch(String search);

    public Page<Watch> getAllActiveWatchPagination(String brand, Integer pageSize, Integer pageNumber);

    public Page<Watch> searchActiveWatchPagination(String brand, Integer pageSize, Integer pageNumber, String x);

    public Page<Watch> searchWatchPagination(Integer pageSize, Integer pageNumber, String x);

    public Page<Watch> getAllWatchesPagination(Integer pageSize, Integer pageNumber);
}
