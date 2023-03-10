package com.banquito.core.productsaccounts.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.banquito.core.productsaccounts.controller.dto.InterestRateRQRS;
import com.banquito.core.productsaccounts.exception.CRUDException;
import com.banquito.core.productsaccounts.model.InterestRate;
import com.banquito.core.productsaccounts.service.InterestRateService;

public class InterestRateControllerTest {
    @Mock
    private InterestRateService interestRateService;

    @InjectMocks
    private InterestRateController interestRateController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testObtainAll() {
        List<InterestRate> interestRates = new ArrayList<>();
        InterestRate interestRate1 = new InterestRate();
        interestRate1.setId(1);
        interestRate1.setName("InterestRate1");
        interestRate1.setInterestRate(new BigDecimal("0.0250"));
        interestRate1.setState("ACT");
        interestRate1.setStart(new Date());
        interestRates.add(interestRate1);

        InterestRate interestRate2 = new InterestRate();
        interestRate2.setId(2);
        interestRate2.setName("InterestRate2");
        interestRate2.setInterestRate(new BigDecimal("0.0350"));
        interestRate2.setState("INA");
        interestRate2.setStart(new Date());
        interestRate2.setEnd(new Date());
        interestRates.add(interestRate2);

        when(interestRateService.listAllActives()).thenReturn(interestRates);

        ResponseEntity<List<InterestRateRQRS>> response = interestRateController.obtainAll();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        List<InterestRateRQRS> responseList = response.getBody();
        assertNotNull(responseList);
        assertEquals(2, responseList.size());

        InterestRateRQRS responseInterestRate1 = responseList.get(0);
        assertEquals(1, responseInterestRate1.getId());
        assertEquals("InterestRate1", responseInterestRate1.getName());
        assertEquals(new BigDecimal("0.0250"), responseInterestRate1.getInterestRate());
        assertEquals("ACT", responseInterestRate1.getState());
        assertNotNull(responseInterestRate1.getStart());
        assertNull(responseInterestRate1.getEnd());
    }

    @Test
    void testObtainByCode() {
        InterestRate interestRate = new InterestRate();
        interestRate.setId(1);
        interestRate.setName("Interest Rate 1");
        interestRate.setInterestRate(new BigDecimal("0.05"));
        interestRate.setState("ACT");
        interestRate.setStart(new Date());

        when(interestRateService.obtainById(1)).thenReturn(interestRate);

        ResponseEntity<InterestRateRQRS> response = interestRateController.obtainByCode("1");
        assertEquals(HttpStatus.OK, response.getStatusCode());

        InterestRateRQRS responseInterestRate = response.getBody();
        assertNotNull(responseInterestRate);
        assertEquals("Interest Rate 1", responseInterestRate.getName());
        assertEquals(new BigDecimal("0.05"), responseInterestRate.getInterestRate());
        assertEquals("ACT", responseInterestRate.getState());
    }

    @Test
    void testObtainByCode_NotFound() {
        when(interestRateService.obtainById(1)).thenReturn(null);

        ResponseEntity<InterestRateRQRS> response = interestRateController.obtainByCode("1");
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

        
        @Test
    void testCreate() throws CRUDException {
        InterestRateRQRS interestRateRQRS = InterestRateRQRS.builder()
                .name("test interest rate")
                .interestRate(new BigDecimal("0.05"))
                .state("ACT")
                .start(new Date())
                .build();

        doNothing().when(interestRateService).create(any(InterestRate.class));

        ResponseEntity<?> response = interestRateController.create(interestRateRQRS);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        
    }


    @Test
    void testDelete() {
        String interestRateId = "1";

        doNothing().when(interestRateService).inactivate(Mockito.anyInt());

        ResponseEntity<?> response = interestRateController.delete(interestRateId);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void testDelete_CRUDException() {
        String interestRateId = "1";
        int errorCode = 400;

        doThrow(new CRUDException(errorCode, "Error deleting interest rate")).when(interestRateService).inactivate(Mockito.anyInt());

        ResponseEntity<?> response = interestRateController.delete(interestRateId);
        assertEquals(HttpStatus.valueOf(errorCode), response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void testUpdate() throws CRUDException {
        InterestRateRQRS interestRateRQRS = InterestRateRQRS.builder()
                .name("test interest rate")
                .interestRate(new BigDecimal("0.05"))
                .state("ACT")
                .start(new Date())
                .build();
        
        InterestRate interestRate = Mockito.mock(InterestRate.class);
        when(interestRate.getId()).thenReturn(1);
        when(interestRate.getName()).thenReturn(interestRateRQRS.getName());
        when(interestRate.getInterestRate()).thenReturn(interestRateRQRS.getInterestRate());
        when(interestRate.getState()).thenReturn(interestRateRQRS.getState());
        when(interestRate.getStart()).thenReturn(interestRateRQRS.getStart());
        doNothing().when(interestRateService).update(any(Integer.class), any(InterestRate.class));
        when(interestRateService.obtainById(any(Integer.class))).thenReturn(interestRate);

        ResponseEntity<InterestRateRQRS> response = interestRateController.update("1", interestRateRQRS);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        InterestRateRQRS responseInterestRate = response.getBody();
        assertNotNull(responseInterestRate);
        assertEquals("test interest rate", responseInterestRate.getName());
        assertEquals(new BigDecimal("0.05"), responseInterestRate.getInterestRate());
        assertEquals("ACT", responseInterestRate.getState());
        assertNotNull(responseInterestRate.getStart());
    }

}
