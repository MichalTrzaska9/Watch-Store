package com.example.controller;

import com.example.service.BrandService;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class AdminControllerUnitTest {

    @InjectMocks
    private AdminController adminController;

    @Mock
    private BrandService brandService;

    @Mock
    private HttpSession httpSession;

    @Test
    void testDeleteBrandSuccess() {
        int brandId = 1;
        when(brandService.deleteBrand(brandId)).thenReturn(true);

        String result = adminController.deleteBrand(brandId, httpSession);

        verify(httpSession).setAttribute("successMessage", "Marka usunięta");
        assertEquals("redirect:/admin/brand", result);
    }

    @Test
    void testDeleteBrandError() {
        int brandId = 1;
        when(brandService.deleteBrand(brandId)).thenReturn(false);

        String result = adminController.deleteBrand(brandId, httpSession);

        verify(httpSession).setAttribute("errorMessage", "Błąd serwera");
        assertEquals("redirect:/admin/brand", result);
    }
}
