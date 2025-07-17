package com.example.service;

import com.example.entity.Watch;
import com.example.repository.WatchRepository;
import com.example.service.serviceImplementation.WatchServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class WatchServiceImplTest {

    @Mock
    private WatchRepository watchRepository;

    @InjectMocks
    private WatchServiceImpl watchService;

    @Test
    void testReturnAllWatches() {
        Watch watch1 = new Watch();
        Watch watch2 = new Watch();
        List<Watch> watches = Arrays.asList(watch1, watch2);

        when(watchRepository.findAll()).thenReturn(watches);

        List<Watch> result = watchService.getAllWatches();

        assertEquals(2, result.size());
        verify(watchRepository).findAll();
    }

    @Test
    void testReturnEmptyListWhenNoWatches() {
        when(watchRepository.findAll()).thenReturn(List.of());

        List<Watch> result = watchService.getAllWatches();

        assertTrue(result.isEmpty());
        verify(watchRepository).findAll();
    }

    @Test
    void testDeleteWatchWhenExists() {
        Integer id = 1;
        Watch watch = new Watch();
        watch.setId(id);

        when(watchRepository.findById(id)).thenReturn(Optional.of(watch));
        doNothing().when(watchRepository).delete(watch);

        Boolean result = watchService.deleteWatch(id);

        assertTrue(result);
        verify(watchRepository).findById(id);
        verify(watchRepository).delete(watch);
    }

    @Test
    void testReturnFalseWhenWatchDoesNotExist() {
        Integer id = 99;
        when(watchRepository.findById(id)).thenReturn(Optional.empty());

        Boolean result = watchService.deleteWatch(id);

        assertFalse(result);
        verify(watchRepository).findById(id);
        verify(watchRepository, never()).delete(any());
    }
}
