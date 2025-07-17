package com.example.integration;

import com.example.entity.Watch;
import com.example.service.serviceImplementation.WatchServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(properties = "spring.config.location=classpath:application-test.properties")
public class WatchServiceIntegrationTest {

    @Autowired
    private WatchServiceImpl watchService;

    @Test
    void testSaveAndRetrieveWatch() {
        // given
        Watch watch = new Watch();
        watch.setModel("Explorer");
        watch.setBrand("Rolex");
        watch.setDescription("Luxury watch");
        watch.setCondition("New");
        watch.setPrice(BigDecimal.valueOf(9999.99));
        watch.setStock(5);
        watch.setImage("explorer.jpg");
        watch.setIsActive(true);

        // when
        Watch savedWatch = watchService.saveWatch(watch);
        Watch fetchedWatch = watchService.getWatchById(savedWatch.getId());

        // then
        assertEquals("Explorer", fetchedWatch.getModel());
        assertEquals("Rolex", fetchedWatch.getBrand());
        assertTrue(fetchedWatch.getIsActive());
    }

    @Test
    void testDeleteWatchSuccess() {
        // given
        Watch watch = new Watch();
        watch.setModel("Submariner");
        watch.setBrand("Rolex");
        watch.setDescription("aaaaa");
        watch.setCondition("Used");
        watch.setPrice(BigDecimal.valueOf(78500.00));
        watch.setStock(2);
        watch.setImage("submariner.jpg");
        watch.setIsActive(true);

        Watch savedWatch = watchService.saveWatch(watch);
        Integer id = savedWatch.getId();

        // when
        Boolean deleted = watchService.deleteWatch(id);
        Watch fetchedAfterDelete = watchService.getWatchById(id);

        // then
        assertTrue(deleted);
        assertNull(fetchedAfterDelete);
    }
}
